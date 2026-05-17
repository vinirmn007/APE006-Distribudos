import pika
import time

RABBITMQ_HOST = 'rabbitmq'
# ==========================================
# CREATE CONNECTION TO RABBITMQ
# ==========================================

while True:
    try:
        connection = pika.BlockingConnection(
            pika.ConnectionParameters(
                host=RABBITMQ_HOST,
                credentials=pika.PlainCredentials(
                    "admin",
                    "admin"
                )
            )
        )

        break
    except Exception:
        print("Waiting for RabbitMQ...")
        time.sleep(5)

channel = connection.channel()

# ==========================================
# CREATE DIRECT EXCHANGE
# ==========================================

channel.exchange_declare(
    exchange="exchange.fleet",
    exchange_type="direct",
    durable=True
)

# ==========================================
# CREATE QUEUES
# ==========================================

queues = [
    "gps_queue",
    "alerts_queue",
    "fuel_queue",
    "notifications_queue"
]

for queue in queues:
    channel.queue_declare(
        queue=queue,
        durable=True
    )

# ==========================================
# BIND ROUTING KEYS
# ==========================================

bindings = [
    ("gps_queue", "gps.telemetry"),
    ("alerts_queue", "alert.critical"),
    ("fuel_queue", "fuel.level"),
    ("notifications_queue", "notify.fleet")
]

for queue_name, routing_key in bindings:

    channel.queue_bind(
        exchange="exchange.fleet",
        queue=queue_name,
        routing_key=routing_key
    )

    print(
        f"Bound {queue_name} -> {routing_key}"
    )

print("\nRabbitMQ infrastructure created successfully.")

connection.close()