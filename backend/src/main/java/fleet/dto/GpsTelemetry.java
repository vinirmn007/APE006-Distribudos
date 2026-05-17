package main.java.fleet.dto;

public class GpsTelemetry {

    private String sensor;
    private String placa;
    private Double latitud;
    private Double longitud;

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "GpsTelemetry{" +
                "sensor='" + sensor + '\'' +
                ", placa='" + placa + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}