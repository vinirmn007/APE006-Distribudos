package main.java.fleet.dto;

public class AlertTelemetry {

    private String sensor;
    private String placa;
    private Double valor;
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

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    @Override
    public String toString() {
        return "AlertTelemetry{" +
                "sensor='" + sensor + '\'' +
                ", placa='" + placa + '\'' +
                ", valor=" + valor +
                ", unidad='" + unidad + '\'' +
                '}';
    }
}
