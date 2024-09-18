/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pspreto0;

/**
 *
 * @author 2dam
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PSPReto0 {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("hijo")) {
            crearVentanaHijo();
        } else if (args.length > 0 && args[0].equals("nieto")) {
            crearVentanaNieto();
        } else {
            // Crear la ventana principal si no hay argumentos
            crearVentanaPadre();
        }
    }

    // Método para crear la ventana principal (padre)
    public static void crearVentanaPadre() {
        JFrame ventanaPadre = new JFrame("Gestor de Procesos del Sistema");
        ventanaPadre.setSize(600, 500);
        ventanaPadre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaPadre.setLayout(new BorderLayout(10, 10));
        ventanaPadre.getContentPane().setBackground(new Color(230, 240, 255)); // Fondo suave

        // Panel para el título
        JLabel titulo = new JLabel("Procesos Activos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(33, 102, 172));
        ventanaPadre.add(titulo, BorderLayout.NORTH);

        // Crear la lista de procesos con estilo
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> lista = new JList<>(model);
        lista.setFont(new Font("Monospaced", Font.PLAIN, 14));
        lista.setBackground(new Color(255, 255, 255));
        lista.setSelectionBackground(new Color(184, 207, 229));
        lista.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(33, 102, 172), 1), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JScrollPane scrollPane = new JScrollPane(lista);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        ventanaPadre.add(scrollPane, BorderLayout.CENTER);

        // Panel para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(new Color(230, 240, 255));

        // Botón para matar el proceso seleccionado
        JButton botonMatar = new JButton("Matar Proceso");
        botonMatar.setBackground(new Color(220, 53, 69));
        botonMatar.setForeground(Color.WHITE);
        botonMatar.setFocusPainted(false);
        botonMatar.setFont(new Font("Arial", Font.BOLD, 14));
        botonMatar.setPreferredSize(new Dimension(160, 40));

        botonMatar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String procesoSeleccionado = lista.getSelectedValue();

                if (procesoSeleccionado != null) {
                    try {
                        // Obtener el PID del proceso seleccionado (el PID está en la primera posición)
                        String[] partes = procesoSeleccionado.split("\\s+");
                        String pid = partes[0];  // El PID está al principio

                        // Ejecutar comando kill en Linux o taskkill en Windows
                        String comandoKill = esWindows() 
                                ? "taskkill /PID " + pid + " /F" 
                                : "kill -9 " + pid;

                        Process proceso = Runtime.getRuntime().exec(comandoKill);
                        proceso.waitFor();

                        JOptionPane.showMessageDialog(ventanaPadre, "Proceso con PID " + pid + " finalizado.");
                        model.removeElement(procesoSeleccionado);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(ventanaPadre, "Error al intentar matar el proceso.");
                    }
                } else {
                    JOptionPane.showMessageDialog(ventanaPadre, "Por favor, selecciona un proceso.");
                }
            }
        });

        // Botón para crear ventana hijo
        JButton botonCrearHijo = new JButton("Añadir Ventana Hijo");
        botonCrearHijo.setBackground(new Color(40, 167, 69));
        botonCrearHijo.setForeground(Color.WHITE);
        botonCrearHijo.setFocusPainted(false);
        botonCrearHijo.setFont(new Font("Arial", Font.BOLD, 14));
        botonCrearHijo.setPreferredSize(new Dimension(160, 40));

        botonCrearHijo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearProceso("hijo");
            }
        });

        // Añadir botones al panel de botones
        panelBotones.add(botonCrearHijo);
        panelBotones.add(botonMatar);

        ventanaPadre.add(panelBotones, BorderLayout.SOUTH);

        // Cargar la lista de procesos del sistema al iniciar la ventana
        cargarProcesos(model);

        ventanaPadre.setVisible(true);
    }

    // Método para crear la ventana hijo
    public static void crearVentanaHijo() {
        JFrame ventanaHijo = new JFrame("Ventana Hijo");
        ventanaHijo.setSize(300, 200);
        ventanaHijo.setLayout(new BorderLayout(10, 10));
        ventanaHijo.getContentPane().setBackground(new Color(240, 255, 230));  // Fondo claro para ventana hijo

        JLabel tituloHijo = new JLabel("Ventana Hijo", SwingConstants.CENTER);
        tituloHijo.setFont(new Font("Arial", Font.BOLD, 20));
        ventanaHijo.add(tituloHijo, BorderLayout.NORTH);

        // Botón para crear ventana nieto
        JButton botonCrearNieto = new JButton("Añadir Ventana Nieto");
        botonCrearNieto.setBackground(new Color(255, 193, 7));
        botonCrearNieto.setForeground(Color.WHITE);
        botonCrearNieto.setFocusPainted(false);
        botonCrearNieto.setFont(new Font("Arial", Font.BOLD, 14));

        botonCrearNieto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearProceso("nieto");
            }
        });

        ventanaHijo.add(botonCrearNieto, BorderLayout.CENTER);
        ventanaHijo.setVisible(true);
    }

    // Método para crear la ventana nieto
    public static void crearVentanaNieto() {
        JFrame ventanaNieto = new JFrame("Ventana Nieto");
        ventanaNieto.setSize(300, 200);
        ventanaNieto.setLayout(new BorderLayout(10, 10));
        ventanaNieto.getContentPane().setBackground(new Color(255, 240, 230));  // Fondo claro para ventana nieto

        JLabel label = new JLabel("Soy la ventana Nieto", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        ventanaNieto.add(label, BorderLayout.CENTER);

        ventanaNieto.setVisible(true);
    }

    // Método para crear un nuevo proceso
    public static void crearProceso(String tipo) {
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "java", "-cp", System.getProperty("java.class.path"), PSPReto0.class.getName(), tipo);
            builder.inheritIO();  // Heredar la entrada/salida del proceso padre
            builder.start();  // Iniciar el nuevo proceso
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al crear proceso " + tipo);
        }
    }

    // Método para determinar si el sistema operativo es Windows
    private static boolean esWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    // Método para cargar la lista de procesos del sistema
    private static void cargarProcesos(DefaultListModel<String> model) {
        try {
            String comandoListar = esWindows() 
                    ? "tasklist" 
                    : "ps -e";

            Process proceso = Runtime.getRuntime().exec(comandoListar);
            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String linea;
            List<String> listaProcesos = new ArrayList<>();

            while ((linea = reader.readLine()) != null) {
                listaProcesos.add(linea);
            }

            // Agregar los procesos al modelo de la lista
            for (String procesoStr : listaProcesos) {
                model.addElement(procesoStr);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar la lista de procesos.");
        }
    }
}
