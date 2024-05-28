package main.java.model;

import java.util.ArrayList;
import java.util.Objects;

public class Traje implements Comparable<Traje> {

    private ArrayList<Componente> piezas;
    private String nombre;

    // Constructor
    public Traje(String nombre) {
        this.nombre = nombre;
        this.piezas = new ArrayList<>();
    }

    public Traje() {
        this.piezas = new ArrayList<>();
    }

    // Getters y Setters
    public ArrayList<Componente> getPiezas() {
        return piezas;
    }

    public void setPiezas(ArrayList<Componente> piezas) {
        this.piezas = piezas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método toString
    @Override
    public String toString() {
        return "Traje [nombre= " + nombre + ", piezas= " + piezas + "]";
    }

    // Método para agregar una pieza al traje
    public void agregarPieza(Componente pieza) {
        if (piezas == null) {
            piezas = new ArrayList<>();
        }
        piezas.add(pieza);
    }

    @Override
    public int compareTo(Traje otroTraje) {
        // Implementación de la comparación
        return this.nombre.compareTo(otroTraje.getNombre());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Traje traje = (Traje) o;
        return Objects.equals(nombre, traje.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    public void aplicarDescuento(double porcentaje) {
        for (Componente pieza : piezas) {
            pieza.setDescuento(porcentaje);
        }
    }

    public void restablecerPrecioOriginal() {
        for (Componente pieza : piezas) {
            pieza.setDescuento(0);
        }
    }

}