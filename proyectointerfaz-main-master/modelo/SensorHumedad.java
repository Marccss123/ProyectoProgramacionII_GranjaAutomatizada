package granjaautomatizada.modelo;

import java.util.ArrayList;

public class SensorHumedad {

    private String id;
    private boolean conectado;
    private int humedadActual = 50; // valor inicial

    private Parcela parcela;

    private ArrayList<LecturaHumedad> lecturas;

    public SensorHumedad(String id) {
        this.id = id;
        this.conectado = true;
        this.lecturas = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public Parcela getParcela() {
        return parcela;
    }

    public void setParcela(Parcela parcela) {
        this.parcela = parcela;
    }

    public ArrayList<LecturaHumedad> getLecturas() {
        return lecturas;
    }

    public LecturaHumedad leerHumedad() {

        if (!conectado) {
            return null;
        }

        humedadActual = granjaautomatizada.utilitario.Util.generarHumedadProgresiva(humedadActual);

        LecturaHumedad lectura = new LecturaHumedad(
                granjaautomatizada.utilitario.Util.obtenerFechaHoraActual(),
                humedadActual
        );

        lecturas.add(lectura);

        return lectura;
    }

}