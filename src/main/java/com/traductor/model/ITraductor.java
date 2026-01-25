package com.traductor.model;

/**
 * Interfaz para definir el comportamiento de un traductor.
 *
 * @see TraductorBraille
 */
public interface ITraductor {
    /**
     * Traduce un texto a su representación correspondiente.
     *
     * @param entrada El texto a traducir.
     * @return El texto traducido.
     */
    String traducir(String entrada);
    
    /**
     * Traduce texto Braille (en Unicode) de vuelta a español.
     *
     * @param textoBraille El texto en Braille a traducir.
     * @return El texto en español.
     */
    String traducirBrailleAEspanol(String textoBraille);
}
