package fleet.consumers;

import fleet.dto.GpsTelemetry;
import fleet.service.FleetService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class GpsConsumer {

    private final FleetService fleetService;

    public GpsConsumer(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    @RabbitListener(queues = "gps_queue")
    public void consumeMessage(GpsTelemetry gpsTelemetry) {
        System.out.println("Received message from gps_queue: " + gpsTelemetry);
        fleetService.saveLatestTelemetry(gpsTelemetry);
    }
}
