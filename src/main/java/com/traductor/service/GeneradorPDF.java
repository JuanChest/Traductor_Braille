package com.traductor.util;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.IOException;

/**
 * Clase encargada de generar archivos PDF con texto en Braille.
 * Genera un PDF simple con el texto original y su traducción a Braille.
 * Utiliza el estándar Marburg Medium (DIN 32976) para las dimensiones del Braille.
 */
public class GeneradorPDF {
    
    // Constantes del estándar Marburg Medium (DIN 32976)
    private static final float MM_TO_POINTS = 2.834645f;
    
    // Dimensiones de la celda
    private static final float ALTO_CELDA_MM = 10.0f;
    
    // Espaciado entre celdas/caracteres
    private static final float ESPACIADO_ENTRE_CARACTERES_MM = 6.0f;
    private static final float ESPACIADO_ENTRE_LINEAS_MM = 15.0f; // Aumentado a 15mm para mayor separación
    
    // Tamaño de fuente calculado para Marburg Medium
    private static final float TAMANO_FUENTE_BRAILLE = ALTO_CELDA_MM * MM_TO_POINTS * 1.2f;
    
    /**
     * Genera un archivo PDF con el texto original y su traducción a Braille.
     *
     * @param textoOriginal El texto original en español
     * @param textoBraille El texto traducido a símbolos Braille
     * @param rutaDestino La ruta donde se guardará el archivo PDF
     * @throws IOException Si ocurre un error al crear o escribir el archivo
     */
    public static void generarPDF(String textoOriginal, String textoBraille, String rutaDestino) throws IOException {
        File archivo = new File(rutaDestino);
        
        // Crear el documento PDF
        PdfWriter writer = new PdfWriter(archivo);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(40, 40, 40, 40);
        
        try {
            PdfFont fontBold = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
            
            // Texto original - Sans Serif, negrita y centrado
            Paragraph textoOrig = new Paragraph(textoOriginal)
                    .setFont(fontBold)
                    .setFontSize(48)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(30);
            document.add(textoOrig);
            
            // Texto Braille - se intenta con fuentes del sistema que soporten Unicode
            boolean brailleAgregado = false;
            
            // Se intenta con diferentes fuentes del sistema que podrían soportar Braille
            String[] fuentesParaProbar = {
                "C:/Windows/Fonts/seguisym.ttf",  // Segoe UI Symbol
                "C:/Windows/Fonts/arial.ttf",      // Arial
                "C:/Windows/Fonts/arialuni.ttf",   // Arial Unicode MS
                "C:/Windows/Fonts/DejaVuSans.ttf"  // DejaVu Sans
            };
            
            for (String rutaFuente : fuentesParaProbar) {
                try {
                    File archivoFuente = new File(rutaFuente);
                    if (archivoFuente.exists()) {
                        PdfFont fontBraille = PdfFontFactory.createFont(rutaFuente, PdfEncodings.IDENTITY_H, 
                                                                        PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                        
                        // Aplicar estándar Marburg Medium
                        Paragraph textoBrailleParrafo = new Paragraph(textoBraille)
                                .setFont(fontBraille)
                                .setFontSize(TAMANO_FUENTE_BRAILLE)
                                .setFixedLeading(ESPACIADO_ENTRE_LINEAS_MM * MM_TO_POINTS)
                                .setTextAlignment(TextAlignment.CENTER)
                                .setCharacterSpacing(ESPACIADO_ENTRE_CARACTERES_MM * MM_TO_POINTS * 0.15f);
                        document.add(textoBrailleParrafo);
                        brailleAgregado = true;
                        break;
                    }
                } catch (Exception e) {
                    // Continuar con la siguiente fuente
                    continue;
                }
            }
            
            // Si ninguna fuente funcionó, se usa la fuente normal con estándar Marburg
            if (!brailleAgregado) {
                Paragraph textoBrailleParrafo = new Paragraph(textoBraille)
                        .setFont(fontBold)
                        .setFontSize(TAMANO_FUENTE_BRAILLE)
                        .setFixedLeading(ESPACIADO_ENTRE_LINEAS_MM * MM_TO_POINTS)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setCharacterSpacing(ESPACIADO_ENTRE_CARACTERES_MM * MM_TO_POINTS * 0.15f);
                document.add(textoBrailleParrafo);
            }
            
        } finally {
            document.close();
        }
    }
}
