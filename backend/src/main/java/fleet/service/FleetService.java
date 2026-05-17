package fleet.service;

import fleet.dto.AlertTelemetry;
import fleet.dto.FuelTelemetry;
import fleet.dto.GpsTelemetry;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FleetService {

    private final ConcurrentHashMap<String, GpsTelemetry> latestTelemetryByVehicle = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, FuelTelemetry> latestFuelByVehicle = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<AlertTelemetry>> alertsByVehicle = new ConcurrentHashMap<>();

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

    public FuelTelemetry saveLatestFuel(FuelTelemetry fuel) {
        if (fuel == null) {
            throw new IllegalArgumentException("fuel must not be null");
        }
        String vehicleId = fuel.getPlaca();
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("fuel.placa must not be blank");
        }
        latestFuelByVehicle.put(vehicleId, fuel);
        return fuel;
    }

    public AlertTelemetry saveAlert(AlertTelemetry alert) {
        if (alert == null) {
            throw new IllegalArgumentException("alert must not be null");
        }
        String vehicleId = alert.getPlaca();
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("alert.placa must not be blank");
        }
        alertsByVehicle.computeIfAbsent(vehicleId, k -> Collections.synchronizedList(new ArrayList<>())).add(alert);
        return alert;
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

    public Optional<FuelTelemetry> getVehicleFuel(String vehicleId) {
        if (vehicleId == null || vehicleId.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(latestFuelByVehicle.get(vehicleId));
    }

    public List<AlertTelemetry> getVehicleAlerts(String vehicleId) {
        if (vehicleId == null || vehicleId.isBlank()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(alertsByVehicle.getOrDefault(vehicleId, Collections.emptyList()));
    }
}