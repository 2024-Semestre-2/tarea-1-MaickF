/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author Fabian
 */
public class Instruccion {
    
    private String registroDestino;
    private String registroOrigen;
    private int valor;
    private String linea;
    private String operacion;

    public Instruccion(String operacion, String registroDestino, String registroOrigen, String linea){
        this.registroDestino = registroDestino;
        this.registroOrigen = registroOrigen;
        this.linea = linea;
        this.operacion = operacion;
    }

    public Instruccion(String operacion, String registroDestino, int valor, String linea){
        this.registroDestino = registroDestino;
        this.valor = valor;
        this.linea = linea;
        this.operacion = operacion;
    }

    /**
     * @return the registroDestino
     */
    public String getOperacion() {
        return operacion;
    }

    /**
     * @param registroDestino the registroDestino to set
     */
    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    /**
     * @return the registroDestino
     */
    public String getRegistroDestino() {
        return registroDestino;
    }

    /**
     * @param registroDestino the registroDestino to set
     */
    public void setRegistroDestino(String registroDestino) {
        this.registroDestino = registroDestino;
    }

    /**
     * @return the registroOrigen
     */
    public String getRegistroOrigen() {
        return registroOrigen;
    }

    /**
     * @param registroOrigen the registroOrigen to set
     */
    public void setRegistroOrigen(String registroOrigen) {
        this.registroOrigen = registroOrigen;
    }

    /**
     * @return the valor
     */
    public int getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(int valor) {
        this.valor = valor;
    }

    /**
     * @return the linea
     */
    public String getLinea() {
        return linea;
    }

    /**
     * @param linea the linea to set
     */
    public void setLinea(String linea) {
        this.linea = linea;
    }
}
