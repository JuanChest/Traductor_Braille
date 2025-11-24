package com.traductor.controller;

import com.traductor.model.ITraductor;
import com.traductor.view.InterfazTraductor;

/**
 * Controlador que gestiona la interacción entre la vista y el modelo.
 *
 * @see ITraductor
 * @see InterfazTraductor
 */
public class TraductorController {
    private ITraductor traductor;
    private InterfazTraductor vista;
    
    /**
     * Constructor que inicializa el controlador.
     *
     * @param vista La interfaz de usuario.
     * @param traductor El traductor a utilizar.
     */
    public TraductorController(InterfazTraductor vista, ITraductor traductor) {
        this.vista = vista;
        this.traductor = traductor;
    }
    
    /**
     * Procesa la traducción solicitada por la vista.
     */
    public void traducir() {
        String textoEntrada = vista.obtenerTexto();
        
        if (textoEntrada == null || textoEntrada.trim().isEmpty()) {
            vista.mostrarResultado("Por favor ingrese texto para traducir");
            return;
        }
        
        String resultado = traductor.traducir(textoEntrada);
        vista.mostrarResultado(resultado);
    }
}
