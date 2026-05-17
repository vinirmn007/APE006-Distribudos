package main.java.fleet.consumers;

import main.java.fleet.dto.GpsTelemetry;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class GpsConsumer {

    @RabbitListener(queues = "gps_queue")
    public void consumeMessage(GpsTelemetry gpsTelemetry) {
        System.out.println("Received message from gps_queue: " + gpsTelemetry);
    }
}
