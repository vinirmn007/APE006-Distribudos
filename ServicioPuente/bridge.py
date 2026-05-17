import paho.mqtt.client as mqtt
import pika
import os
import json
import time

MQTT_BROKER = os.getenv("MQTT_BROKER", "mosquitto")
MQTT_PORT = int(os.getenv("MQTT_PORT", 1883))
MQTT_TOPIC = os.getenv("MQTT_TOPIC", "#") 

RABBITMQ_HOST = os.getenv("RABBITMQ_HOST", "rabbitmq")
RABBITMQ_QUEUE = os.getenv("RABBITMQ_QUEUE", "telemetria_queue")

rabbitmq_connection = None
rabbitmq_channel = None

def init_rabbitmq():
    global rabbitmq_connection, rabbitmq_channel
    while True:
        try:
            rabbitmq_connection = pika.BlockingConnection(pika.ConnectionParameters(host=RABBITMQ_HOST))
            rabbitmq_channel = rabbitmq_connection.channel()
            rabbitmq_channel.queue_declare(queue=RABBITMQ_QUEUE, durable=True)
            print(f"conectado a RabbitMQ en {RABBITMQ_HOST}")
            break
        except Exception as e:
            print(f"Fallo al conectar a RabbitMQ: {e}. Reintentando en 5 segundos...")
            time.sleep(5)

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print(f"conectado a Mosquitto MQTT broker en {MQTT_BROKER}")
        client.subscribe(MQTT_TOPIC)
    else:
        print(f"Fallo al conectar a Mosquitto, return code {rc}")

def on_message(client, userdata, msg):
    global rabbitmq_connection, rabbitmq_channel
    try:
        payload = msg.payload.decode('utf-8')
        topic = msg.topic
        print(f"Mensaje recibido de Mosquitto - Topic: {topic}")

        try:
            data = json.loads(payload)
        except json.JSONDecodeError:
            data = payload

        mensaje_rmq = {
            "topic": topic,
            "data": data
        }

        # Check connection and reconnect if necessary
        if rabbitmq_connection is None or rabbitmq_connection.is_closed:
            init_rabbitmq()

        rabbitmq_channel.basic_publish(
            exchange='exchange.fleet',
            routing_key=RABBITMQ_QUEUE,
            body=json.dumps(mensaje_rmq),
            properties=pika.BasicProperties(
                delivery_mode=2,  # Delivery mode 2 para hacer el mensaje persistente (no se pierda)
            )
        )
        print(f"Enviado a la cola de RabbitMQ '{RABBITMQ_QUEUE}' con delivery_mode=2")

    except Exception as e:
        print(f"Error al procesar el mensaje: {e}")

if __name__ == '__main__':
    print("Iniciando Servicio Puente...")
    init_rabbitmq()

    mqtt_client = mqtt.Client()
    mqtt_client.on_connect = on_connect
    mqtt_client.on_message = on_message

    while True:
        try:
            mqtt_client.connect(MQTT_BROKER, MQTT_PORT, 60)
            break
        except Exception as e:
            print(f"Fallo al conectar a Mosquitto: {e}. Reintentando en 5 segundos...")
            time.sleep(5)

    mqtt_client.loop_forever()
