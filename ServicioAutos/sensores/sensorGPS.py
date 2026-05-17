import json
import random

def publish_gps(client, auto_id, placa, estado):
    estado["latitud"] += random.uniform(-0.0001, 0.0001)
    estado["longitud"] += random.uniform(-0.0001, 0.0001)

    payload_gps = json.dumps({
        "sensor": "gps",
        "placa": placa,
        "latitud": round(estado["latitud"], 6),
        "longitud": round(estado["longitud"], 6)
    })
    
    client.publish(f"flota/{auto_id}/gps", payload_gps)
