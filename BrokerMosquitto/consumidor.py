from controllers.database import init_db
from controllers.mosquitto import start_mqtt

if __name__ == "__main__":
    # Inicializar la base de datos (crear la tabla si no existe)
    init_db()

    # Iniciar la conexión al broker MQTT y comenzar a escuchar los mensajes
    start_mqtt()
