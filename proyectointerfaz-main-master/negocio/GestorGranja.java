package granjaautomatizada.negocio;


import granjaautomatizada.modelo.*;
import java.util.ArrayList;

public class GestorGranja {

    private ArrayList<Parcela> parcelas;
    private ArrayList<Aspersor> aspersoresInventario;
    private ArrayList<SensorHumedad> sensoresInventario;

    private GestorParcelas gestorParcelas;
    private GestorAspersores gestorAspersores;
    private GestorSensores gestorSensores;
    private GestorCultivos gestorCultivos;

    private int contadorIdAspersores = 0;
    private int contadorIdSensores = 0;

    public GestorGranja() {
        parcelas = new ArrayList<>();
        aspersoresInventario = new ArrayList<>();
        sensoresInventario = new ArrayList<>();

        gestorParcelas = new GestorParcelas(this);
        gestorAspersores = new GestorAspersores(this);
        gestorSensores = new GestorSensores(this);
        gestorCultivos = new GestorCultivos(this);
    }

    public int getSiguienteIdAspersor() {
        contadorIdAspersores++;
        return contadorIdAspersores;
    }

    public int getSiguienteIdSensor() {
        contadorIdSensores++;
        return contadorIdSensores;
    }

    public ArrayList<Parcela> getParcelas() {
        return parcelas;
    }

    public ArrayList<Aspersor> getAspersoresInventario() {
        return aspersoresInventario;
    }

    public ArrayList<SensorHumedad> getSensoresInventario() {
        return sensoresInventario;
    }

    public GestorParcelas getGestorParcelas() {
        return gestorParcelas;
    }

    public GestorAspersores getGestorAspersores() {
        return gestorAspersores;
    }

    public GestorSensores getGestorSensores() {
        return gestorSensores;
    }

    public GestorCultivos getGestorCultivos() {
        return gestorCultivos;
    }

}