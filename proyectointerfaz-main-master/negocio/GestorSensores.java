package granjaautomatizada.negocio;

import granjaautomatizada.modelo.*;
import granjaautomatizada.utilitario.GranjaException;
import java.util.Scanner;

public class GestorSensores {

    private GestorGranja gestorGranja;

    public GestorSensores(GestorGranja gestorGranja) {
        this.gestorGranja = gestorGranja;
    }
    public void mostrarInfoSensores() {

        System.out.println("\n=== INFORMACIÓN DE SENSORES DE HUMEDAD ===");
        System.out.println("Cantidad total de sensores: "
                + gestorGranja.getSensoresInventario().size());

        for (SensorHumedad sensor : gestorGranja.getSensoresInventario()) {

            System.out.println("\n" + sensor.getId() + ":");

            if (sensor.getParcela() != null) {
                System.out.println("  Se encuentra en: "
                        + sensor.getParcela().getId());
            } else {
                System.out.println("  Se encuentra en: INVENTARIO");
            }

            if (!sensor.getLecturas().isEmpty()) {
                LecturaHumedad ultima =
                        sensor.getLecturas().get(sensor.getLecturas().size() - 1);
                System.out.println("  Última lectura: "
                        + ultima.getPorcentajeHumedad() + "%");
            } else {
                System.out.println("  Última lectura: SIN LECTURAS");
            }

            System.out.println("  Conexión: "
                    + (sensor.isConectado() ? "CONECTADO" : "DESCONECTADO"));
        }
    }
    public void agregarSensoresAlInventario(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            int siguienteNum = gestorGranja.getSiguienteIdSensor();
            String id = granjaautomatizada.utilitario.Util.generarId("SENSOR", siguienteNum);
            gestorGranja.getSensoresInventario().add(new SensorHumedad(id));
        }

        System.out.println(" Se agregaron " + cantidad + " sensores al inventario.");
    }
    public void asignarSensorAParcela(String idParcela) throws GranjaException {

        SensorHumedad disponible = null;
        for (SensorHumedad s : gestorGranja.getSensoresInventario()) {
            if (s.getParcela() == null) {
                disponible = s;
                break;
            }
        }

        if (disponible == null) {
            throw new GranjaException("No hay sensores disponibles en el inventario. (Agregue más con la Opción 8)");
        }

        granjaautomatizada.modelo.Parcela parcelaDestino = null;

        for (granjaautomatizada.modelo.Parcela parcela : gestorGranja.getParcelas()) {
            if (parcela.getId().equalsIgnoreCase(idParcela)) {
                parcelaDestino = parcela;
                break;
            }
        }

        if (parcelaDestino == null) {
            throw new GranjaException("No se encontró la parcela con ID: " + idParcela);
        }

        disponible.setParcela(parcelaDestino);
        parcelaDestino.getSensores().add(disponible);
        System.out.println("Sensor " + disponible.getId() + " asignado a " + parcelaDestino.getId());
    }
    public void mostrarHumedadParcelas() {

        System.out.println("\n=== HUMEDAD ACTUAL DE LAS PARCELAS ===");

        for (Parcela parcela : gestorGranja.getParcelas()) {

            if (parcela.getSensores().isEmpty()) {
                System.out.println(parcela.getId()
                        + ": SIN SENSOR");
                continue;
            }

            SensorHumedad sensor = parcela.getSensores().get(0);

            if (!sensor.isConectado()) {
                System.out.println(parcela.getId()
                        + ": SENSOR DESCONECTADO");
                continue;
            }

            if (sensor.getLecturas().isEmpty()) {
                System.out.println(parcela.getId()
                        + ": SIN LECTURAS");
                continue;
            }

            LecturaHumedad ultima =
                    sensor.getLecturas()
                            .get(sensor.getLecturas().size() - 1);

            System.out.println(parcela.getId()
                    + ": Humedad: "
                    + ultima.getPorcentajeHumedad() + "%");
        }
    }
    public void mostrarLecturasSensor(Scanner scanner) {

        System.out.println("\n=== HISTORIAL DE LECTURAS DE SENSOR ===");

        if (gestorGranja.getSensoresInventario().isEmpty()) {
            System.out.println(" No hay sensores registrados.");
            return;
        }

        for (SensorHumedad s : gestorGranja.getSensoresInventario()) {
            System.out.println(s.getId()
                    + " - Parcela: "
                    + (s.getParcela() != null ? s.getParcela().getId() : "SIN PARCELA"));
        }

        System.out.print("\nIngrese el ID del sensor: ");
        String id = scanner.next();

        SensorHumedad sensor = null;

        for (SensorHumedad s : gestorGranja.getSensoresInventario()) {
            if (s.getId().equals(id)) {
                sensor = s;
                break;
            }
        }

        if (sensor == null) {
            System.out.println(" Sensor no encontrado.");
            return;
        }

        if (sensor.getLecturas().isEmpty()) {
            System.out.println(" El sensor no tiene lecturas.");
            return;
        }

        System.out.println("\nLecturas del " + sensor.getId() + ":");

        for (LecturaHumedad lectura : sensor.getLecturas()) {
            System.out.println("Fecha: " + lectura.getFecha()
                    + " | Humedad: "
                    + lectura.getPorcentajeHumedad() + "%");
        }
    }
    public void cambiarConexionSensor(Scanner scanner) {

        System.out.println("\n=== CONECTAR / DESCONECTAR SENSOR ===");

        for (SensorHumedad s : gestorGranja.getSensoresInventario()) {
            System.out.println(s.getId()
                    + " | Conectado: " + s.isConectado());
        }

        System.out.print("\nIngrese el ID del sensor: ");
        String id = scanner.next();

        SensorHumedad sensor = null;

        for (SensorHumedad s : gestorGranja.getSensoresInventario()) {
            if (s.getId().equals(id)) {
                sensor = s;
                break;
            }
        }

        if (sensor == null) {
            System.out.println("⚠ Sensor no encontrado.");
            return;
        }

        sensor.setConectado(!sensor.isConectado());

        System.out.println(" Estado cambiado. Ahora está "
                + (sensor.isConectado() ? "CONECTADO" : "DESCONECTADO"));
    }
    public void eliminarSensor(Scanner scanner) {
        System.out.println("\n=== ELIMINAR SENSOR ===");
        for (SensorHumedad s : gestorGranja.getSensoresInventario()) {
            System.out.println(s.getId());
        }
        System.out.print("\nIngrese el ID del sensor a eliminar: ");
        String id = scanner.next();
        SensorHumedad sensor = null;
        for (SensorHumedad s : gestorGranja.getSensoresInventario()) {
            if (s.getId().equals(id)) {
                sensor = s;
                break;
            }
        }
        if (sensor == null) {
            System.out.println(" Sensor no encontrado.");
            return;
        }
        if (sensor.getParcela() != null) {
            sensor.getParcela().getSensores().remove(sensor);
            System.out.println(" "
                    + sensor.getParcela().getId()
                    + " quedó sin sensor.");
        }
        gestorGranja.getSensoresInventario().remove(sensor);
        System.out.println(" Sensor eliminado correctamente.");
    }

}