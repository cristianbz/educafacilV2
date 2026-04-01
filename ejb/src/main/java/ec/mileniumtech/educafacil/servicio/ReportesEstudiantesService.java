package ec.mileniumtech.educafacil.servicio;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;

import ec.mileniumtech.educafacil.modelo.persistencia.entity.Matricula;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumEstadosMatricula;
import ec.mileniumtech.educafacil.utilitarios.fechas.FechaFormato;
import jakarta.ejb.Stateless;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Servicio para encapsular la logica de generacion de PDFs (certificados, registros),
 * CSVs para Moodle, y reportes de JasperReports.
 */
@Stateless
public class ReportesEstudiantesService {

    private static final Logger log = Logger.getLogger(ReportesEstudiantesService.class);

    /**
     * Genera el PDF del registro de matricula / inscripcion.
     */
    public byte[] generarRegistroMatriculaPdf(Matricula matricula, String confEmpresaNombre, String logoCeimscapUrl, String logoQrPath) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(null);

        document.setPageSize(PageSize.A4);
        document.setMargins(35, 35, 35, 35);
        document.setMarginMirroring(true);

        PdfWriter writer = PdfWriter.getInstance(document, baos);
        Rectangle rect = new Rectangle(100, 30, 500, 800);
        writer.setBoxSize("art", rect);

        document.open();

        Font fontTitulos = FontFactory.getFont(FontFactory.HELVETICA_BOLD.toString(), 11);
        Font fontCabecera = new Font(FontFamily.HELVETICA, 15, Font.BOLD, BaseColor.BLACK);
        Font fontContenidoTablas = new Font(FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);

        PdfPTable tablaCabecera = new PdfPTable(1);
        Image logotipo = Image.getInstance(logoCeimscapUrl);
        logotipo.scalePercent(15);
        logotipo.setAlignment(Element.ALIGN_MIDDLE);
        PdfPCell cell = new PdfPCell(logotipo);
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        tablaCabecera.addCell(cell);                
        tablaCabecera.setWidthPercentage(150f);
        document.add(tablaCabecera);
        
        Paragraph parrafoHoja = new Paragraph();
        for (int i=0; i<3; i++) parrafoHoja.add(new Phrase(Chunk.NEWLINE));
        
        parrafoHoja.add(new Phrase(confEmpresaNombre, fontCabecera));
        parrafoHoja.setAlignment(Element.ALIGN_CENTER);
        parrafoHoja.add(new Phrase(Chunk.NEWLINE));
        parrafoHoja.add(new Phrase(Chunk.NEWLINE));
        parrafoHoja.add(new Phrase("REGISTRO DE INSCRIPCION / MATRICULA", fontTitulos));
        parrafoHoja.setAlignment(Element.ALIGN_CENTER);
        for (int i=0; i<3; i++) parrafoHoja.add(new Phrase(Chunk.NEWLINE));
        document.add(parrafoHoja);

        Paragraph parrafoInscripcion = new Paragraph();
        if(EnumEstadosMatricula.INSCRITO.getCodigo().equals(matricula.getMatrEstado()))
            parrafoInscripcion.add(new Phrase("Datos de la inscripcion:", fontTitulos));
        else
            parrafoInscripcion.add(new Phrase("Datos de la matricula:", fontTitulos));
        for (int i=0; i<3; i++) parrafoInscripcion.add(new Phrase(Chunk.NEWLINE));

        document.add(parrafoInscripcion);
        PdfPTable tablaDatosPersonales = new PdfPTable(2);
        
        agregarCeldaPares(tablaDatosPersonales, "APELLIDOS Y NOMBRES:", matricula.getEstudiante().getPersona().getPersApellidos() + " " + matricula.getEstudiante().getPersona().getPersNombres(), fontContenidoTablas);
        agregarCeldaPares(tablaDatosPersonales, "FECHA INSCRIPCION:", matricula.getMatrFechaInscripcion() != null ? FechaFormato.cambiarFormato(matricula.getMatrFechaInscripcion(),"yyyy-MM-dd") : "", fontContenidoTablas);
        agregarCeldaPares(tablaDatosPersonales, "FECHA MATRICULA:", matricula.getMatrFechaMatricula() != null ? FechaFormato.cambiarFormato(matricula.getMatrFechaMatricula(),"yyyy-MM-dd") : "", fontContenidoTablas);
        agregarCeldaPares(tablaDatosPersonales, "CURSO:", matricula.getOfertaCursos().getOfertaCapacitacion().getCurso().getCursNombre(), fontContenidoTablas);
        agregarCeldaPares(tablaDatosPersonales, "INSTRUCTOR:", matricula.getOfertaCursos().getInstructor().getPersona().getPersApellidos() + " " + matricula.getOfertaCursos().getInstructor().getPersona().getPersNombres(), fontContenidoTablas);
        agregarCeldaPares(tablaDatosPersonales, "FECHA DE INICIO:", FechaFormato.cambiarFormato(matricula.getOfertaCursos().getOcurFechaInicio(),"yyyy-MM-dd"), fontContenidoTablas);
        agregarCeldaPares(tablaDatosPersonales, "FECHA DE FIN:", FechaFormato.cambiarFormato(matricula.getOfertaCursos().getOcurFechaFin(),"yyyy-MM-dd"), fontContenidoTablas);

        String mensajeQR = matricula.getEstudiante().getPersona().getPersApellidos() + " " + 
                           matricula.getEstudiante().getPersona().getPersNombres() + " " + 
                           matricula.getOfertaCursos().getOfertaCapacitacion().getCurso().getCursNombre() + " " + 
                           FechaFormato.cambiarFormato(matricula.getMatrFechaRegistro(),"yyyy-MM-dd") + " " + 
                           matricula.getMatrId();                    

        byte[] pngData = getQRCodeImage(mensajeQR, 80, 80, logoQrPath);

        tablaDatosPersonales.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tablaDatosPersonales.setWidthPercentage(70f);
        document.add(tablaDatosPersonales);
        
        Paragraph parrafo = new Paragraph();
        parrafo.add(new Phrase(Chunk.NEWLINE));
        parrafo.add(new Phrase(Chunk.NEWLINE));
        document.add(parrafo);
        
        PdfPTable tablaQr = new PdfPTable(1);
        Image logotipoQr = Image.getInstance(pngData);
        logotipoQr.scalePercent(150);
        logotipoQr.setAlignment(Element.ALIGN_MIDDLE);
        PdfPCell celda = new PdfPCell(logotipoQr);
        celda.setColspan(1);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setBorder(PdfPCell.NO_BORDER);
        tablaQr.addCell(celda);                
        tablaQr.setWidthPercentage(150f);
        document.add(tablaQr);
        
        document.close();
        return baos.toByteArray();
    }

    private void agregarCeldaPares(PdfPTable tabla, String label, String valor, Font font) {
        PdfPCell c1 = new PdfPCell(new Phrase(label, font));
        c1.setBorder(PdfPCell.NO_BORDER);
        tabla.addCell(c1);
        PdfPCell c2 = new PdfPCell(new Phrase(valor, font));
        c2.setBorder(PdfPCell.NO_BORDER);
        tabla.addCell(c2);
    }

    /**
     * Genera el certificado en formato byte[]
     */
    public byte[] generarCertificadoSoporteDigitalPdf(Matricula matricula, String certificadoBgUrl, String logoQrPath) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(null);

        document.setPageSize(PageSize.A4.rotate());
        document.setMargins(35, 35, 35, 35);
        document.setMarginMirroring(true);

        PdfWriter writer = PdfWriter.getInstance(document, baos);
        Rectangle rect = new Rectangle(100, 30, 500, 800);
        writer.setBoxSize("art", rect);

        document.open();

        PdfPTable tablaCabecera = new PdfPTable(1);
        Image logotipo = Image.getInstance(certificadoBgUrl);
        logotipo.scalePercent(100);
        logotipo.setAlignment(Element.ALIGN_MIDDLE);
        PdfPCell cell = new PdfPCell(logotipo);
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        tablaCabecera.addCell(cell);                
        tablaCabecera.setWidthPercentage(150f);
        document.add(tablaCabecera);
        
        String mensajeQR = matricula.getEstudiante().getPersona().getPersApellidos() + " " + 
                           matricula.getEstudiante().getPersona().getPersNombres() + " " + 
                           matricula.getOfertaCursos().getOfertaCapacitacion().getCurso().getCursNombre() + " " + 
                           FechaFormato.cambiarFormato(matricula.getMatrFechaRegistro(),"yyyy-MM-dd") + " " + 
                           matricula.getMatrId();                    
        byte[] pngData = getQRCodeImage(mensajeQR, 100, 100, logoQrPath);

        PdfContentByte capaNombres = writer.getDirectContent();
        capaNombres.saveState();
        
        Image logotipoQr = Image.getInstance(pngData);
        logotipoQr.scalePercent(150);
        logotipoQr.setAlignment(Element.ALIGN_MIDDLE);
        logotipoQr.setAbsolutePosition(650f, 0f);
        capaNombres.addImage(logotipoQr);
        
        BaseFont bf = BaseFont.createFont();
        capaNombres.beginText();
        capaNombres.setFontAndSize(bf, 20);
        capaNombres.setTextMatrix((float)1, (float)0, (float) 0,2, 285, 285);
        capaNombres.showText(matricula.getEstudiante().getPersona().getPersApellidos() + " " + matricula.getEstudiante().getPersona().getPersNombres());
        capaNombres.endText();
        capaNombres.restoreState();

        PdfContentByte capaCurso = writer.getDirectContent();
        capaCurso.saveState();     
        capaCurso.beginText();
        capaCurso.setFontAndSize(bf, 20);
        capaCurso.setTextMatrix((float)1, (float)0, (float) 0,2, 250, 210);
        capaCurso.showText(matricula.getOfertaCursos().getOfertaCapacitacion().getCurso().getCursNombre());
        capaCurso.endText();
        capaCurso.restoreState();
     
        PdfContentByte capaDuracion = writer.getDirectContent();
        capaDuracion.saveState();     
        capaDuracion.beginText();
        capaDuracion.setFontAndSize(bf, 12);
        capaDuracion.setTextMatrix((float)1, (float)0, (float) 0,2, 380, 170);
        capaDuracion.showText(matricula.getOfertaCursos().getOcurDuracion() + " horas");
        capaDuracion.endText();
        capaDuracion.restoreState();
        
        PdfContentByte capaMes = writer.getDirectContent();
        capaMes.saveState();     
        Calendar fecha = Calendar.getInstance();                   
        fecha.setTime(matricula.getOfertaCursos().getOcurFechaInicio());
        Calendar fechaFin = Calendar.getInstance();                        
        fechaFin.setTime(matricula.getOfertaCursos().getOcurFechaFin());
        
        capaMes.beginText();
        capaMes.setFontAndSize(bf, 12);
        capaMes.setTextMatrix((float)1, (float)0, (float) 0,2, 580, 190);
        if(fecha.get(Calendar.MONTH) != fechaFin.get(Calendar.MONTH))
            capaMes.showText(obtieneMes(fecha.get(Calendar.MONTH)) + " - " + obtieneMes(fechaFin.get(Calendar.MONTH)));
        else
            capaMes.showText(obtieneMes(fecha.get(Calendar.MONTH)));
        capaMes.endText();
        capaMes.restoreState();
        
        PdfContentByte capaFecha = writer.getDirectContent();
        capaFecha.saveState();     
        capaFecha.beginText();
        capaFecha.setFontAndSize(bf, 8);
        capaFecha.setTextMatrix((float)1, (float)0, (float) 0,2, 665, 150);
        capaFecha.showText("Quito, " + FechaFormato.cambiarFormato(matricula.getOfertaCursos().getOcurFechaFin(),"yyyy-MM-dd"));
        capaFecha.endText();
        capaFecha.restoreState();
        
        document.close();
        return baos.toByteArray();
    }

    /**
     * Genera el CSV para Moodle
     */
    public byte[] generarCsvUsuariosMoodle(List<Matricula> matriculas) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(baos);
        CSVWriter writer = new CSVWriter(osw);
        
        String[] cabecera = {"username", "firstname", "lastname", "email"};
        writer.writeNext(cabecera, false);
        
        for (Matricula matricula : matriculas) {
            String[] line = {
                matricula.getEstudiante().getPersona().getPersDocumentoIdentidad(),
                matricula.getEstudiante().getPersona().getPersNombres(),
                matricula.getEstudiante().getPersona().getPersApellidos(),
                matricula.getEstudiante().getPersona().getPersCorreoElectronico()
            };
            writer.writeNext(line, false);
        }
        writer.flush();
        writer.close();
        
        return baos.toByteArray();
    }

    /**
     * Rellena el reporte Jasper y devuelve el PDF como byte array.
     */
    public byte[] generarReporteJasper(List<Matricula> matriculas, String infoReporte, InputStream jasperInputStream) throws Exception {
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperInputStream);
        
        Map<String, Object> params = new HashMap<>();
        params.put("parametroCurso", infoReporte);
        
        JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(matriculas);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, datasource);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
        
        return baos.toByteArray();
    }

    /**
     * Genera el codigo QR en memoria.
     */
    private byte[] getQRCodeImage(String text, int width, int height, String logoQR) throws WriterException, IOException {
        final Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF8");
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        byte[] pngData = pngOutputStream.toByteArray();
        if (logoQR != null && !logoQR.isEmpty()) {
            File logoFile = new File(logoQR);
            if(logoFile.exists()) {
                InputStream in = new ByteArrayInputStream(pngData);
                BufferedImage qrImage = ImageIO.read(in);
                BufferedImage overly = ImageIO.read(logoFile);
                int deltaHeight = qrImage.getHeight() - overly.getHeight();
                int deltaWidth = qrImage.getWidth() - overly.getWidth();
                BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) combined.getGraphics();
                g.drawImage(qrImage, 0, 0, null);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                g.drawImage(overly, (int) Math.round(deltaWidth / 2.0), (int) Math.round(deltaHeight / 2.0), null);
                
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(combined, "png", os);
                return os.toByteArray();
            }
        }
        return pngData;
    }

    private String obtieneMes(int numeromes) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
                          "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return (numeromes >= 0 && numeromes < 12) ? meses[numeromes] : "";
    }
}
