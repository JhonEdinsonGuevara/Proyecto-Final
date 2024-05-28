package main.java.model;

public class Componente implements Comparable<Componente>{

    private int id;
    private String nombre;
    private String talla;
    private String color;
    private boolean escomunitario;
    private double precio;
    // Nuevo campo para almacenar el precio original, para las rebajas.
    private double descuento;

    public Componente(int id, String nombre, String talla, String color, boolean escomunitario, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.talla = talla;
        this.color = color;
        this.escomunitario = escomunitario;
        this.precio = precio;
        this.descuento=0;
    }

    public Componente() {
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isEscomunitario() {
        return escomunitario;
    }

    public void setEscomunitario(boolean escomunitario) {
        this.escomunitario = escomunitario;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Componente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", talla='" + talla + '\'' +
                ", color='" + color + '\'' +
                ", escomunitario=" + escomunitario +
                ", precio=" + precio* (1-descuento) +
                '}';
    }

    // Método equals (componentes son iguales si tienen el mismo id)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Componente that = (Componente) obj;
        return id == that.id;
    }

    // Método compareTo (componentes se ordenan por id)
    @Override
    public int compareTo(Componente o) {
        return Integer.compare(this.id, o.id);
    }

}
