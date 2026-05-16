import paho.mqtt.client as mqtt
import os
from controllers.database import save_to_db

# Configuración del broker MQTT
BROKER = os.environ.get("MQTT_BROKER", "mosquitto")
PORT = 1883
TOPIC = "flota/#"

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("Conectado al broker MQTT exitosamente (Consumidor)")
        client.subscribe(TOPIC)
        print(f"Suscrito al tema: {TOPIC}")
    else:
        print(f"Fallo en la conexión, código de retorno {rc}")

def on_message(client, userdata, msg):
    payload_str = msg.payload.decode('utf-8')
    print(f"[Recibido] Tema: {msg.topic} | Mensaje: {payload_str}")
    save_to_db(msg.topic, payload_str)

def start_mqtt():
    client = mqtt.Client()
    client.on_connect = on_connect
    client.on_message = on_message

    try:
        print("Iniciando conexión al broker MQTT...")
        client.connect(BROKER, PORT, 60)
        client.loop_forever()
    except KeyboardInterrupt:
        print("\nConsumidor detenido.")
    except Exception as e:
        print(f"Error al conectar con el broker: {e}")
    finally:
        client.disconnect()
