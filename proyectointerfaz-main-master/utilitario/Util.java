package granjaautomatizada.utilitario;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.*;

public class Util {

    private static Random random = new Random();

    public static String generarId(String prefijo, int numero) {
        return prefijo + "_" + numero;
    }

    public static int generarHumedadProgresiva(int humedadActual) {
        int variacion = random.nextInt(11) - 5; // -5 a +5
        int nuevaHumedad = humedadActual + variacion;

        if (nuevaHumedad < 0) nuevaHumedad = 0;
        if (nuevaHumedad > 100) nuevaHumedad = 100;

        return nuevaHumedad;
    }

    public static LocalDateTime obtenerFechaHoraActual() {
        return LocalDateTime.now();
    }

    public static void mostrarMenuPrincipal() {
        System.out.println("\n=== SISTEMA DE AUTOMATIZACIÓN DE GRANJA ===");
        System.out.println("1. Ingresar terreno y crear parcelas");
        System.out.println("2. Mostrar información de parcelas");
        System.out.println("3. Mostrar información de aspersores");
        System.out.println("4. Mostrar información de sensores");
        System.out.println("5. Añadir aspersor a una parcela");
        System.out.println("6. Añadir sensor a una parcela");
        System.out.println("7. Agregar aspersores al inventario");
        System.out.println("8. Agregar sensores al inventario");
        System.out.println("9. Mostrar lista de cultivos disponibles");
        System.out.println("10. Eliminar una parcela");
        System.out.println("11. Cambiar cultivo de una parcela");
        System.out.println("12. Verificar humedad de las parcelas");
        System.out.println("13. Mostrar lecturas de un sensor");
        System.out.println("14. Mostrar cuándo se prendió un aspersor");
        System.out.println("15. Prender manualmente un aspersor");
        System.out.println("16. Conectar o desconectar un aspersor");
        System.out.println("17. Conectar o desconectar un sensor");
        System.out.println("18. Eliminar un aspersor del programa");
        System.out.println("19. Eliminar un sensor del programa");
        System.out.println("20. Salir");
        System.out.print("Seleccione una opción: ");
    }

    public static int leerEntero(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.println("Error: Ingrese un número entero válido.");
            scanner.next();
            System.out.print(mensaje);
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    public static int leerEnteroPositivo(Scanner scanner, String mensaje) {
        int numero = 0;
        boolean datoValido = false;

        do {
            try {
                numero = leerEntero(scanner, mensaje);

                if (numero <= 0) {
                    throw new GranjaException("El valor debe ser mayor a 0.");
                }
                datoValido = true;

            } catch (GranjaException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Intente nuevamente.");
            }
        } while (!datoValido);

        return numero;
    }
}