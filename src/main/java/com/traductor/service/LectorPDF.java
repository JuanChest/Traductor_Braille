package com.traductor.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

import java.io.IOException;

/**
 * Servicio para leer archivos PDF y extraer su contenido como texto.
 */
public class LectorPDF {
    
    /**
     * Lee el contenido de un archivo PDF y lo devuelve como texto.
     *
     * @param rutaPDF La ruta del archivo PDF a leer.
     * @return El texto extraído del PDF.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public static String leerPDF(String rutaPDF) throws IOException {
        StringBuilder textoExtraido = new StringBuilder();
        
        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(rutaPDF))) {
            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                // LocationTextExtractionStrategy es fundamental para detectar 
                // el espacio físico entre caracteres Unicode.
                LocationTextExtractionStrategy estrategia = new LocationTextExtractionStrategy();
                String textoPagina = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i), estrategia);
                
                if (textoPagina != null) {
                    textoExtraido.append(textoPagina);
                }
                
                if (i < pdfDoc.getNumberOfPages()) {
                    textoExtraido.append("\n");
                }
            }
        }
        // Normaliza los espacios: si iText capturó múltiples espacios, los dejamos como uno solo.
        return textoExtraido.toString();
    }
}