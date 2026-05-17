package main.java.fleet.dto;

public class FuelTelemetry {

    private String sensor;
    private String placa;
    private Double nivel;
    private String unidad;

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

    public Double getNivel() {
        return nivel;
    }

    public void setNivel(Double nivel) {
        this.nivel = nivel;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    @Override
    public String toString() {
        return "FuelTelemetry{" +
                "sensor='" + sensor + '\'' +
                ", placa='" + placa + '\'' +
                ", nivel=" + nivel +
                ", unidad='" + unidad + '\'' +
                '}';
    }
}
