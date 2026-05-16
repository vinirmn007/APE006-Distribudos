import sqlite3
import json
import os
from datetime import datetime

DB_NAME = "data/telemetria.db"

def init_db():
    os.makedirs(os.path.dirname(DB_NAME), exist_ok=True)
    conn = sqlite3.connect(DB_NAME)
    cursor = conn.cursor()
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS datos_telemetria (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            topic TEXT,
            sensor TEXT,
            payload TEXT,
            fecha_hora DATETIME
        )
    ''')
    conn.commit()
    conn.close()

def save_to_db(topic, payload_str):
    try:
        data = json.loads(payload_str)
        sensor = data.get("sensor", "desconocido")
    except json.JSONDecodeError:
        sensor = "desconocido"

    fecha_hora = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    conn = sqlite3.connect(DB_NAME)
    cursor = conn.cursor()
    cursor.execute('''
        INSERT INTO datos_telemetria (topic, sensor, payload, fecha_hora)
        VALUES (?, ?, ?, ?)
    ''', (topic, sensor, payload_str, fecha_hora))
    conn.commit()
    conn.close()
