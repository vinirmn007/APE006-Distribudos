package fleet.controller;

import fleet.dto.GpsTelemetry;
import fleet.service.FleetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/fleet")
public class FleetController {

    private final FleetService fleetService;

    public FleetController(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, GpsTelemetry>> getFleetStatus() {
        return ResponseEntity.ok(fleetService.getFleetStatus());
    }

    @GetMapping("/vehicle/{id}/telemetria")
    public ResponseEntity<GpsTelemetry> getVehicleTelemetry(@PathVariable("id") String id) {
        return fleetService.getVehicleTelemetry(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}