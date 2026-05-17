package fleet.consumers;

import fleet.dto.AlertTelemetry;
import fleet.service.FleetService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AlertConsumer {

    private final FleetService fleetService;

    public AlertConsumer(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    @RabbitListener(queues = {"alerts_queue", "notifications_queue"})
    public void consumeMessage(AlertTelemetry alertTelemetry) {
        System.out.println("Received message from alerts_queue: " + alertTelemetry);
        try {
            fleetService.saveAlert(alertTelemetry);
        } catch (Exception e) {
            System.err.println("Error saving alert telemetry: " + e.getMessage());
        }
    }
}
