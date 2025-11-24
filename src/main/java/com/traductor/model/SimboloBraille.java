package com.traductor.model;

/**
 * Representa un símbolo en Braille.
 *
 * @see DiccionarioBraille
 */
public class SimboloBraille {
    private boolean[] points;
    
    /**
     * Inicializa el símbolo Braille con un código.
     *
     * @param brailleCode El código del símbolo en Braille.
     */
    public SimboloBraille(String brailleCode) {
        this.points = new boolean[6];
        convertirCodigo(brailleCode);
    }
    
    /**
     * Obtiene el patrón de puntos del símbolo.
     *
     * @return Cadena con los números de los puntos activos.
     */
    public String getPatternCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < points.length; i++) {
            if (points[i]) {
                code.append(i + 1);
            }
        }
        return code.toString();
    }
    
    /**
     * Verifica si un punto específico está activo.
     *
     * @param point El número del punto a verificar.
     * @return true si el punto está activo, false en caso contrario.
     */
    public boolean isPoint(int point) {
        if (point >= 1 && point <= 6) {
            return points[point - 1];
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
                    points[pos - 1] = true;
                }
            }
        }
    }
}
