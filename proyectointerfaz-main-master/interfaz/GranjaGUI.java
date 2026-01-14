package granjaautomatizada.interfaz;

import granjaautomatizada.negocio.*;
import granjaautomatizada.modelo.*;
import granjaautomatizada.utilitario.GranjaException;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class GranjaGUI extends JFrame {
    private GestorGranja gestor;
    private JTextArea txtSalida;

    public GranjaGUI() {
        gestor = new GestorGranja();
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("SISTEMA DE GESTIÓN DE GRANJA AUTOMATIZADA");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        // PANEL DE BOTONES (Organizado por grupos)
        JPanel panelIzquierdo = new JPanel(new GridLayout(11, 1, 5, 5));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Gestión de Parcelas"));
        panelIzquierdo.add(crearBoton("Crear Terreno y Parcelas", 1));
        panelIzquierdo.add(crearBoton("Info de Parcelas", 2));
        panelIzquierdo.add(crearBoton("Lista de Cultivos", 9));
        panelIzquierdo.add(crearBoton("Eliminar una Parcela", 10));
        panelIzquierdo.add(crearBoton("Cambiar Cultivo", 11));
        panelIzquierdo.add(crearBoton("Evaluar Riego Auto", 12));

        JPanel panelDerecho = new JPanel(new GridLayout(11, 1, 5, 5));
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Inventario y Dispositivos"));
        panelDerecho.add(crearBoton("Cargar Aspersores Inv.", 7));
        panelDerecho.add(crearBoton("Cargar Sensores Inv.", 8));
        panelDerecho.add(crearBoton("Asignar Aspersor a Parcela", 5));
        panelDerecho.add(crearBoton("Asignar Sensor a Parcela", 6));
        panelDerecho.add(crearBoton("Info de Aspersores", 3));
        panelDerecho.add(crearBoton("Info de Sensores", 4));

        JPanel panelCentroAbajo = new JPanel(new GridLayout(2, 4, 5, 5));
        panelCentroAbajo.setBorder(BorderFactory.createTitledBorder("Control de Dispositivos"));
        panelCentroAbajo.add(crearBoton("Historial Sensor", 13));
        panelCentroAbajo.add(crearBoton("Historial Aspersor", 14));
        panelCentroAbajo.add(crearBoton("Riego Manual", 15));
        panelCentroAbajo.add(crearBoton("On/Off Aspersor", 16));
        panelCentroAbajo.add(crearBoton("On/Off Sensor", 17));
        panelCentroAbajo.add(crearBoton("Eliminar Aspersor", 18));
        panelCentroAbajo.add(crearBoton("Eliminar Sensor", 19));

        JButton btn20 = new JButton("SALIR");
        btn20.setBackground(new Color(200, 50, 50));
        btn20.setForeground(Color.WHITE);
        btn20.addActionListener(e -> System.exit(0));
        panelCentroAbajo.add(btn20);

        // AREA DE SALIDA
        txtSalida = new JTextArea();
        txtSalida.setEditable(false);
        txtSalida.setBackground(new Color(40, 44, 52));
        txtSalida.setForeground(new Color(171, 178, 191));
        txtSalida.setFont(new Font("Monospaced", Font.BOLD, 13));
        JScrollPane scroll = new JScrollPane(txtSalida);

        // Agregando Paneles
        JPanel panelSuperior = new JPanel(new GridLayout(1, 2));
        panelSuperior.add(panelIzquierdo);
        panelSuperior.add(panelDerecho);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelCentroAbajo, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, int opcion) {
        JButton btn = new JButton(texto);
        btn.addActionListener(e -> ejecutarOpcion(opcion));
        return btn;
    }

    private void ejecutarOpcion(int opcion) {
        String id;
        try {
            switch (opcion) {
                case 1:
                    try {
                        boolean modoAgregar = false;
                        if (!gestor.getParcelas().isEmpty()) {
                            Object[] options = {"Agregar más terreno", "Borrar todo y reiniciar", "Cancelar"};
                            int choice = JOptionPane.showOptionDialog(this,
                                    "Ya existen parcelas en la granja.\n¿Qué desea hacer?",
                                    "Gestión de Terreno",
                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options,
                                    options[0]);

                            if (choice == 2 || choice == JOptionPane.CLOSED_OPTION) break; // Cancelar
                            if (choice == 1) { // Borrar todo
                                gestor.getParcelas().clear();
                                modoAgregar = false;
                            } else { // Agregar
                                modoAgregar = true;
                            }
                        }

                        String mensajeTerreno = modoAgregar ? "Ingrese la cantidad de terreno EXTRA a añadir (m²):" : "Ingrese el tamaño TOTAL del terreno (m²):";
                        double terrenoInput = 0;
                        while (terrenoInput <= 0) {
                            String tStr = JOptionPane.showInputDialog(mensajeTerreno);
                            if (tStr == null) break;
                            try {
                                terrenoInput = Double.parseDouble(tStr);
                                if (terrenoInput <= 0) JOptionPane.showMessageDialog(this, "El número debe ser mayor a 0.");
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(this, "Ingrese un número válido.");
                            }
                        }
                        if (terrenoInput <= 0) break;

                        double terrenoRestante = terrenoInput;

                        int contador = modoAgregar ? gestor.getParcelas().size() : 0;
                        int indiceInicioNuevas = gestor.getParcelas().size(); // Para saber cuáles son las nuevas

                        while (terrenoRestante > 0) {
                            double tamanio = (terrenoRestante >= 50) ? 50 : terrenoRestante;
                            contador++;
                            gestor.getParcelas().add(new Parcela("PARCELA_" + contador, tamanio));
                            terrenoRestante -= tamanio;
                        }

                        txtSalida.append("\n[SISTEMA] Se añadieron " + (gestor.getParcelas().size() - indiceInicioNuevas) + " parcelas nuevas.\n");
                        txtSalida.append("Total actual de parcelas: " + gestor.getParcelas().size() + "\n");

                        int respuestaStock = JOptionPane.showConfirmDialog(null, "¿Desea agregar más aspersores/sensores al inventario?", "Inventario", JOptionPane.YES_NO_OPTION);
                        if (respuestaStock == JOptionPane.YES_OPTION) {
                            String sAsp = JOptionPane.showInputDialog("Cantidad de aspersores a comprar:");
                            if(sAsp!=null && !sAsp.isEmpty()) gestor.getGestorAspersores().agregarAspersoresAlInventario(Integer.parseInt(sAsp));

                            String sSen = JOptionPane.showInputDialog("Cantidad de sensores a comprar:");
                            if(sSen!=null && !sSen.isEmpty()) gestor.getGestorSensores().agregarSensoresAlInventario(Integer.parseInt(sSen));
                        }

                        for (Parcela p : gestor.getParcelas()) {
                            // Solo si le falta aspersor
                            if (p.getAspersores().isEmpty()) {
                                for (Aspersor a : gestor.getAspersoresInventario()) {
                                    if (a.getParcela() == null) {
                                        a.setParcela(p); p.getAspersores().add(a); break;
                                    }
                                }
                            }
                            if (p.getSensores().isEmpty()) {
                                for (SensorHumedad s : gestor.getSensoresInventario()) {
                                    if (s.getParcela() == null) {
                                        s.setParcela(p); p.getSensores().add(s); break;
                                    }
                                }
                            }
                        }
                        txtSalida.append("[SISTEMA] Dispositivos asignados automáticamente según stock disponible.\n");

                        int respuestaCultivo = JOptionPane.showConfirmDialog(null, "¿Desea registrar cultivos para las NUEVAS parcelas?", "Cultivos", JOptionPane.YES_NO_OPTION);

                        if (respuestaCultivo == JOptionPane.YES_OPTION) {
                            for (int i = indiceInicioNuevas; i < gestor.getParcelas().size(); i++) {
                                Parcela p = gestor.getParcelas().get(i);
                                String nom = "";


                                while (true) {
                                    nom = JOptionPane.showInputDialog("Nombre del cultivo para " + p.getId() + ":\n(Escriba 'SALTAR' o Cancele para dejarla vacía)");
                                    if (nom == null) { nom = "SALTAR"; break; }
                                    if (!nom.trim().isEmpty()) break;
                                }

                                if (nom.equalsIgnoreCase("SALTAR")) {
                                    txtSalida.append(" - " + p.getId() + " sin cultivo.\n");
                                    continue;
                                }
                                int min = pedirNumero("Humedad Mínima para " + nom + " (0-100):", 0, 100);
                                if (min == -1) continue; // Si cancela, pasa a la siguiente parcela

                                int max = pedirNumero("Humedad Máxima (" + (min+1) + "-100):", min + 1, 100);
                                if (max == -1) continue;

                                int freq = pedirNumero("Frecuencia de Riego (Horas):", 1, 500);
                                if (freq == -1) continue;

                                p.setCultivo(new Cultivo(nom, min, max, freq));
                                txtSalida.append(" - Cultivo " + nom + " registrado en " + p.getId() + ".\n");
                            }
                        } else {
                            txtSalida.append("[INFO] Se omitió el registro de cultivos para las parcelas nuevas.\n");
                        }

                        txtSalida.append("[ÉXITO] Proceso finalizado.\n");

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                    break;
                case 2:
                    txtSalida.append("\n=== INFORMACIÓN DETALLADA DE PARCELAS ===\n");

                    if (gestor.getParcelas().isEmpty()) {
                        txtSalida.append("No hay parcelas registradas en el sistema.\n");
                    } else {
                        for (Parcela p : gestor.getParcelas()) {
                            txtSalida.append("\n--------------------------------------------\n");
                            txtSalida.append("PARCELA: " + p.getId() + "   (Área: " + p.getMetrosCuadrados() + "m²)\n");

                            // 1. Info del Cultivo
                            if (p.getCultivo() != null) {
                                txtSalida.append("CULTIVO: " + p.getCultivo().getNombre().toUpperCase()
                                        + " (Riego cada " + p.getCultivo().getFrecuenciaRiegoHoras() + "h)\n");
                            } else {
                                txtSalida.append("CULTIVO: [SIN CULTIVO]\n");
                            }

                            txtSalida.append("ASPERSORES INSTALADOS:\n");
                            if (p.getAspersores().isEmpty()) {
                                txtSalida.append("   (Ninguno)\n");
                            } else {
                                for (Aspersor a : p.getAspersores()) {
                                    String estado = a.isEncendido() ? "[ON]" : "[OFF]";
                                    String conexion = a.isConectado() ? "Conectado" : "Desconectado";
                                    txtSalida.append("   -> " + a.getId() + "  " + estado + "  (" + conexion + ")\n");
                                }
                            }

                            txtSalida.append("SENSORES INSTALADOS:\n");
                            if (p.getSensores().isEmpty()) {
                                txtSalida.append("   (Ninguno)\n");
                            } else {
                                for (SensorHumedad s : p.getSensores()) {
                                    String conexion = s.isConectado() ? "Conectado" : "Desconectado";
                                    String lectura = "Sin datos";
                                    if (!s.getLecturas().isEmpty()) {
                                        lectura = s.getLecturas().get(s.getLecturas().size()-1).getPorcentajeHumedad() + "%";
                                    }
                                    txtSalida.append("   -> " + s.getId() + "  (" + conexion + ")  Ult. Humedad: " + lectura + "\n");
                                }
                            }
                        }
                    }
                    txtSalida.append("--------------------------------------------\n");
                    break;
                case 3:
                    txtSalida.append("\n--- INFO ASPERSORES ---\n");
                    for (Aspersor a : gestor.getAspersoresInventario()) {
                        txtSalida.append(a.getId() + " | Parcela: " + (a.getParcela() != null ? a.getParcela().getId() : "INV") + " | Conectado: " + a.isConectado() + " | Encendido: " + a.isEncendido() + "\n");
                    }
                    break;
                case 4:
                    txtSalida.append("\n--- INFO SENSORES ---\n");
                    for (SensorHumedad s : gestor.getSensoresInventario()) {
                        txtSalida.append(s.getId() + " | Parcela: " + (s.getParcela() != null ? s.getParcela().getId() : "INV") + " | Conectado: " + s.isConectado() + "\n");
                    }
                    break;
                case 5:
                    id = JOptionPane.showInputDialog("Ingrese ID Parcela:");
                    if(id != null) {
                        gestor.getGestorAspersores().asignarAspersorAParcela(id);
                        txtSalida.append("\n[ÉXITO] Se intentó asignar un aspersor a la parcela " + id + ".\n(Verifique en Info Parcelas)\n");
                        JOptionPane.showMessageDialog(this, "Operación realizada. Verifique el área de texto.");
                    }
                    break;
                case 6:
                    id = JOptionPane.showInputDialog("Ingrese ID de la Parcela destino:");
                    if (id != null) {
                        gestor.getGestorSensores().asignarSensorAParcela(id);

                        txtSalida.append("\n[ÉXITO] Sensor asignado correctamente a la parcela " + id + ".\n");
                        JOptionPane.showMessageDialog(this, "¡Sensor asignado con éxito!");
                    }
                    break;
                case 7:
                    int cantA = pedirNumero("¿Cuántos Aspersores desea agregar al inventario?", 1, 1000);
                    if (cantA != -1) { // Si no canceló
                        gestor.getGestorAspersores().agregarAspersoresAlInventario(cantA);
                        txtSalida.append("\n[ÉXITO] Se agregaron " + cantA + " aspersores al inventario.\n");
                    }
                    break;
                case 8:
                    int cantS = pedirNumero("¿Cuántos Sensores desea agregar al inventario?", 1, 1000);
                    if (cantS != -1) {
                        gestor.getGestorSensores().agregarSensoresAlInventario(cantS);
                        txtSalida.append("\n[ÉXITO] Se agregaron " + cantS + " sensores al inventario.\n");
                    }
                    break;
                case 9:
                    txtSalida.append("\n--- CULTIVOS REGISTRADOS ---\n");
                    for(Parcela p : gestor.getParcelas()){
                        if(p.getCultivo() != null) txtSalida.append("Cultivo: " + p.getCultivo().getNombre() + " en " + p.getId() + "\n");
                    }
                    break;
                case 10:
                    id = JOptionPane.showInputDialog("ID Parcela a ELIMINAR:");
                    if(id != null) {
                        gestor.getGestorParcelas().eliminarParcela(new java.util.Scanner(id + "\n" + id));
                        txtSalida.append("\nParcela " + id + " eliminada.\n");
                    }
                    break;
                case 11:
                    txtSalida.append("\n--- GESTIÓN DE CULTIVOS ---\n");

                    if (gestor.getParcelas().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Primero debe crear parcelas (Opción 1).");
                        break;
                    }

                    for (Parcela p : gestor.getParcelas()) {
                        String infoCultivo = (p.getCultivo() != null) ? p.getCultivo().getNombre() : "[VACÍA - DISPONIBLE]";
                        txtSalida.append("ID: " + p.getId() + " | Cultivo Actual: " + infoCultivo + "\n");
                    }

                    String idBuscado = JOptionPane.showInputDialog("Ingrese el ID de la parcela para Asignar o Editar su cultivo:");
                    if (idBuscado == null) break;

                    Parcela parcelaSeleccionada = null;
                    for (Parcela p : gestor.getParcelas()) {
                        if (p.getId().equalsIgnoreCase(idBuscado)) {
                            parcelaSeleccionada = p;
                            break;
                        }
                    }

                    if (parcelaSeleccionada == null) {
                        JOptionPane.showMessageDialog(this, "La parcela '" + idBuscado + "' no existe.");
                        break;
                    }

                    String accion = (parcelaSeleccionada.getCultivo() == null) ? "REGISTRAR NUEVO" : "EDITAR";
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "La parcela " + parcelaSeleccionada.getId() + " tiene: " +
                                    ((parcelaSeleccionada.getCultivo() == null) ? "SIN CULTIVO" : parcelaSeleccionada.getCultivo().getNombre()) +
                                    "\n¿Desea " + accion + " un cultivo aquí?",
                            "Gestión de Cultivo", JOptionPane.YES_NO_OPTION);

                    if (confirm != JOptionPane.YES_OPTION) break;

                    String nuevoNombre = JOptionPane.showInputDialog("Nombre del cultivo (ej: Tomate, Maíz):");
                    if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Operación cancelada o nombre inválido.");
                        break;
                    }

                    int nuevoMin = pedirNumero("Humedad Mínima para " + nuevoNombre + " (0-100%):", 0, 100);
                    if (nuevoMin == -1) break;

                    int nuevoMax = pedirNumero("Humedad Máxima (" + (nuevoMin + 1) + "-100%):", nuevoMin + 1, 100);
                    if (nuevoMax == -1) break;

                    int nuevaFreq = pedirNumero("Frecuencia de Riego (horas):", 1, 500);
                    if (nuevaFreq == -1) break;

                    Cultivo nuevoCultivo = new Cultivo(nuevoNombre, nuevoMin, nuevoMax, nuevaFreq);
                    parcelaSeleccionada.setCultivo(nuevoCultivo);

                    txtSalida.append("\n[ÉXITO] Se ha configurado el cultivo '" + nuevoNombre + "' en la parcela " + parcelaSeleccionada.getId() + ".\n");
                    JOptionPane.showMessageDialog(this, "¡Cultivo guardado correctamente!");
                    break;
                case 12: // Evaluar Riego Automático y mostrar en GUI
                    txtSalida.append("\n--- EJECUTANDO EVALUACIÓN DE RIEGO ---\n");

                    // 1. Llamamos al método original para que cambie los estados (on/off) internamente
                    gestor.getGestorAspersores().evaluarYRiegoAutomatico();

                    // 2. Ahora recorremos las parcelas para mostrar los resultados en la INTERFAZ
                    if (gestor.getParcelas().isEmpty()) {
                        txtSalida.append("No hay parcelas para evaluar.\n");
                    } else {
                        for (Parcela p : gestor.getParcelas()) {
                            txtSalida.append("\nParcela: " + p.getId());

                            // Verificar cultivo
                            if (p.getCultivo() == null) {
                                txtSalida.append(" -> Sin cultivo registrado.\n");
                                continue;
                            }

                            // Verificar sensor y humedad
                            if (p.getSensores().isEmpty()) {
                                txtSalida.append(" -> ERROR: No tiene sensor.\n");
                            } else {
                                SensorHumedad sensor = p.getSensores().get(0);
                                LecturaHumedad lectura = sensor.leerHumedad();

                                if (lectura == null) {
                                    txtSalida.append(" -> ERROR: Sensor desconectado.\n");
                                } else {
                                    int hum = lectura.getPorcentajeHumedad();
                                    int min = p.getCultivo().getHumedadMinima();
                                    txtSalida.append("\n  - Humedad actual: " + hum + "% (Mínima: " + min + "%)");

                                    // Mostrar estado del riego
                                    if (hum < min) {
                                        txtSalida.append("\n  - ESTADO: [RIEGO ACTIVADO] Falta humedad.\n");
                                    } else {
                                        txtSalida.append("\n  - ESTADO: [RIEGO APAGADO] Humedad correcta.\n");
                                    }
                                }
                            }
                        }
                    }
                    txtSalida.append("--------------------------------------\n");
                    break;
                case 13:
                    id = JOptionPane.showInputDialog("ID del Sensor para ver lecturas:");
                    if(id != null) {
                        for(SensorHumedad s : gestor.getSensoresInventario()) {
                            if(s.getId().equals(id)) {
                                txtSalida.append("\nLecturas de " + id + ":\n");
                                for(LecturaHumedad l : s.getLecturas()) txtSalida.append(l.getFecha() + " | " + l.getPorcentajeHumedad() + "%\n");
                            }
                        }
                    }
                    break;
                case 14:
                    id = JOptionPane.showInputDialog("ID del Aspersor para ver historial:");
                    if(id != null) {
                        for(Aspersor a : gestor.getAspersoresInventario()) {
                            if(a.getId().equals(id)) {
                                txtSalida.append("\nHistorial " + id + ":\n");
                                for(LocalDateTime f : a.getHistorialEncendidos()) txtSalida.append("Prendido el: " + f + "\n");
                            }
                        }
                    }
                    break;
                case 15: // Encender Aspersor Manual
                    id = JOptionPane.showInputDialog("Ingrese ID Aspersor a prender MANUAL:");
                    if (id != null) {
                        boolean encontrado = false;

                        for (Aspersor a : gestor.getAspersoresInventario()) {
                            if (a.getId().equalsIgnoreCase(id)) { // equalsIgnoreCase para evitar errores de mayúsculas
                                encontrado = true;

                                if (a.isConectado()) {
                                    a.encender();
                                    txtSalida.append("\n[ÉXITO] Aspersor " + a.getId() + " encendido manualmente.\n");
                                    JOptionPane.showMessageDialog(this, "¡Aspersor encendido!");
                                } else {
                                    txtSalida.append("\n[ERROR] El aspersor " + a.getId() + " está DESCONECTADO y no puede prenderse.\n");
                                    JOptionPane.showMessageDialog(this, "Error: El aspersor está desconectado. (Use opción 16 para conectar)");
                                }
                                break;
                            }
                        }
                        if (!encontrado) {
                            JOptionPane.showMessageDialog(this, "Error: No existe ningún aspersor con el ID: " + id);
                        }
                    }
                    break;
                case 16:
                    id = JOptionPane.showInputDialog("ID del Aspersor para cambiar conexión:");
                    if(id != null) {
                        for(Aspersor a : gestor.getAspersoresInventario()) {
                            if(a.getId().equals(id)) {
                                a.setConectado(!a.isConectado());
                                txtSalida.append("\nAspersor " + id + " ahora está " + (a.isConectado()?"CONECTADO":"DESCONECTADO") + "\n");
                            }
                        }
                    }
                    break;
                case 17:
                    id = JOptionPane.showInputDialog("ID del Sensor para cambiar conexión:");
                    if(id != null) {
                        for(SensorHumedad s : gestor.getSensoresInventario()) {
                            if(s.getId().equals(id)) {
                                s.setConectado(!s.isConectado());
                                txtSalida.append("\nSensor " + id + " ahora está " + (s.isConectado()?"CONECTADO":"DESCONECTADO") + "\n");
                            }
                        }
                    }
                    break;
                case 18:
                    id = JOptionPane.showInputDialog("ID del Aspersor a ELIMINAR:");
                    if(id != null) {
                        gestor.getGestorAspersores().eliminarAspersor(new java.util.Scanner(id));
                        txtSalida.append("\nAspersor " + id + " eliminado del sistema.\n");
                    }
                    break;
                case 19:
                    id = JOptionPane.showInputDialog("ID del Sensor a ELIMINAR:");
                    if(id != null) {
                        gestor.getGestorSensores().eliminarSensor(new java.util.Scanner(id));
                        txtSalida.append("\nSensor " + id + " eliminado del sistema.\n");
                    }
                    break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GranjaGUI().setVisible(true));
    }

    private int pedirNumero(String mensaje, int min, int max) {
        while (true) {
            String input = JOptionPane.showInputDialog(this, mensaje);
            if (input == null) return -1; // El usuario dio clic en Cancelar

            try {
                int valor = Integer.parseInt(input);
                if (valor < min || valor > max) {
                    JOptionPane.showMessageDialog(this, "⚠ Error: El número debe estar entre " + min + " y " + max);
                } else {
                    return valor; // Número válido, lo devolvemos
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "⚠ Error: Debe ingresar un número entero válido.");
            }
        }
    }
}