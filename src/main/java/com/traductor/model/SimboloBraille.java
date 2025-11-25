package com.traductor.model;

/**
 * Representa un símbolo en Braille.
 *
 * @see DiccionarioBraille
 */
public class SimboloBraille {
    private boolean[] puntos;
    
    /**
     * Inicializa el símbolo Braille con un código.
     *
     * @param brailleCode El código del símbolo en Braille.
     */
    public SimboloBraille(String brailleCode) {
        this.puntos = new boolean[6];
        convertirCodigo(brailleCode);
    }
    
    /**
     * Obtiene el patrón de puntos del símbolo.
     *
     * @return Cadena con los números de los puntos activos.
     */
    public String obtenerCodigoPatron() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < puntos.length; i++) {
            if (puntos[i]) {
                code.append(i + 1);
            }
        }
        return code.toString();
    }
    
    /**
     * Verifica si un punto específico está activo.
     *
     * @param punto El número del punto a verificar.
     * @return true si el punto está activo, false en caso contrario.
     */
    public boolean isPuntoActivado(int punto) {
        if (punto >= 1 && punto <= 6) {
            return puntos[punto - 1];
        }
        return false;
    }
    
    /**
     * Convierte un código de Braille en el patrón de puntos.
     *
     * @param brailleCode El código a convertir.
     */
    private void convertirCodigo(String brailleCode) {
        for (char c : brailleCode.toCharArray()) {
            if (Character.isDigit(c)) {
                int pos = Character.getNumericValue(c);
                if (pos >= 1 && pos <= 6) {
                    puntos[pos - 1] = true;
                }
            }
        }
    }
}
