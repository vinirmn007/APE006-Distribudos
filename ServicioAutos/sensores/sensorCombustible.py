import json

def publish_combustible(client, auto_id, placa, estado):
    # Actualizar estado
    estado["combustible"] = max(0, estado["combustible"] - 0.5)

    payload_combustible = json.dumps({
        "sensor": "combustible",
        "placa": placa,
        "nivel": round(estado["combustible"], 2),
        "unidad": "%"
    })
    
    client.publish(f"flota/{auto_id}/combustible", payload_combustible)
