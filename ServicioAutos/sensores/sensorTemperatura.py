import json
import random

def publish_temperatura(client, auto_id, placa, estado):
    estado["temperatura"] = round(random.uniform(85.0, 105.0), 2)

    payload_temperatura = json.dumps({
        "sensor": "temperatura",
        "placa": placa,
        "valor": estado["temperatura"],
        "unidad": "C"
    })
    
    client.publish(f"flota/{auto_id}/temperatura", payload_temperatura)
