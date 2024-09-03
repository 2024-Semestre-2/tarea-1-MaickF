/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import Vista.PantallaConfiguracion;
import Vista.PantallaMemoria;
import Vista.PantallaPrincipal;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import java.io.*;
import java.io.IOException;
import java.awt.Desktop;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author Fabian
 */
public class Controlador{

    private PantallaPrincipal pp;
    private PantallaConfiguracion pconfig;
    private PantallaMemoria pm;
    private Archivo archivoActual;
    private Memoria memoria;
    private BloqueProceso bcp;
    private Registro[] registros;
    private String[] identificadores;
    private String[] operaciones;
    private String ir;
    DefaultTableModel dtm = new DefaultTableModel(0, 0);
    DefaultTableModel dtm2 = new DefaultTableModel(0, 0);

    public void gestionarBotones(){
        pp.getBtnSeleccionar().addActionListener(btnSeleccionar_ActionPerformed);
        pp.getBtnCargar().addActionListener(btnCargar_ActionPerformed);
        pp.getBtnInstruccion().addActionListener(btnInstruccion_ActionPerformed);
        pp.getBtnMemoria().addActionListener(btnMemoria_ActionPerformed);
        pp.getBtnConfiguracion().addActionListener(btnConfiguracion_ActionPerformed);
        pm.getBtnCerrar().addActionListener(btnCerrar_ActionPerformed);
        pconfig.getBtnVolver().addActionListener(btnVolver_ActionPerformed);
        pconfig.getBtnGuardar().addActionListener(btnGuardar_ActionPerformed);
    }

    ActionListener btnInstruccion_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int posicion = registros[buscarRegistro("PC")].getValor();
            if(posicion<bcp.getFinalMemoria()){
                if(bcp.getEstado().equals("Preparado")){
                    bcp.setEstado("Ejecutando");
                }
                Instruccion instruccionActual = (Instruccion) memoria.getContenido()[posicion];
                if(instruccionActual.getOperacion().equals("MOV")){
                    registros[buscarRegistro(instruccionActual.getRegistroDestino())].setValor(instruccionActual.getValor());
                }else if(instruccionActual.getOperacion().equals("ADD")){
                    int posicion2 = buscarRegistro("AC");
                    int posicion3 = buscarRegistro(instruccionActual.getRegistroOrigen());
                    int valorActual = registros[posicion2].getValor();
                    valorActual+=registros[posicion3].getValor();
                    registros[buscarRegistro(instruccionActual.getRegistroDestino())].setValor(valorActual);
                }else if(instruccionActual.getOperacion().equals("SUB")){
                    int posicion2 = buscarRegistro("AC");
                    int posicion3 = buscarRegistro(instruccionActual.getRegistroOrigen());
                    int valorActual = registros[posicion2].getValor();
                    valorActual-=registros[posicion3].getValor();
                    registros[buscarRegistro(instruccionActual.getRegistroDestino())].setValor(valorActual);
                }else if(instruccionActual.getOperacion().equals("LOAD")){
                    int posicion3 = buscarRegistro(instruccionActual.getRegistroOrigen());
                    int valorActual = registros[posicion3].getValor();
                    registros[buscarRegistro(instruccionActual.getRegistroDestino())].setValor(valorActual);
                }else{
                    int posicion2 = buscarRegistro("AC");
                    int valorActual = registros[posicion2].getValor();
                    registros[buscarRegistro(instruccionActual.getRegistroOrigen())].setValor(valorActual);
                }
                posicion++;
                registros[buscarRegistro("PC")].setValor(posicion);
                if(posicion==bcp.getFinalMemoria()){
                    bcp.setEstado("Terminado");
                }else{
                    Instruccion siguienteInstruccion = (Instruccion) memoria.getContenido()[posicion];
                    ir = siguienteInstruccion.getLinea();
                }
                actualizarBcp();
                actualizarMemoriaSO();
                actualizarTabla();
            }else{
                inicializarRegistros();
                actualizarMemoriaSO();
                actualizarTabla();
            }
        }
    };

    ActionListener btnSeleccionar_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String contenido = "";
            String formato = "";
            JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(".")); //sets current directory
			int response = fileChooser.showOpenDialog(null); //select file to open
			if(response == JFileChooser.APPROVE_OPTION) {
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                int i = file.getName().lastIndexOf('.');
                if (i > 0) {
                    formato = file.getName().substring(i + 1);
                }
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        contenido += linea + "\n";
                    }
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
				System.out.println(formato);
			}
            archivoActual = new Archivo(contenido, formato);
            pp.getjTextArea1().setText(contenido);
        }
    };

    ActionListener btnMemoria_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pp.setVisible(false);
            pm.setVisible(true);
        }
    };
    ActionListener btnConfiguracion_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pp.setVisible(false);
            pconfig.setVisible(true);
        }
    };

    ActionListener btnCerrar_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pp.setVisible(true);
            pm.setVisible(false);
        }
    };

    ActionListener btnVolver_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pp.setVisible(true);
            pconfig.setVisible(false);
        }
    };

    ActionListener btnGuardar_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int valor = Integer.parseInt(pconfig.getjTextField1().getText());
            memoria.setLimite(valor);
            pp.setVisible(true);
            pm.setVisible(false);
        }
    };

    ActionListener btnCargar_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<Instruccion> instrucciones = new ArrayList<>();
            String contenido = archivoActual.getContenido();
            String contenido2 = contenido.replaceAll(",", "");
            String correccion = contenido2.trim();
            String correccion2 = correccion.toUpperCase();
            String[] listaTemporal = correccion2.split("\\n+");
            for(int i = 0; i<listaTemporal.length; i++){
                String actual = listaTemporal[i];
                String[] division = actual.split("\\s+");
                int validacion = validarOperacion(division[0]);
                if(division.length == 3){
                    if(validacion == 0){
                        boolean validacion2 = validarRegistro(division[1]);
                        boolean validacion3 = esNumerico(division[2]);
                        if(validacion2 && validacion3){
                            int valor = Integer.parseInt(division[2]);
                            Instruccion nuevInstruccion = new Instruccion(division[0], division[1], valor, actual);
                            instrucciones.add(nuevInstruccion);
                        }
                    }
                }else if(division.length == 2){
                    if(validacion > 0){
                        boolean validacion2 = validarRegistro(division[1]);
                        if(validacion2){
                            Instruccion nuevInstruccion = new Instruccion(division[0], "AC", division[1], actual);
                            instrucciones.add(nuevInstruccion);
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog (null, "Error semantico", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if(instrucciones.size() == listaTemporal.length){
                Random random = new Random();
                int min = memoria.getLimite();
                int max = 1024;
                int posicionAleatoria = random.nextInt(max - min + 1) + min;
                for(int i = 0; i<instrucciones.size(); i++){
                    memoria.getContenido()[posicionAleatoria]=instrucciones.get(i);
                    posicionAleatoria++;
                }
                posicionAleatoria-=instrucciones.size();
                bcp.setInicioMemoria(posicionAleatoria);
                bcp.setFinalMemoria(posicionAleatoria+instrucciones.size());
                System.out.println("LLEGO");
                ir = instrucciones.get(0).getLinea();
                registros[buscarRegistro("PC")].setValor(posicionAleatoria);
                bcp.getRegistros().replace("PC", posicionAleatoria);
                bcp.setEstado("Preparado");
                actualizarMemoriaSO();
                actualizarTabla();
            }else{
                JOptionPane.showMessageDialog (null, "Error en el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    };

    public Controlador(){
        this.pp = new PantallaPrincipal();
        this.pconfig = new PantallaConfiguracion();
        this.pm = new PantallaMemoria();
        this.memoria = new Memoria();
        this.ir = "-";
        this.identificadores = new String[]{"AX", "BX", "CX", "DX", "AC", "PC"};
        this.operaciones = new String[]{"MOV", "LOAD", "STORE", "ADD", "SUB"};
        this.registros = new Registro[6];
        this.bcp = new BloqueProceso("nuevo","-", 0, 0);
        inicializarRegistros();
        gestionarBotones();
        String columnas[] = new String[] {"ID", "Estado","AX", "BX", "CX", "DX", "AC", "PC", "IR", "Inicio", "Final"};
        dtm.setColumnIdentifiers(columnas);
        pp.getjTable1().setModel(dtm);
        pp.getJScrollPane2().setViewportView(pp.getjTable1());
        actualizarTabla();
        String columnas2[] = new String[] {"Memoria"};
        dtm2.setColumnIdentifiers(columnas2);
        pm.getjTable1().setModel(dtm2);
        pm.getjScrollPane1().setViewportView(pm.getjTable1());
        this.pp.setVisible(true);
    }

    public void actualizarTablaMemoria(){
        dtm2 = new DefaultTableModel(0, 0);
        String columnas2[] = new String[] {"Memoria"};
        dtm2.setColumnIdentifiers(columnas2);
        pm.getjTable1().setModel(dtm2);
        pm.getjScrollPane1().setViewportView(pm.getjTable1());
        for(int i = 0; i<memoria.getContenido().length; i++){
            Vector<Object> vector = new Vector<>();
            vector.add(memoria.getContenido()[i]);
            dtm2.addRow(vector);
        }
    }

    public void inicializarRegistros(){
        int cont = 0;
        for(String i : identificadores){
            if(i.equals("PC")){
                Registro registroActual = new Registro(i, "contador", 0);
                registros[cont]=registroActual;
                cont++;
                bcp.getRegistros().put(i, 0);
            }else if(i.equals("AC")){
                Registro registroActual = new Registro(i, "acumulador", 0);
                registros[cont]=registroActual;
                cont++;
                bcp.getRegistros().put(i, 0);
            }else{
                Registro registroActual = new Registro(i, "general", 0);
                registros[cont]=registroActual;
                cont++;
                bcp.getRegistros().put(i, 0);
            }
        }
    }

    public void actualizarMemoriaSO(){
        Arrays.fill(memoria.getContenido(), 0, memoria.getLimite(), null);
        Random random = new Random();
        int max = memoria.getLimite();
        int min = 0;
        int posicionAleatoria = random.nextInt(max - min + 1) + min;
        memoria.getContenido()[posicionAleatoria] = bcp.getId();
        posicionAleatoria++;
        memoria.getContenido()[posicionAleatoria] = bcp.getEstado();
        posicionAleatoria++;
        memoria.getContenido()[posicionAleatoria] = bcp.getInicioMemoria();
        posicionAleatoria++;
        memoria.getContenido()[posicionAleatoria] = bcp.getFinalMemoria();
        posicionAleatoria++;
        for (Map.Entry<String, Integer> entry : bcp.getRegistros().entrySet()) {
            memoria.getContenido()[posicionAleatoria] = entry.getKey() + " = " + entry.getValue().toString();
            posicionAleatoria++;
        }
        memoria.getContenido()[posicionAleatoria] = ir;
        actualizarTablaMemoria();
    }

    public void actualizarBcp(){
        for(int i = 0; i<registros.length; i++){
            String id = registros[i].getIdentificador();
            bcp.getRegistros().replace(id, registros[i].getValor());
        }
    }

    public int validarOperacion(String operacion) {
        for(int i = 0; i<operaciones.length; i++){
            if (operaciones[i].equals(operacion)) {
                return i;
            }
        }
        return -1;
    }

    public int buscarRegistro(String identificador){
        for(int i = 0; i<registros.length; i++){
            if(identificador.equals(registros[i].getIdentificador())){
                return i;
            }
        }
        return 0;
    }

    public void actualizarTabla(){
        dtm.addRow(new Object[] { bcp.getId(), bcp.getEstado(), bcp.getRegistros().get("AX"), bcp.getRegistros().get("BX"), bcp.getRegistros().get("CX"), bcp.getRegistros().get("DX"), bcp.getRegistros().get("AC"), bcp.getRegistros().get("PC"), this.ir, bcp.getInicioMemoria(),bcp.getFinalMemoria()});
    }

    public boolean validarRegistro(String registro) {
        for(int i = 0; i<identificadores.length; i++){
            if (identificadores[i].equals(registro)) {
                return true;
            }
        }
        return false;
    }

    public boolean esNumerico(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        char[] cadena = str.toCharArray();
        if(cadena[0]=='-'){
            cadena = Arrays.copyOfRange(cadena, 1, cadena.length);
        }
        for (char c : cadena) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
