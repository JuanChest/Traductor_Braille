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
}
