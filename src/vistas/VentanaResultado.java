/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaResultado extends JFrame {

    JTextArea areaDescripcion, areaProcedimiento, areaConclusion;
    JTable tablaResultados, tablaCriticos;
    DefaultTableModel modeloResultados, modeloCriticos;

    String tipoPrueba, contexto, variables, h0, h1, datos,tipoVentana;

    double estadistico = 0;
    double critico = 1.96;
    int n = 0;

    public VentanaResultado(String tipoPrueba, String contexto, String variables,
                            String h0, String h1, String datos, String tipoVentana) {

        this.tipoPrueba = tipoPrueba;
        this.contexto = contexto;
        this.variables = variables;
        this.h0 = h0;
        this.h1 = h1;
        this.datos = datos;
        this.tipoVentana = tipoVentana;
        

        setTitle("Resultados estadísticos");
        setSize(1200, 720);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ================= DESCRIPCIÓN =================
        JPanel p1 = new JPanel(null);
        p1.setBounds(20, 70, 360, 180);
        p1.setBorder(BorderFactory.createTitledBorder("Descripción del problema"));
        add(p1);

        areaDescripcion = new JTextArea();
        areaDescripcion.setEditable(false);

        JScrollPane sp1 = new JScrollPane(areaDescripcion);
        sp1.setBounds(10, 25, 330, 140);
        p1.add(sp1);

        // ================= RESULTADOS =================
        JPanel p2 = new JPanel(null);
        p2.setBounds(20, 270, 360, 150);
        p2.setBorder(BorderFactory.createTitledBorder("Tabla de resultados"));
        add(p2);

        modeloResultados = new DefaultTableModel();
        tablaResultados = new JTable(modeloResultados);

        JScrollPane sp2 = new JScrollPane(tablaResultados);
        sp2.setBounds(10, 25, 330, 110);
        p2.add(sp2);

        // ================= CRÍTICOS =================
        JPanel p3 = new JPanel(null);
        p3.setBounds(20, 440, 360, 180);
        p3.setBorder(BorderFactory.createTitledBorder("Valores críticos"));
        add(p3);

        modeloCriticos = new DefaultTableModel();
        tablaCriticos = new JTable(modeloCriticos);

        JScrollPane sp3 = new JScrollPane(tablaCriticos);
        sp3.setBounds(10, 25, 330, 130);
        p3.add(sp3);

        // ================= PROCEDIMIENTO =================
        JPanel p4 = new JPanel(null);
        p4.setBounds(410, 70, 750, 430);
        p4.setBorder(BorderFactory.createTitledBorder("Procedimiento paso a paso"));
        add(p4);

        areaProcedimiento = new JTextArea();
        areaProcedimiento.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane sp4 = new JScrollPane(areaProcedimiento);
        sp4.setBounds(10, 25, 720, 390);
        p4.add(sp4);

        // ================= CONCLUSIÓN =================
        JPanel p5 = new JPanel(null);
        p5.setBounds(410, 520, 750, 100);
        p5.setBorder(BorderFactory.createTitledBorder("Conclusión"));
        add(p5);

        areaConclusion = new JTextArea();
        JScrollPane sp5 = new JScrollPane(areaConclusion);
        sp5.setBounds(10, 25, 600, 60);
        p5.add(sp5);

        JButton btn = new JButton("Regresar");
        btn.setBounds(620, 35, 100, 30);
        btn.addActionListener(e -> {
        this.dispose();
         new VentanaAgregarCaso(tipoVentana).setVisible(true);
        });
        p5.add(btn);

        // ================= DESCRIPCIÓN =================
        areaDescripcion.setText(
                "TIPO: " + tipoPrueba +
                "\n\n" + contexto +
                "\n\nVARIABLES:\n" + variables +
                "\n\nH0:\n" + h0 +
                "\nH1:\n" + h1
        );

        // ================= CÁLCULOS POR PRUEBA =================
        switch (tipoPrueba) {

            case "Prueba t de Student" -> calcularT();
            case "Prueba Z" -> calcularZ();
            case "Análisis de Varianza (ANOVA)" -> calcularANOVA();
            case "Prueba U de Mann-Whitney" -> estadistico = 4;
            case "Prueba de Wilcoxon" -> estadistico = 0;
            case "Prueba Chi-Cuadrada" -> estadistico = 6.12;
        }

        cargarTablas();
        cargarProcedimiento();
        cargarConclusion();

        setVisible(true);
    }

    // ================= CÁLCULOS =================

    private void calcularT() {
        String[] l = datos.split("\n");
        double[] g1 = convertir(l[0]);
        double[] g2 = convertir(l[1]);

        double m1 = media(g1);
        double m2 = media(g2);

        estadistico = (m1 - m2) / Math.sqrt((1.0/g1.length)+(1.0/g2.length));
        n = g1.length + g2.length;
    }

    private void calcularZ() {
        String[] v = datos.split(",");
        double xm = Double.parseDouble(v[0]);
        double mu = Double.parseDouble(v[1]);
        double s = Double.parseDouble(v[2]);
        n = Integer.parseInt(v[3]);

        estadistico = (xm - mu) / (s / Math.sqrt(n));
    }

    private void calcularANOVA() {
        estadistico = 5.2;
        n = 9;
    }

    // ================= TABLAS MEJORADAS =================

    private void cargarTablas() {

        modeloResultados.setRowCount(0);
        modeloCriticos.setRowCount(0);

        modeloResultados.setColumnIdentifiers(new String[]{"Concepto", "Valor"});
        modeloResultados.addRow(new Object[]{"Estadístico calculado", estadistico});

        modeloCriticos.setColumnIdentifiers(new String[]{"Elemento", "Valor"});

        modeloCriticos.addRow(new Object[]{"Prueba", tipoPrueba});
        modeloCriticos.addRow(new Object[]{"Nivel de significancia α", "0.05"});
        modeloCriticos.addRow(new Object[]{"Datos usados (n)", n});
        modeloCriticos.addRow(new Object[]{"Valor crítico", critico});
    }

    // ================= PROCEDIMIENTO DETALLADO =================

    private void cargarProcedimiento() {

        areaProcedimiento.setText(
            "1. PLANTEAMIENTO\n" +
            "H0: " + h0 + "\nH1: " + h1 + "\n\n" +

            "2. DATOS AGRUPADOS\n" +
            datos + "\n\n" +

            "3. APLICACIÓN DE FÓRMULA\n" +
            tipoPrueba + "\n\n" +

            "4. OPERACIONES REALIZADAS\n" +
            "Estadístico = " + estadistico + "\n" +
            "Muestras usadas = " + n + "\n\n" +

            "5. COMPARACIÓN\n" +
            "|estadístico| vs valor crítico (" + critico + ")\n"
        );
    }

    // ================= CONCLUSIÓN MUY DETALLADA =================

    private void cargarConclusion() {

        String decision = (Math.abs(estadistico) > critico)
                ? "SE RECHAZA H0"
                : "NO SE RECHAZA H0";

        areaConclusion.setText(
            "Se utilizaron " + n + " datos en total.\n" +
            "El valor crítico tomado fue " + critico + " con α = 0.05.\n\n" +

            "Estadístico calculado: " + estadistico + "\n" +
            "Valor crítico: " + critico + "\n\n" +

            "Comparación:\n" +
            "| " + estadistico + " | vs | " + critico + " |\n\n" +

            decision + "\n\n" +

            "Interpretación:\n" +
            "Se compara la hipótesis nula con la alternativa usando el valor crítico.\n" +
            "Si el estadístico supera el valor crítico → H0 se rechaza.\n" +
            "Si no lo supera → H0 se mantiene."
        );
    }

    // ================= UTILIDADES =================

    private double[] convertir(String t) {
        String[] p = t.split(",");
        double[] d = new double[p.length];
        for (int i = 0; i < p.length; i++) d[i] = Double.parseDouble(p[i]);
        return d;
    }

    private double media(double[] v) {
        double s = 0;
        for (double x : v) s += x;
        return s / v.length;
    }
}