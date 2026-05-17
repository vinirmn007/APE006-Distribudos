package fleet.consumers;

import fleet.dto.AlertTelemetry;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AlertConsumer {

    @RabbitListener(queues = "alerts_queue")
    public void consumeMessage(AlertTelemetry alertTelemetry) {
        System.out.println("Received message from alerts_queue: " + alertTelemetry);
        // TODO: Implementar lógica para guardar alertas
    }
}
