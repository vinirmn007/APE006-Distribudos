package fleet.controller;

import fleet.dto.GpsTelemetry;
import fleet.service.FleetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.List;
import fleet.dto.FuelTelemetry;
import fleet.dto.AlertTelemetry;

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

    @GetMapping("/vehicle/{id}/fuel")
    public ResponseEntity<FuelTelemetry> getVehicleFuel(@PathVariable("id") String id) {
        return fleetService.getVehicleFuel(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/vehicle/{id}/alerts")
    public ResponseEntity<List<AlertTelemetry>> getVehicleAlerts(@PathVariable("id") String id) {
        return ResponseEntity.ok(fleetService.getVehicleAlerts(id));
    }
}