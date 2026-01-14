package granjaautomatizada.modelo;

import java.util.ArrayList;

public class Parcela {

    private String id;
    private double metrosCuadrados;

    private Cultivo cultivo;

    private ArrayList<Aspersor> aspersores;
    private ArrayList<SensorHumedad> sensores;

    public Parcela(String id, double metrosCuadrados) {
        this.id = id;
        this.metrosCuadrados = metrosCuadrados;
        this.aspersores = new ArrayList<>();
        this.sensores = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public double getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(double metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public Cultivo getCultivo() {
        return cultivo;
    }

    public void setCultivo(Cultivo cultivo) {
        this.cultivo = cultivo;
    }

    public ArrayList<Aspersor> getAspersores() {
        return aspersores;
    }

    public ArrayList<SensorHumedad> getSensores() {
        return sensores;
    }
}

