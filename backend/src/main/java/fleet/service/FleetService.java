package fleet.service;

import fleet.dto.GpsTelemetry;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FleetService {

    private final ConcurrentHashMap<String, GpsTelemetry> latestTelemetryByVehicle = new ConcurrentHashMap<>();

    public GpsTelemetry saveLatestTelemetry(GpsTelemetry telemetry) {
        if (telemetry == null) {
            throw new IllegalArgumentException("telemetry must not be null");
        }

        String vehicleId = telemetry.getPlaca();
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("telemetry.placa must not be blank");
        }

        latestTelemetryByVehicle.put(vehicleId, telemetry);
        return telemetry;
    }

    public Map<String, GpsTelemetry> getFleetStatus() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(latestTelemetryByVehicle));
    }

    public Optional<GpsTelemetry> getVehicleTelemetry(String vehicleId) {
        if (vehicleId == null || vehicleId.isBlank()) {
            return Optional.empty();
        }

        return Optional.ofNullable(latestTelemetryByVehicle.get(vehicleId));
    }
}