package fleet.consumers;

import fleet.dto.FuelTelemetry;
import fleet.service.FleetService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FuelConsumer {

    private final FleetService fleetService;

    public FuelConsumer(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    @RabbitListener(queues = "fuel_queue")
    public void consumeMessage(FuelTelemetry fuelTelemetry) {
        System.out.println("Received message from fuel_queue: " + fuelTelemetry);
        try {
            fleetService.saveLatestFuel(fuelTelemetry);
        } catch (Exception e) {
            System.err.println("Error saving fuel telemetry: " + e.getMessage());
        }
    }
}
