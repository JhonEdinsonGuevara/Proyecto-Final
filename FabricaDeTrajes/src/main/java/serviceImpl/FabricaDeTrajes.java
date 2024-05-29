package main.java.serviceImpl;

import main.java.exceptions.*;
import main.java.model.*;
import main.java.serviceI.IFabricaDeTrajes;

import java.util.*;

public class FabricaDeTrajes implements IFabricaDeTrajes {

    private ArrayList<Componente> componentesEnAlmacen;
    private TreeSet<Traje> trajesEnAlmacen;
    private boolean sonRebajas = false;

    public FabricaDeTrajes() {
        this.componentesEnAlmacen = new ArrayList<>();
        this.trajesEnAlmacen = new TreeSet<>(Comparator.comparing(Traje::getNombre));
    }

    @Override
    public void añadirComponenteAAlmacen() throws IdException, MuchoExtracomunitarioException, MangaException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione el tipo de componente:");
        System.out.println("1. Chaqueta");
        System.out.println("2. Blusa");
        System.out.println("3. Falda");
        System.out.println("4. Pantalón");
        int tipo = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Introduzca los datos del componente:");
        System.out.print("ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        for (Componente comp : componentesEnAlmacen) {
            if (comp.getId() == id) {
                throw new IdException("ID ya existe.");
            }
        }

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Talla: ");
        String talla = scanner.nextLine();
        System.out.print("Color: ");
        String color = scanner.nextLine();
        System.out.print("Es comunitario (true/false): ");
        boolean escomunitario = scanner.nextBoolean();
        System.out.print("Precio: ");
        double precio = scanner.nextDouble();
        scanner.nextLine();

        Componente componente = null;

        switch (tipo) {
            case 1://Chaqueta
                System.out.print("Número de botones: ");
                int numBotones = scanner.nextInt();
                scanner.nextLine();
                //añadimos 2 euros por cada boton
                precio += numBotones * 2;
                componente = new Chaqueta(id, nombre, talla, color, escomunitario, precio, numBotones);
                break;
            case 2://Blusa
                System.out.print("Manga larga (true/false): ");
                boolean mangaLarga = scanner.nextBoolean();
                scanner.nextLine();
                boolean tieneBlusaMismoColor = componentesEnAlmacen.stream()
                        .filter(c -> c instanceof Blusa)
                        .anyMatch(c -> c.getColor().equals(color));

                if (tieneBlusaMismoColor) {
                    boolean tieneMangaContraria = componentesEnAlmacen.stream()
                            .filter(c -> c instanceof Blusa && c.getColor().equals(color))
                            .anyMatch(c -> ((Blusa) c).isMangaLarga() != mangaLarga);

                    if (!tieneMangaContraria) {
                        throw new MangaException("No existe una blusa de manga contraria del mismo color.");
                    }
                }
                componente = new Blusa(id, nombre, talla, color, escomunitario, precio, mangaLarga);
                break;
            case 3://falda
                System.out.print("Con cremallera (true/false): ");
                boolean conCremalleraFalda = scanner.nextBoolean();
                scanner.nextLine();
                if (conCremalleraFalda) {
                    //añadimos 1 euro por cada cremallera
                    precio += 1;
                }
                componente = new Falda(id, nombre, talla, color, escomunitario, precio, conCremalleraFalda);
                break;
            case 4://pantalon
                System.out.print("Con cremallera (true/false): ");
                boolean conCremalleraPantalon = scanner.nextBoolean();
                scanner.nextLine();
                if (conCremalleraPantalon) {
                    //añadimos 1 euro por cada cremallera
                    precio += 1;
                }
                componente = new Pantalon(id, nombre, talla, color, escomunitario, precio, conCremalleraPantalon);
                break;
            default:
                System.out.println("Opción no válida.");
                return;
        }
        //Valida si hay demasiados componentes extracomunitarios
        long numExtracomunitarios = componentesEnAlmacen.stream().filter(c -> !c.isEscomunitario()).count();
        if (!escomunitario && numExtracomunitarios > componentesEnAlmacen.size() / 2) {
            throw new MuchoExtracomunitarioException("No se puede añadir más componentes extracomunitarios, Más del 50% de los componentes son extracomunitarios.");
        }
        componentesEnAlmacen.add(componente);
        System.out.println("Componente añadido al almacén.");

    }

    @Override
    public void listarComponentes() {
        if (componentesEnAlmacen.isEmpty()) {
            System.out.println("No hay componentes en el almacén.");
        } else {
            System.out.println("***** Lista de Coponentes *****");
            componentesEnAlmacen.forEach(System.out::println);
        }
    }

    @Override
    public void añadirTrajeAAlmacen() throws ColoresException, TallaException, TrajeYaExisteException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nombre del traje: ");
        String nombreTraje = scanner.nextLine();
        if (trajesEnAlmacen.stream().anyMatch(t -> t.getNombre().equals(nombreTraje))) {
            throw new TrajeYaExisteException("El nombre del traje ya existe en el almacén.");
        }

        System.out.println("Blusas disponibles:");
        componentesEnAlmacen.stream().filter(c -> c instanceof Blusa).forEach(System.out::println);
        System.out.print("ID de la blusa: ");
        int idBlusa = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Chaquetas disponibles:");
        componentesEnAlmacen.stream().filter(c -> c instanceof Chaqueta).forEach(System.out::println);
        System.out.print("ID de la chaqueta: ");
        int idChaqueta = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Faldas y Pantalones disponibles:");
        componentesEnAlmacen.stream().filter(c -> c instanceof Falda || c instanceof Pantalon).forEach(System.out::println);
        System.out.print("ID de la falda o pantalón: ");
        int idParteInferior = scanner.nextInt();
        scanner.nextLine();

        Componente blusa = buscarComponentePorId(idBlusa);
        Componente chaqueta = buscarComponentePorId(idChaqueta);
        Componente parteInferior = buscarComponentePorId(idParteInferior);

        if (blusa == null || chaqueta == null || parteInferior == null) {
            System.out.println("Error: Uno o más componentes no existen en el almacén.");
            return;
        }

        if (!blusa.getColor().startsWith(chaqueta.getColor().substring(0, 1)) ||
                !blusa.getColor().startsWith(parteInferior.getColor().substring(0, 1))) {
            throw new ColoresException("Los colores no son amigos.");
        }

        if (!blusa.getTalla().equals(chaqueta.getTalla()) ||
                (!parteInferior.getTalla().equals(blusa.getTalla()) && !(parteInferior instanceof Falda))) {
            throw new TallaException("Las tallas no coinciden.");
        }

        Traje traje = new Traje(nombreTraje);
        traje.agregarPieza(blusa);
        traje.agregarPieza(chaqueta);
        traje.agregarPieza(parteInferior);

        componentesEnAlmacen.remove(blusa);
        componentesEnAlmacen.remove(chaqueta);
        componentesEnAlmacen.remove(parteInferior);

        trajesEnAlmacen.add(traje);
        System.out.println("Traje creado y añadido al almacén.");
    }


    @Override
    public void listarTrajes() {
        if (trajesEnAlmacen.isEmpty()) {
            System.out.println("No hay trajes en el almacén.");
        } else {
            System.out.println("***** Trajes en el Almacen ***");
            trajesEnAlmacen.forEach(System.out::println);
        }
    }

    @Override
    public void activarDesactivarRebajas() {
        sonRebajas = !sonRebajas;
        double porcentajeDescuento = 0.20; // 20% de descuento

        if (sonRebajas) {
            // Aplicar descuento a todos los componentes
            for (Componente componente : componentesEnAlmacen) {
                componente.setDescuento(porcentajeDescuento);
            }
            // Aplicar descuento a todos los trajes
            for (Traje traje : trajesEnAlmacen) {
                traje.aplicarDescuento(porcentajeDescuento);
            }
            System.out.println("Rebajas activadas.");
        } else {
            // Restablecer precios originales de todos los componentes
            for (Componente componente : componentesEnAlmacen) {
                componente.setDescuento(0);
            }
            // Restablecer precios originales de todos los trajes
            for (Traje traje : trajesEnAlmacen) {
                traje.restablecerPrecioOriginal();
            }
            System.out.println("Rebajas desactivadas.");
        }
    }

    @Override
    public void crearEnvio() {
        Scanner scanner = new Scanner(System.in);
        List<Traje> envio = new ArrayList<>();

        boolean añadirMas = true;
        while (añadirMas) {
            System.out.println("Trajes disponibles:");
            trajesEnAlmacen.forEach(t -> System.out.println(t.getNombre()));
            System.out.print("Nombre del traje: ");
            String nombreTraje = scanner.nextLine();

            Traje traje = trajesEnAlmacen.stream().filter(t -> t.getNombre().equals(nombreTraje)).findFirst().orElse(null);
            if (traje == null) {
                System.out.println("Error: El traje no existe en el almacén.");
                return;
            }

            envio.add(traje);
            trajesEnAlmacen.remove(traje);

            System.out.print("¿Desea añadir más trajes al envío? (si/no): ");
            String respuesta = scanner.nextLine();
            añadirMas = respuesta.equalsIgnoreCase("si");
        }

        System.out.println("Envío creado con los trajes: " + envio);
    }

    @Override
    public void crearComponentesDePrueba() {
        try {
            // Añadimos componentes extracomunitarios (no más del 50%)
            componentesEnAlmacen.add(new Chaqueta(1, "Chaqueta Azul", "M", "Azul", false, 200.0, 3));
            componentesEnAlmacen.add(new Blusa(2, "Blusa Azul", "M", "Azul", false, 100.0, true));
            componentesEnAlmacen.add(new Falda(3, "Falda Azul", "S", "Azul", false, 100.0, true)); // La falda puede ser de talla diferente
            componentesEnAlmacen.add(new Pantalon(4, "Pantalón Azul", "M", "Azul", false, 100.0, true));

            // Añadimos componentes comunitarios
            componentesEnAlmacen.add(new Chaqueta(5, "Chaqueta Roja", "M", "Rojo", true, 300.0, 4));
            componentesEnAlmacen.add(new Blusa(6, "Blusa Roja", "M", "Rojo", true, 100.0, false));
            componentesEnAlmacen.add(new Falda(7, "Falda Roja", "S", "Rojo", true, 100.0, false)); // La falda puede ser de talla diferente
            componentesEnAlmacen.add(new Pantalon(8, "Pantalón Rojo", "M", "Rojo", true, 100.0, true));

            // Añadimos blusas de manga larga y corta del mismo color
            componentesEnAlmacen.add(new Blusa(9, "Blusa Amarilla Larga", "M", "Amarillo", true, 200.0, true));
            componentesEnAlmacen.add(new Blusa(10, "Blusa Amarilla Corta", "M", "Amarillo", true, 100.0, false));

            // Añadir componentes adicionales con colores amigos para asegurar compatibilidad
            componentesEnAlmacen.add(new Blusa(11, "Blusa Azul Corta", "M", "Azul", true, 100.0, false));
            componentesEnAlmacen.add(new Pantalon(12, "Pantalón Amarillo", "M", "Amarillo", true, 100.0, true));
            componentesEnAlmacen.add(new Chaqueta(13, "Chaqueta Azul", "M", "Azul", true, 200.0, 3));
            componentesEnAlmacen.add(new Falda(14, "Falda Azul", "S", "Azul", true, 100.0, false)); // La falda puede ser de talla diferente

            // Añadimos componentes de color rosa con precios redondeados
            componentesEnAlmacen.add(new Chaqueta(15, "Chaqueta Rosa", "M", "Rosa", true, 200.0, 3));
            componentesEnAlmacen.add(new Blusa(16, "Blusa Rosa", "M", "Rosa", true, 100.0, true));
            componentesEnAlmacen.add(new Falda(17, "Falda Rosa", "S", "Rosa", true, 100.0, true)); // La falda puede ser de talla diferente
            componentesEnAlmacen.add(new Pantalon(18, "Pantalón Rosa", "M", "Rosa", true, 100.0, true));

            System.out.println("Componentes de prueba creados.");
        } catch (Exception e) {
            System.out.println("Error al crear componentes de prueba: " + e.getMessage());
        }
    }

    private Componente buscarComponentePorId(int id) {
        return componentesEnAlmacen.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public void mostrarCantidadComponentesPorSubclase() {
        int chaquetaCount = 0;
        int blusaCount = 0;
        int faldaCount = 0;
        int pantalonCount = 0;

        for (Componente componente : componentesEnAlmacen) {
            if (componente instanceof Chaqueta) {
                chaquetaCount++;
            } else if (componente instanceof Blusa) {
                blusaCount++;
            } else if (componente instanceof Falda) {
                faldaCount++;
            } else if (componente instanceof Pantalon) {
                pantalonCount++;
            }
        }

        System.out.println("Cantidad de componentes por subclase:");
        System.out.println("Chaqueta: " + chaquetaCount);
        System.out.println("Blusa: " + blusaCount);
        System.out.println("Falda: " + faldaCount);
        System.out.println("Pantalón: " + pantalonCount);
    }

    public void escribirMenu() {
        System.out.println("MENU FABRICA TRAJES");
        System.out.println("1.- Añadir Componente a almacén");
        System.out.println("2.- Listar Componentes del almacén");
        System.out.println("3.- Crear traje y añadir a almacén");
        System.out.println("4.- Listar trajes del almacén");
        System.out.println("5.- Mostrar cantidad de componentes por subclase");
        System.out.println("7.- Activar/Desactivar las rebajas");
        System.out.println("8.- Crear envío");
        System.out.println("9.- Crear componentes de prueba");
        System.out.println("0.- Salir");
    }

    public static void main(String[] args) {
        FabricaDeTrajes fabrica = new FabricaDeTrajes();
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            fabrica.escribirMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

            try {
                switch (opcion) {
                    case 1:
                        fabrica.añadirComponenteAAlmacen();
                        break;
                    case 2:
                        fabrica.listarComponentes();
                        break;
                    case 3:
                        fabrica.añadirTrajeAAlmacen();
                        break;
                    case 4:
                        fabrica.listarTrajes();
                        break;
                    case 5: // primer reuqerimiento Nueva opción para mostrar la cantidad de componentes por subclase.
                        fabrica.mostrarCantidadComponentesPorSubclase();
                        break;
                    case 7:
                        fabrica.activarDesactivarRebajas();
                        break;
                    case 8:
                        fabrica.crearEnvio();
                        break;
                    case 9:
                        fabrica.crearComponentesDePrueba();
                        break;
                    case 0:
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
