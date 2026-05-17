package fleet.consumers;

import fleet.dto.FuelTelemetry;
import fleet.dto.AlertTelemetry;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import fleet.service.FleetService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FuelConsumer {

    private final FleetService fleetService;
    private final RabbitTemplate rabbitTemplate;

    public FuelConsumer(FleetService fleetService, RabbitTemplate rabbitTemplate) {
        this.fleetService = fleetService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "fuel_queue")
    public void consumeMessage(FuelTelemetry fuelTelemetry) {
        System.out.println("Received message from fuel_queue: " + fuelTelemetry);
        try {
            fleetService.saveLatestFuel(fuelTelemetry);

            if (fuelTelemetry.getNivel() != null && fuelTelemetry.getNivel() < 20.0) {
                AlertTelemetry alert = new AlertTelemetry();
                alert.setSensor("fuel_alert");
                alert.setPlaca(fuelTelemetry.getPlaca());
                alert.setValor(fuelTelemetry.getNivel());
                alert.setUnidad(fuelTelemetry.getUnidad());

                rabbitTemplate.convertAndSend("exchange.fleet", "notify.fleet", alert);
                System.out.println("Published low fuel alert to notifications_queue for " + fuelTelemetry.getPlaca());
            }
        } catch (Exception e) {
            System.err.println("Error saving fuel telemetry: " + e.getMessage());
        }
    }
}
