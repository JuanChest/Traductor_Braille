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

/**
 * GeneradorPDF es una clase que proporciona funcionalidades para generar documentos PDF
 * que contienen texto en español y su correspondiente representación en Braille.
 */
public class GeneradorPDF {

    private static final float MM_TO_POINTS = 2.834645f;
    private static final float ALTO_CELDA_MM = 10.0f;
    private static final float ESPACIADO_ENTRE_CARACTERES_MM = 6.0f;
    private static final float ESPACIADO_ENTRE_LINEAS_MM = 15.0f;
    private static final float TAMANO_FUENTE_BRAILLE = ALTO_CELDA_MM * MM_TO_POINTS * 1.2f;

    /**
     * Genera un documento PDF con el texto original y su representación en Braille.
     * @param textoOriginal Texto en español que se desea incluir en el PDF.
     * @param textoBraille Representación en Braille del texto original.
     * @param rutaDestino Ruta donde se guardará el archivo PDF generado.
     * @param modoEspejo Indica si el contenido debe generarse en modo espejo.
     * @throws IOException Si ocurre un error al crear o escribir el archivo PDF.
     */
    public static void generarPDF(String textoOriginal, String textoBraille, String rutaDestino, boolean modoEspejo) throws IOException {
        File archivo = new File(rutaDestino);
        PdfWriter writer = new PdfWriter(archivo);
        PdfDocument pdf = new PdfDocument(writer);
        
        // Documento base A4
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(40, 40, 40, 40);

        try {
            if (modoEspejo) {
                // En modo espejo, manejamos tanto el Español como el Braille dentro del canvas transformado
                generarContenidoEspejo(pdf, textoOriginal, textoBraille);
            } else {
                // Modo normal
                PdfFont fontBold = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
                
                // 1. Español Normal
                document.add(new Paragraph(textoOriginal)
                        .setFont(fontBold)
                        .setFontSize(48)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginBottom(20));

                // 2. Braille Normal
                agregarTextoBraille(document, textoBraille);
            }

        } finally {
            document.close();
        }
    }

    /**
     * Genera el contenido del PDF en modo espejo.
     * @param pdf PdfDocument donde se añadirá el contenido.
     * @param textoOriginal Texto en español que se desea incluir en el PDF.
     * @param textoBraille Representación en Braille del texto original.
     * @throws IOException Si ocurre un error al crear o escribir el contenido del PDF.
     */
    private static void generarContenidoEspejo(PdfDocument pdf, String textoOriginal, String textoBraille) throws IOException {
        PdfPage page = pdf.addNewPage();
        PdfCanvas pdfCanvas = new PdfCanvas(page);
        float pageWidth = page.getPageSize().getWidth();
        float pageHeight = page.getPageSize().getHeight();

        // 1. Preparar Transformación de Espejo
        pdfCanvas.saveState();
        // Matriz de reflexión horizontal: escala x = -1, traslación x = pageWidth
        AffineTransform transform = new AffineTransform(-1.0, 0.0, 0.0, 1.0, pageWidth, 0.0);
        pdfCanvas.concatMatrix(transform);

        // 2. Crear un Canvas para añadir elementos de alto nivel (Paragraphs)
        Canvas canvas = new Canvas(pdfCanvas, page.getPageSize());
        
        // --- DIBUJAR ESPAÑOL (ESPEJO) ---
        PdfFont fontBold = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
        Paragraph pOriginal = new Paragraph(textoOriginal)
                .setFont(fontBold)
                .setFontSize(48)
                .setTextAlignment(TextAlignment.CENTER);
        
        // Posicionamos el español en la parte superior
        canvas.showTextAligned(pOriginal, pageWidth / 2, pageHeight - 100, TextAlignment.CENTER);

        // --- DIBUJAR BRAILLE (ESPEJO) ---
        PdfFont fontBraille = cargarFuenteBraille();
        Paragraph pBraille = crearParrafoBraille(textoBraille, fontBraille)
                .setTextAlignment(TextAlignment.CENTER);
        
        // Posicionamos el braille debajo del texto original
        canvas.showTextAligned(pBraille, pageWidth / 2, pageHeight - 200, TextAlignment.CENTER);

        // 3. Limpiar y restaurar
        pdfCanvas.restoreState();
        canvas.close();
    }

    /**
     * Agrega el texto en Braille al documento.
     * @param document Documento donde se añadirá el texto en Braille.
     * @param textoBraille Representación en Braille del texto original.
     * @throws IOException Si ocurre un error al cargar la fuente Braille.
     */
    private static void agregarTextoBraille(Document document, String textoBraille) throws IOException {
        PdfFont fontBraille = cargarFuenteBraille();
        document.add(crearParrafoBraille(textoBraille, fontBraille).setTextAlignment(TextAlignment.CENTER));
    }

    /**
     * Carga una fuente que soporte caracteres Braille.
     * @return PdfFont que soporta Braille.
     * @throws IOException Si ocurre un error al cargar la fuente.
     */
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
    
    /**
     * Crea un párrafo formateado para texto en Braille.
     * @param texto Texto en Braille.
     * @param font Fuente que soporta Braille.
     * @return Paragraph formateado para Braille.
     */
    private static Paragraph crearParrafoBraille(String texto, PdfFont font) {
        return new Paragraph(texto)
                .setFont(font)
                .setFontSize(TAMANO_FUENTE_BRAILLE)
                .setFixedLeading(ESPACIADO_ENTRE_LINEAS_MM * MM_TO_POINTS)
                .setCharacterSpacing(ESPACIADO_ENTRE_CARACTERES_MM * MM_TO_POINTS * 0.15f);
    }
}