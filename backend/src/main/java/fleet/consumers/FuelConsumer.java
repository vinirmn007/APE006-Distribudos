package fleet.consumers;

import fleet.dto.FuelTelemetry;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FuelConsumer {

    @RabbitListener(queues = "fuel_queue")
    public void consumeMessage(FuelTelemetry fuelTelemetry) {
        System.out.println("Received message from fuel_queue: " + fuelTelemetry);
        // TODO: Implementar lógica para guardar combustible
    }
}
