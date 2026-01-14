package granjaautomatizada.modelo;

public class Cultivo {

    private String nombre;
    private int humedadMinima;
    private int humedadMaxima;
    private int frecuenciaRiegoHoras;

    public Cultivo(String nombre, int humedadMinima, int humedadMaxima, int frecuenciaRiegoHoras) {
        this.nombre = nombre;
        this.humedadMinima = humedadMinima;
        this.humedadMaxima = humedadMaxima;
        this.frecuenciaRiegoHoras = frecuenciaRiegoHoras;
    }

    public String getNombre() {
        return nombre;
    }

    public int getHumedadMinima() {
        return humedadMinima;
    }

    public int getHumedadMaxima() {
        return humedadMaxima;
    }

    public int getFrecuenciaRiegoHoras() {
        return frecuenciaRiegoHoras;
    }
}