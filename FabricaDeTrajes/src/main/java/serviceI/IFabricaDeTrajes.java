package main.java.serviceI;

import main.java.exceptions.*;

public interface IFabricaDeTrajes {

    void añadirComponenteAAlmacen() throws IdException, MuchoExtracomunitarioException, MangaException;
    void listarComponentes();
    void añadirTrajeAAlmacen() throws ColoresException, TallaException, TrajeYaExisteException;
    void listarTrajes();
    void activarDesactivarRebajas();
    void crearEnvio();
    void crearComponentesDePrueba();
}