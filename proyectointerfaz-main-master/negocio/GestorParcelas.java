package granjaautomatizada.negocio;

import granjaautomatizada.modelo.Parcela;
import granjaautomatizada.utilitario.GranjaException;
import granjaautomatizada.utilitario.Util;

import java.util.Scanner;

public class GestorParcelas {

    private GestorGranja gestorGranja;
    private int contadorParcelas = 0;

    public GestorParcelas(GestorGranja gestorGranja) {
        this.gestorGranja = gestorGranja;
    }

    public void crearParcelasDesdeTerreno(Scanner scanner) {
        double terrenoTotal = Util.leerEnteroPositivo(scanner, "Ingrese la cantidad total de terreno en m²: ");
        double terrenoRestante = terrenoTotal;
        while (terrenoRestante > 0) {
            double tamanioParcela;

            if (terrenoRestante >= 50) {
                tamanioParcela = 50;
            } else {
                tamanioParcela = terrenoRestante;
            }

            contadorParcelas++;
            String idParcela = Util.generarId("PARCELA", contadorParcelas);
            Parcela parcela = new Parcela(idParcela, tamanioParcela);
            gestorGranja.getParcelas().add(parcela);
            System.out.println("Parcela creada: " + idParcela +
                    " | Tamaño: " + tamanioParcela + " m²");
            terrenoRestante -= tamanioParcela;
        }
        System.out.println("\nTotal de parcelas creadas: "
                + gestorGranja.getParcelas().size());
        asignarAspersoresYSensores(scanner);
        gestorGranja.getGestorCultivos().registrarCultivosEnParcelas(scanner);

    }


    private void asignarAspersoresYSensores(Scanner scanner) {
        int cantidadAspersores;
        do {
            cantidadAspersores=Util.leerEntero(scanner, "\nIngrese la cantidad de aspersores disponibles (Positivo)");
            if (cantidadAspersores < 0) System.out.println(" Error: No puedes ingresar números negativos.");
        } while (cantidadAspersores < 0);
        int cantidadSensores;
        do {
            cantidadSensores = Util.leerEntero(scanner, "Ingrese la cantidad de sensores disponibles (Positivo)");
            if (cantidadSensores < 0) System.out.println(" Error: No puedes ingresar números negativos.");
        } while (cantidadSensores < 0);
        for (int i = 0; i < cantidadAspersores; i++) {
            int siguienteNum = gestorGranja.getSiguienteIdAspersor();
            String id = Util.generarId("ASPERSOR", siguienteNum);
            gestorGranja.getAspersoresInventario().add(
                    new granjaautomatizada.modelo.Aspersor(id)
            );
        }

        for (int i = 0; i < cantidadSensores; i++) {
            int siguienteNum = gestorGranja.getSiguienteIdSensor();
            String id = Util.generarId("SENSOR", siguienteNum);
            gestorGranja.getSensoresInventario().add(
                    new granjaautomatizada.modelo.SensorHumedad(id)
            );
        }

        int totalParcelas = gestorGranja.getParcelas().size();

        if (cantidadAspersores < totalParcelas) {
            System.out.println("\nADVERTENCIA: Tienes " + totalParcelas + " parcelas pero solo "
                    + cantidadAspersores + " aspersores.");
            System.out.println("Las últimas " + (totalParcelas - cantidadAspersores) + " parcelas se quedarán sin aspersor.");
        }

        if (cantidadSensores < totalParcelas) {
            System.out.println("\nADVERTENCIA: Tienes " + totalParcelas + " parcelas pero solo "
                    + cantidadSensores + " sensores.");
            System.out.println("Las últimas " + (totalParcelas - cantidadSensores) + " parcelas se quedarán sin sensor.");
        }

        System.out.println("\nAsignando aspersores y sensores a parcelas...\n");

        int indiceAspersor = 0;
        int indiceSensor = 0;

        for (granjaautomatizada.modelo.Parcela parcela : gestorGranja.getParcelas()) {
            if (indiceAspersor < gestorGranja.getAspersoresInventario().size()) {
                granjaautomatizada.modelo.Aspersor aspersor =
                        gestorGranja.getAspersoresInventario().get(indiceAspersor);
                aspersor.setParcela(parcela);
                parcela.getAspersores().add(aspersor);
                indiceAspersor++;
            } else {
                System.out.println("La " + parcela.getId()
                        + " NO tiene aspersor asignado.");
            }
            if (indiceSensor < gestorGranja.getSensoresInventario().size()) {
                granjaautomatizada.modelo.SensorHumedad sensor =
                        gestorGranja.getSensoresInventario().get(indiceSensor);
                sensor.setParcela(parcela);
                parcela.getSensores().add(sensor);
                indiceSensor++;
            } else {
                System.out.println("La " + parcela.getId()
                        + " NO tiene sensor asignado.");
            }
        }

        System.out.println("\nAsignación finalizada.");
    }
    public void mostrarInfoParcelas() {

        System.out.println("\n----- INFORMACIÓN DE PARCELAS -----");
        System.out.println("Cantidad de parcelas: " + gestorGranja.getParcelas().size());

        for (Parcela parcela : gestorGranja.getParcelas()) {

            System.out.println("\n" + parcela.getId() + ":");
            System.out.println("  Cantidad de terreno: " + parcela.getMetrosCuadrados() + " m²");

            if (parcela.getCultivo() != null) {
                System.out.println("  Cultivo: " + parcela.getCultivo().getNombre());
                System.out.println("  Humedad requerida: "
                        + parcela.getCultivo().getHumedadMinima() + "% - "
                        + parcela.getCultivo().getHumedadMaxima() + "%");
            } else {
                System.out.println("  Cultivo: SIN CULTIVO");
            }

            System.out.println("  Número de aspersores: " + parcela.getAspersores().size());
            System.out.println("  Número de sensores: " + parcela.getSensores().size());
        }
    }
    public void eliminarParcela(Scanner scanner) throws GranjaException {

        if (gestorGranja.getParcelas().isEmpty()) {
            throw new GranjaException("No hay parcelas registradas en el sistema.");
        }

        System.out.println("\n=== PARCELAS DISPONIBLES ===");
        for (Parcela parcela : gestorGranja.getParcelas()) {
            System.out.println(parcela.getId());
        }

        System.out.print("\nIngrese el ID de la parcela a eliminar: ");
        String id = scanner.next();

        Parcela parcelaEliminar = null;

        for (Parcela p : gestorGranja.getParcelas()) {
            if (p.getId().equals(id)) {
                parcelaEliminar = p;
                break;
            }
        }

        if (parcelaEliminar == null) {
            throw new GranjaException("La parcela con ID '" + id + "' no existe.");
        }

        System.out.print("Ingrese nuevamente el ID para confirmar: ");
        String confirmacion = scanner.next();

        if (!confirmacion.equalsIgnoreCase(id)) {
            throw new GranjaException("Confirmación incorrecta. Se canceló la eliminación.");
        }
        for (granjaautomatizada.modelo.Aspersor a : parcelaEliminar.getAspersores()) {
            a.setParcela(null);
            a.apagar();
        }
        for (granjaautomatizada.modelo.SensorHumedad s : parcelaEliminar.getSensores()) {
            s.setParcela(null);
        }

        gestorGranja.getParcelas().remove(parcelaEliminar);

        System.out.println(" Parcela " + id + " eliminada correctamente.");
    }

}