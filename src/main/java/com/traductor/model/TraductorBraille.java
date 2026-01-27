package com.traductor.model;

/**
 * Implementa la traducción bidireccional entre Español y Braille Unicode.
 */
public class TraductorBraille implements ITraductor {
    private DiccionarioBraille diccionario;

    public TraductorBraille() {
        this.diccionario = new DiccionarioBraille();
    }

    /**
     * Traduce texto de Español a Braille Unicode.
     * 
     * @param texto El texto en Español a traducir.
     * @return El texto traducido en Braille Unicode.
     */
    @Override
    public String traducir(String texto) {
        if (texto == null || texto.isEmpty())
            return "";

        StringBuilder resultado = new StringBuilder();
        boolean enModoNumero = false;

        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);

            if (diccionario.esNumero(caracter)) {
                if (!enModoNumero) {
                    resultado.append(convertirABrailleUnicode(new SimboloBraille(diccionario.obtenerPrefijoNumero())));
                    enModoNumero = true;
                }
                SimboloBraille simbolo = getSimbolo(caracter);
                if (simbolo != null)
                    resultado.append(convertirABrailleUnicode(simbolo));
            } else if (caracter == ' ') {
                enModoNumero = false;
                resultado.append("\u2800"); // Espacio Braille Unicode
            } else {
                enModoNumero = false;
                if (Character.isUpperCase(caracter)) {
                    if (esPalabraCompletaMayuscula(texto, i)) {
                        // Palabra completa en mayúsculas: agregar prefijo DOS veces al inicio de la
                        // palabra
                        if (i == 0 || !Character.isLetter(texto.charAt(i - 1))) {
                            resultado.append(convertirABrailleUnicode(
                                    new SimboloBraille(diccionario.obtenerPrefijoMayuscula())));
                            resultado.append(convertirABrailleUnicode(
                                    new SimboloBraille(diccionario.obtenerPrefijoMayuscula())));
                        }
                    } else {
                        // Solo la primera letra en mayúscula: un solo prefijo
                        resultado.append(
                                convertirABrailleUnicode(new SimboloBraille(diccionario.obtenerPrefijoMayuscula())));
                    }
                    SimboloBraille simbolo = getSimbolo(Character.toLowerCase(caracter));
                    if (simbolo != null)
                        resultado.append(convertirABrailleUnicode(simbolo));
                } else {
                    SimboloBraille simbolo = getSimbolo(caracter);
                    if (simbolo != null)
                        resultado.append(convertirABrailleUnicode(simbolo));
                    else
                        resultado.append(caracter);
                }
            }
        }
        return resultado.toString();
    }

    /**
     * Traduce texto de Braille Unicode a Español.
     * Solo traduce los caracteres que son Braille Unicode (rango U+2800 a U+28FF).
     * El texto normal en español se OMITE completamente del resultado.
     * 
     * @param textoBraille El texto que puede contener Braille Unicode y texto
     *                     normal.
     * @return Solo el texto traducido del Braille, sin incluir texto en español.
     */
    @Override
    public String traducirBrailleAEspanol(String textoBraille) {
        if (textoBraille == null || textoBraille.isEmpty())
            return "";

        StringBuilder resultado = new StringBuilder();
        boolean siguienteMayuscula = false;
        boolean todasMayusculas = false; // Nueva bandera para palabras completas en mayúsculas
        boolean modoNumero = false;
        boolean enSecuenciaBraille = false;

        for (int i = 0; i < textoBraille.length(); i++) {
            char caracterActual = textoBraille.charAt(i);

            // Mantener saltos de línea solo si vienen de Braille
            if (caracterActual == '\n' || caracterActual == '\r') {
                // Solo agregar saltos de línea si ya hay contenido traducido
                if (resultado.length() > 0) {
                    resultado.append(caracterActual);
                }
                modoNumero = false;
                siguienteMayuscula = false;
                todasMayusculas = false;
                enSecuenciaBraille = false;
                continue;
            }

            // Si es espacio Braille Unicode - traducir a espacio normal
            if (caracterActual == '\u2800') {
                resultado.append(' ');
                modoNumero = false;
                siguienteMayuscula = false;
                todasMayusculas = false; // Terminar modo de todas mayúsculas
                enSecuenciaBraille = true;
                continue;
            }

            // Para espacios normales, verificar si estamos en una secuencia Braille
            if (caracterActual == ' ' || caracterActual == '\t') {
                // Verificar si hay más caracteres Braille adelante
                boolean hayBrailleAdelante = false;
                for (int j = i + 1; j < textoBraille.length(); j++) {
                    char siguienteChar = textoBraille.charAt(j);
                    if (siguienteChar != ' ' && siguienteChar != '\t') {
                        hayBrailleAdelante = esBrailleUnicode(siguienteChar);
                        break;
                    }
                }

                // Si estamos en secuencia Braille y hay más Braille adelante, mantener el
                // espacio
                if (enSecuenciaBraille && hayBrailleAdelante) {
                    resultado.append(' ');
                    modoNumero = false;
                    siguienteMayuscula = false;
                    todasMayusculas = false;
                } else {
                    // Si no hay Braille adelante, terminó la secuencia
                    enSecuenciaBraille = false;
                }
                continue;
            }

            // Solo procesar si es un carácter Braille Unicode
            if (esBrailleUnicode(caracterActual)) {
                enSecuenciaBraille = true;
                String patron = convertirUnicodeBrailleAPatron(caracterActual);
                if (patron.equals("0") || patron.isEmpty()) {
                    resultado.append(' ');
                    modoNumero = false;
                    siguienteMayuscula = false;
                    todasMayusculas = false;
                    continue;
                }

                if (patron.equals(diccionario.obtenerPrefijoMayuscula())) {
                    // Verificar si el siguiente carácter también es prefijo de mayúscula
                    if (i + 1 < textoBraille.length()) {
                        char siguienteCaracter = textoBraille.charAt(i + 1);
                        if (esBrailleUnicode(siguienteCaracter)) {
                            String siguientePatron = convertirUnicodeBrailleAPatron(siguienteCaracter);
                            if (siguientePatron.equals(diccionario.obtenerPrefijoMayuscula())) {
                                // Dos prefijos consecutivos = toda la palabra en mayúsculas
                                todasMayusculas = true;
                                siguienteMayuscula = false;
                                i++; // Saltar el segundo prefijo
                                continue;
                            }
                        }
                    }
                    // Solo un prefijo = siguiente letra en mayúscula
                    siguienteMayuscula = true;
                    continue;
                }

                if (patron.equals(diccionario.obtenerPrefijoNumero())) {
                    modoNumero = true;
                    continue;
                }

                Character encontrado = buscarCaracterPorPatron(patron, modoNumero);

                // Si no se encuentra en modo número, intentar buscar como letra
                if (encontrado == null && modoNumero) {
                    encontrado = buscarCaracterPorPatron(patron, false);
                }

                if (encontrado != null) {
                    if (encontrado == ' ') {
                        modoNumero = false;
                        siguienteMayuscula = false;
                        todasMayusculas = false;
                        resultado.append(' ');
                    } else if (todasMayusculas && Character.isLetter(encontrado)) {
                        // Modo de toda la palabra en mayúsculas
                        encontrado = Character.toUpperCase(encontrado);
                        resultado.append(encontrado);
                        // Mantener todasMayusculas activo hasta encontrar un espacio
                        modoNumero = false;
                    } else if (siguienteMayuscula && Character.isLetter(encontrado)) {
                        encontrado = Character.toUpperCase(encontrado);
                        siguienteMayuscula = false;
                        resultado.append(encontrado);
                        // Si es una letra, salir del modo número
                        if (!Character.isDigit(encontrado)) {
                            modoNumero = false;
                        }
                    } else {
                        resultado.append(encontrado);
                        // Si no es un dígito ni una coma/punto, salir del modo número
                        if (!Character.isDigit(encontrado) && encontrado != ',' && encontrado != '.') {
                            modoNumero = false;
                        }
                        // Si no es letra, desactivar todasMayusculas
                        if (!Character.isLetter(encontrado)) {
                            todasMayusculas = false;
                        }
                    }
                }
                // Si no se encuentra traducción, simplemente se omite (no se añade nada)
            } else {
                // NO es Braille Unicode - termina la secuencia Braille
                enSecuenciaBraille = false;
            }
        }
        return resultado.toString();
    }

    /**
     * Convierte un carácter Braille Unicode a su patrón de puntos.
     * 
     * @param caracterBraille El carácter Braille Unicode.
     * @return El patrón de puntos correspondiente.
     */
    private String convertirUnicodeBrailleAPatron(char caracterBraille) {
        int valor = caracterBraille - 0x2800;

        // Si el valor es 0, es el carácter Braille vacío (espacio)
        if (valor == 0)
            return "0";

        StringBuilder patron = new StringBuilder();
        if ((valor & 0x01) != 0)
            patron.append("1");
        if ((valor & 0x02) != 0)
            patron.append("2");
        if ((valor & 0x04) != 0)
            patron.append("3");
        if ((valor & 0x08) != 0)
            patron.append("4");
        if ((valor & 0x10) != 0)
            patron.append("5");
        if ((valor & 0x20) != 0)
            patron.append("6");

        return patron.toString();
    }

    /**
     * Verifica si un carácter es un carácter Braille Unicode.
     * 
     * @param c El carácter a verificar.
     * @return true si es un carácter Braille Unicode, false en caso contrario.
     */
    private boolean esBrailleUnicode(char c) {
        return c >= '\u2800' && c <= '\u28FF';
    }

    /**
     * Verifica si la palabra completa en la posición dada está en mayúsculas.
     * 
     * @param texto    El texto completo.
     * @param posicion La posición del carácter actual.
     * @return true si la palabra completa está en mayúsculas, false en caso
     *         contrario.
     */
    private boolean esPalabraCompletaMayuscula(String texto, int posicion) {
        int inicio = posicion;
        while (inicio > 0 && Character.isLetter(texto.charAt(inicio - 1)))
            inicio--;
        int fin = posicion;
        while (fin < texto.length() && Character.isLetter(texto.charAt(fin)))
            fin++;
        for (int i = inicio; i < fin; i++) {
            if (Character.isLetter(texto.charAt(i)) && !Character.isUpperCase(texto.charAt(i)))
                return false;
        }
        return (fin - inicio) >= 2;
    }

    /**
     * Convierte un símbolo Braille a su representación Unicode.
     * 
     * @param simbolo El símbolo Braille a convertir.
     * @return El carácter Braille Unicode correspondiente.
     */
    private String convertirABrailleUnicode(SimboloBraille simbolo) {
        int valor = 0x2800;
        if (simbolo.isPuntoActivado(1))
            valor += 0x01;
        if (simbolo.isPuntoActivado(2))
            valor += 0x02;
        if (simbolo.isPuntoActivado(3))
            valor += 0x04;
        if (simbolo.isPuntoActivado(4))
            valor += 0x08;
        if (simbolo.isPuntoActivado(5))
            valor += 0x10;
        if (simbolo.isPuntoActivado(6))
            valor += 0x20;
        return String.valueOf((char) valor);
    }

    /**
     * Obtiene el símbolo Braille correspondiente a un carácter.
     * 
     * @param caracter El carácter a traducir.
     * @return El símbolo Braille correspondiente, o null si no existe.
     */
    public SimboloBraille getSimbolo(char caracter) {
        String patron = diccionario.obtenerPatron(caracter);
        return (patron != null) ? new SimboloBraille(patron) : null;
    }

    /**
     * Busca un carácter en el diccionario por su patrón de puntos.
     * 
     * @param patron     El patrón de puntos a buscar.
     * @param modoNumero Indica si se está en modo número.
     * @return El carácter correspondiente, o null si no se encuentra.
     */
    private Character buscarCaracterPorPatron(String patron, boolean modoNumero) {
        if (patron.equals(" "))
            return ' ';
        if (modoNumero) {
            String[] patronesNumeros = { "1", "12", "14", "145", "15", "124", "1245", "125", "24", "245" };
            for (int i = 0; i < patronesNumeros.length; i++) {
                if (patron.equals(patronesNumeros[i]))
                    return (i == 9) ? '0' : (char) ('1' + i);
            }
        }
        for (char c = 'a'; c <= 'z'; c++) {
            if (patron.equals(diccionario.obtenerPatron(c)))
                return c;
        }
        char[] especiales = { ' ', ',', '.', ';', ':', '?', '!', '"', '(', ')', '-', 'á', 'é', 'í', 'ó', 'ú', 'ü',
                'ñ' };
        for (char c : especiales) {
            if (patron.equals(diccionario.obtenerPatron(c)))
                return c;
        }
        return null;
    }
}