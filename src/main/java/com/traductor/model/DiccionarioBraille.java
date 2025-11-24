package com.traductor.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Diccionario de traducción a Braille.
 *
 * @see SimboloBraille
 * @see TraductorBraille
 */
public class DiccionarioBraille {
    private Map<String, String> mapaBraille;
    
    /**
     * Inicializa el diccionario con el alfabeto Braille.
     */
    public DiccionarioBraille() {
        this.mapaBraille = new HashMap<>();
        inicializarDiccionario();
    }
    
    /**
     * Obtiene el patrón Braille correspondiente a una letra.
     *
     * @param letra La letra a buscar en el diccionario.
     * @return El patrón Braille correspondiente o null si no existe.
     */
    public String obtenerPatron(char letra) {
        String letraStr = String.valueOf(Character.toLowerCase(letra));
        return mapaBraille.getOrDefault(letraStr, null);
    }
    
    /**
     * Verifica si un carácter es un dígito.
     *
     * @param c El carácter a verificar.
     * @return true si es un dígito, false en caso contrario.
     */
    public boolean esNumero(char c) {
        return Character.isDigit(c);
    }
    
    /**
     * Obtiene el prefijo de número en Braille (3456).
     *
     * @return El patrón del prefijo de número.
     */
    public String obtenerPrefijoNumero() {
        return "3456";
    }
    
    /**
     * Obtiene el prefijo de mayúscula en Braille (46).
     *
     * @return El patrón del prefijo de mayúscula.
     */
    public String obtenerPrefijoMayuscula() {
        return "46";
    }
    
    /**
     * Inicializa el diccionario con las correspondencias letra-Braille.
     */
    private void inicializarDiccionario() {
        // Letras minúsculas
        mapaBraille.put("a", "1");
        mapaBraille.put("b", "12");
        mapaBraille.put("c", "14");
        mapaBraille.put("d", "145");
        mapaBraille.put("e", "15");
        mapaBraille.put("f", "124");
        mapaBraille.put("g", "1245");
        mapaBraille.put("h", "125");
        mapaBraille.put("i", "24");
        mapaBraille.put("j", "245");
        mapaBraille.put("k", "13");
        mapaBraille.put("l", "123");
        mapaBraille.put("m", "134");
        mapaBraille.put("n", "1345");
        mapaBraille.put("ñ", "12456");
        mapaBraille.put("o", "135");
        mapaBraille.put("p", "1234");
        mapaBraille.put("q", "12345");
        mapaBraille.put("r", "1235");
        mapaBraille.put("s", "234");
        mapaBraille.put("t", "2345");
        mapaBraille.put("u", "136");
        mapaBraille.put("v", "1236");
        mapaBraille.put("w", "2456");
        mapaBraille.put("x", "1346");
        mapaBraille.put("y", "13456");
        mapaBraille.put("z", "1356");
        
        // Letras adicionales con acentos y dieresis
        mapaBraille.put("ñ", "12456");
        mapaBraille.put("w", "2456");
        mapaBraille.put("á", "12356");
        mapaBraille.put("é", "2346");
        mapaBraille.put("í", "34");
        mapaBraille.put("ó", "346");
        mapaBraille.put("ú", "23456");
        mapaBraille.put("ü", "1256");
        
        // Números - Requieren prefijo de número (3456) seguido de letras a-j
        mapaBraille.put("1", "1");     // usa 'a'
        mapaBraille.put("2", "12");    // usa 'b'
        mapaBraille.put("3", "14");    // usa 'c'
        mapaBraille.put("4", "145");   // usa 'd'
        mapaBraille.put("5", "15");    // usa 'e'
        mapaBraille.put("6", "124");   // usa 'f'
        mapaBraille.put("7", "1245");  // usa 'g'
        mapaBraille.put("8", "125");   // usa 'h'
        mapaBraille.put("9", "24");    // usa 'i'
        mapaBraille.put("0", "245");   // usa 'j'
        
        // Caracteres especiales y signos de puntuación
        mapaBraille.put(" ", "0");        // espacio (sin puntos)
        mapaBraille.put(".", "3");        // punto
        mapaBraille.put(",", "2");        // coma
        mapaBraille.put(";", "23");       // punto y coma
        mapaBraille.put(":", "25");       // dos puntos
        mapaBraille.put("?", "26");       // interrogación  
        mapaBraille.put("¿", "26");       // interrogación
        mapaBraille.put("¡", "235");
        mapaBraille.put("!", "235");      // exclamación
        mapaBraille.put("-", "36");       // guion
        mapaBraille.put("(", "126");      // paréntesis abierto
        mapaBraille.put(")", "345");      // paréntesis cerrado
        mapaBraille.put("\"", "236");     // comillas dobles
        
        // Signos matemáticos y otros
        mapaBraille.put("+", "346");      // más
        mapaBraille.put("*", "16");       // por/multiplicación
        mapaBraille.put("×", "16");       // por (símbolo alternativo)
        mapaBraille.put("/", "34");       // dividido/división
        mapaBraille.put("÷", "256");       // dividido (símbolo alternativo)
        mapaBraille.put("=", "2356");   // igual
    }
}
