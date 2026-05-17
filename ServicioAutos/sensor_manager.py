import time
import random
import os
import threading
from sqlalchemy.orm import Session
import paho.mqtt.client as mqtt

from database import SessionLocal
from models import Auto
from sensores.sensorGPS import publish_gps
from sensores.sensorCombustible import publish_combustible
from sensores.sensorTemperatura import publish_temperatura

BROKER = os.environ.get("MQTT_BROKER", "mosquitto")
PORT = 1883

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("ServicioAutos conectado al broker MQTT exitosamente")
    else:
        print(f"Fallo en la conexión MQTT, código de retorno {rc}")

def simulate_sensors():
    client = mqtt.Client()
    client.on_connect = on_connect

    while True:
        try:
            client.connect(BROKER, PORT, 60)
            break
        except Exception as e:
            print(f"Esperando al broker MQTT... ({e})")
            time.sleep(5)
            
    client.loop_start()

    # Variables de estado inicial para la simulación
    estado_vehiculos = {}

    while True:
        try:
            db: Session = SessionLocal()
            autos = db.query(Auto).all()
            db.close()

            for auto in autos:
                placa = auto.placa
                if placa not in estado_vehiculos:
                    estado_vehiculos[placa] = {
                        "latitud": random.uniform(-4.0, -3.9),
                        "longitud": random.uniform(-79.3, -79.1),
                        "combustible": 100.0,
                        "temperatura": 90.0
                    }
                
                estado = estado_vehiculos[placa]

                # Invocar los módulos individuales
                publish_gps(client, auto.id, placa, estado)
                publish_combustible(client, auto.id, placa, estado)
                publish_temperatura(client, auto.id, placa, estado)

                print(f"[Telemetría Enviada] Placa: {placa}")
        except Exception as e:
            print(f"Error en la simulación: {e}")

        time.sleep(3) # Pausa de 3 segundos antes del siguiente ciclo

def start_sensor_simulation():
    thread = threading.Thread(target=simulate_sensors, daemon=True)
    thread.start()
