package com.traductor.service;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.IOException;

public class GeneradorPDF {

    private static final float MM_TO_POINTS = 2.834645f;
    private static final float ALTO_CELDA_MM = 10.0f;
    private static final float ESPACIADO_ENTRE_CARACTERES_MM = 6.0f;
    private static final float ESPACIADO_ENTRE_LINEAS_MM = 15.0f;
    private static final float TAMANO_FUENTE_BRAILLE = ALTO_CELDA_MM * MM_TO_POINTS * 1.2f;

    public static void generarPDF(String textoOriginal, String textoBraille, String rutaDestino, boolean modoEspejo) throws IOException {
        File archivo = new File(rutaDestino);
        PdfWriter writer = new PdfWriter(archivo);
        PdfDocument pdf = new PdfDocument(writer);
        
        // Usamos un Document normal para manejar el flujo de texto
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(40, 40, 40, 40);

        try {
            PdfFont fontBold = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);

            // 1. DIBUJAR ESPAÑOL (Siempre normal, incluso en modo espejo)
            document.add(new Paragraph(textoOriginal)
                    .setFont(fontBold)
                    .setFontSize(48)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            if (modoEspejo) {
                // 2. DIBUJAR BRAILLE CON ESPEJO (Solo afecta al bloque Braille)
                agregarBrailleEspejo(pdf, document, textoBraille);
            } else {
                // 2. DIBUJAR BRAILLE NORMAL
                agregarTextoBraille(document, textoBraille);
            }

        } finally {
            document.close();
        }
    }

    private static void agregarBrailleEspejo(PdfDocument pdf, Document document, String textoBraille) throws IOException {
        PdfPage page = pdf.getPage(pdf.getNumberOfPages());
        PdfCanvas pdfCanvas = new PdfCanvas(page);
        float pageWidth = page.getPageSize().getWidth();
        
        // Calculamos la posición Y actual para que el Braille aparezca debajo del título
        // Ajustamos el valor -50 para que no quede muy separado
        float yPos = document.getRenderer().getCurrentArea().getBBox().getTop() - 45;

        // Guardamos el estado del canvas para no afectar a futuros elementos
        pdfCanvas.saveState();
        
        // Aplicamos la matriz de reflexión horizontal solo a este bloque
        AffineTransform transform = new AffineTransform(-1.0, 0.0, 0.0, 1.0, pageWidth, 0.0);
        pdfCanvas.concatMatrix(transform);
        
        // Creamos un canvas temporal con la matriz invertida
        Canvas canvas = new Canvas(pdfCanvas, page.getPageSize());
        PdfFont fontBraille = cargarFuenteBraille();
        
        // El Braille ahora saldrá con los puntos invertidos y en orden de derecha a izquierda (para regleta)
        Paragraph p = crearParrafoBraille(textoBraille, fontBraille)
                .setTextAlignment(TextAlignment.CENTER)
                .setFixedPosition(0, yPos, pageWidth);
        
        canvas.add(p);
        
        // Restauramos el canvas a su estado normal
        pdfCanvas.restoreState();
        canvas.close();
    }

    private static void agregarTextoBraille(Document document, String textoBraille) throws IOException {
        PdfFont fontBraille = cargarFuenteBraille();
        document.add(crearParrafoBraille(textoBraille, fontBraille).setTextAlignment(TextAlignment.CENTER));
    }

    private static PdfFont cargarFuenteBraille() throws IOException {
        String[] fuentes = {
            "C:/Windows/Fonts/seguisym.ttf",
            "C:/Windows/Fonts/DejaVuSans.ttf",
            "C:/Windows/Fonts/arialuni.ttf"
        };

        for (String ruta : fuentes) {
            File f = new File(ruta);
            if (f.exists()) {
                return PdfFontFactory.createFont(ruta, PdfEncodings.IDENTITY_H);
            }
        }
        return PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);
    }

    private static Paragraph crearParrafoBraille(String texto, PdfFont font) {
        return new Paragraph(texto)
                .setFont(font)
                .setFontSize(TAMANO_FUENTE_BRAILLE)
                .setFixedLeading(ESPACIADO_ENTRE_LINEAS_MM * MM_TO_POINTS)
                .setCharacterSpacing(ESPACIADO_ENTRE_CARACTERES_MM * MM_TO_POINTS * 0.15f);
    }
}