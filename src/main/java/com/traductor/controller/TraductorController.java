package com.traductor.controller;

import com.traductor.model.ITraductor;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Controlador FXML que gestiona la interacción entre la vista y el modelo.
 *
 * @see ITraductor
 */
public class TraductorController {
    @FXML
    private TextArea textoEntrada;

    @FXML
    private TextArea textoSalida;

    private ITraductor traductor;

    /**
     * Constructor sin argumentos requerido por FXML.
     */
    public TraductorController() {
    }

    /**
     * Establece el traductor a utilizar.
     *
     * @param traductor El traductor a utilizar.
     */
    public void setTraductor(ITraductor traductor) {
        this.traductor = traductor;
    }

    /**
     * Método de inicialización llamado automáticamente por FXML.
     */
    @FXML
    private void initialize() {
        // Inicialización adicional si es necesaria
    }

    /**
     * Procesa la traducción solicitada por el usuario.
     * Este método es invocado cuando se hace clic en el botón "Traducir a Braille".
     */
    @FXML
    private void traducir() {
        String texto = textoEntrada.getText();

        if (texto == null || texto.trim().isEmpty()) {
            textoSalida.setText("Por favor ingrese texto para traducir");
            return;
        }

        String resultado = traductor.traducir(texto);
        textoSalida.setText(resultado);
    }
}
