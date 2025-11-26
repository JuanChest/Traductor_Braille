package com.traductor.model;

/**
 * Implementa la traducción de texto a Braille.
 *
 * @see ITraductor
 * @see DiccionarioBraille
 */
public class TraductorBraille implements ITraductor {
    private DiccionarioBraille diccionario;
    
    /**
     * Inicializa el traductor con un diccionario.
     */
    public TraductorBraille() {
        this.diccionario = new DiccionarioBraille();
    }
    
    /**
     * Traduce un texto a su representación en Braille.
     *
     * @param texto El texto a traducir.
     * @return El texto traducido a Braille con símbolos Unicode.
     */
    public String traducir(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }
        
        StringBuilder resultado = new StringBuilder();
        boolean enModoNumero = false;
        
        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);
            
            // Detectar si es un número
            if (diccionario.esNumero(caracter)) {
                // Si no estamos en modo número, agregar el prefijo
                if (!enModoNumero) {
                    SimboloBraille prefijo = new SimboloBraille(diccionario.obtenerPrefijoNumero());
                    resultado.append(convertirABrailleUnicode(prefijo));
                    enModoNumero = true;
                }
                
                SimboloBraille simbolo = getSimbolo(caracter);
                if (simbolo != null) {
                    resultado.append(convertirABrailleUnicode(simbolo));
                }
            } else if (caracter == ',' || caracter == '.') {
                // Puntos y comas no salen del modo número
                SimboloBraille simbolo = getSimbolo(caracter);
                if (simbolo != null) {
                    resultado.append(convertirABrailleUnicode(simbolo));
                }
            } else if (caracter == ' ') {
                // Los espacios salen del modo número
                enModoNumero = false;
                SimboloBraille simbolo = getSimbolo(caracter);
                if (simbolo != null) {
                    resultado.append(convertirABrailleUnicode(simbolo));
                }
            } else {
                // Cualquier otro carácter sale del modo número
                enModoNumero = false;
                
                // Detectar si es mayúscula
                if (Character.isUpperCase(caracter)) {
                    // Verificar si la palabra completa está en mayúsculas
                    boolean palabraCompletaMayuscula = esPalabraCompletaMayuscula(texto, i);
                    
                    if (palabraCompletaMayuscula) {
                        // Solo agregar prefijo de mayúscula al inicio de la palabra
                        boolean esInicioPalabra = (i == 0 || !Character.isLetter(texto.charAt(i - 1)));
                        if (esInicioPalabra) {
                            SimboloBraille prefijoMayus = new SimboloBraille(diccionario.obtenerPrefijoMayuscula());
                            resultado.append(convertirABrailleUnicode(prefijoMayus));
                        }
                    } else {
                        // Agregar prefijo de mayúscula para cada letra mayúscula individual
                        SimboloBraille prefijoMayus = new SimboloBraille(diccionario.obtenerPrefijoMayuscula());
                        resultado.append(convertirABrailleUnicode(prefijoMayus));
                    }
                    
                    // Convertir a minúscula para obtener el símbolo
                    SimboloBraille simbolo = getSimbolo(Character.toLowerCase(caracter));
                    if (simbolo != null) {
                        resultado.append(convertirABrailleUnicode(simbolo));
                    }
                } else {
                    SimboloBraille simbolo = getSimbolo(caracter);
                    if (simbolo != null) {
                        resultado.append(convertirABrailleUnicode(simbolo));
                    } else {
                        resultado.append(caracter); // Mantener caracteres no traducibles
                    }
                }
            }
        }
        
        return resultado.toString();
    }
    
    /**
     * Verifica si una palabra completa está en mayúsculas.
     *
     * @param texto El texto completo.
     * @param posicion La posición del carácter actual.
     * @return true si la palabra completa está en mayúsculas, false en caso contrario.
     */
    private boolean esPalabraCompletaMayuscula(String texto, int posicion) {
        // Encontrar el inicio de la palabra
        int inicio = posicion;
        while (inicio > 0 && Character.isLetter(texto.charAt(inicio - 1))) {
            inicio--;
        }
        
        // Encontrar el fin de la palabra
        int fin = posicion;
        while (fin < texto.length() && Character.isLetter(texto.charAt(fin))) {
            fin++;
        }
        
        // Verificar si todas las letras de la palabra son mayúsculas
        for (int i = inicio; i < fin; i++) {
            char c = texto.charAt(i);
            if (Character.isLetter(c) && !Character.isUpperCase(c)) {
                return false;
            }
        }
        
        // Solo considerar como palabra completa en mayúsculas si tiene al menos 2 letras
        return (fin - inicio) >= 2;
    }
    
    /**
     * Convierte un símbolo Braille a su representación Unicode.
     *
     * @param simbolo El símbolo Braille a convertir.
     * @return El carácter Unicode Braille correspondiente.
     */
    private String convertirABrailleUnicode(SimboloBraille simbolo) {
        // El Unicode Braille comienza en U+2800
        // Cada punto se mapea a un bit: punto1=bit0, punto2=bit1, punto3=bit2, etc.
        int valor = 0x2800;
        
        if (simbolo.isPuntoActivado(1)) valor += 0x01;
        if (simbolo.isPuntoActivado(2)) valor += 0x02;
        if (simbolo.isPuntoActivado(3)) valor += 0x04;
        if (simbolo.isPuntoActivado(4)) valor += 0x08;
        if (simbolo.isPuntoActivado(5)) valor += 0x10;
        if (simbolo.isPuntoActivado(6)) valor += 0x20;
        
        return String.valueOf((char) valor);
    }
    
    /**
     * Obtiene el símbolo Braille correspondiente a un carácter.
     *
     * @param caracter El carácter a convertir.
     * @return El símbolo Braille correspondiente.
     */
    public SimboloBraille getSimbolo(char caracter) {
        String patron = diccionario.obtenerPatron(caracter);
        if (patron != null) {
            return new SimboloBraille(patron);
        }
        return null;
    }
}
