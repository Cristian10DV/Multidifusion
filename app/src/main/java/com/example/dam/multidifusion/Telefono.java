package com.example.dam.multidifusion;

public class Telefono {

    private String numeroTelefono, fechayHora, tipoLlamada;

    public Telefono(String numeroTelefono, String fechayHora, String tipoLlamada) {
        this.numeroTelefono = numeroTelefono;
        this.fechayHora = fechayHora;
        this.tipoLlamada = tipoLlamada;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getFechayHora() {
        return fechayHora;
    }

    public void setFechayHora(String fechayHora) {
        this.fechayHora = fechayHora;
    }

    public String getTipoLlamada() {
        return tipoLlamada;
    }

    public void setTipoLlamada(String tipoLlamada) {
        this.tipoLlamada = tipoLlamada;
    }

    @Override
    public String toString() {
        return "Telefono{" +
                "numero de telefono='" + numeroTelefono + '\'' +
                ", fecha y Hora='" + fechayHora + '\'' +
                ", tipo de llamada='" + tipoLlamada + '\'' +
                '}';
    }
}
