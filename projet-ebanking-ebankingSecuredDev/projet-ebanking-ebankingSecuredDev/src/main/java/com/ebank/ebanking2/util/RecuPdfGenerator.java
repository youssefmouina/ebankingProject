package com.ebank.ebanking2.util;

import com.ebank.ebanking2.model.entity.Virement;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class RecuPdfGenerator {

    public byte[] generate(Virement virement) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            Paragraph title = new Paragraph("Reçu de Virement", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'à' HH:mm", Locale.FRENCH);
            String formattedDate = virement.getCreatedAt().format(formatter);
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{1, 2});
            addTableRow(table, "ID Virement:", String.valueOf(virement.getId()), bodyFont);
            addTableRow(table, "Date:", formattedDate, bodyFont);
            addTableRow(table, "Montant:", virement.getMontant() + " $", bodyFont);
            addTableRow(table, "Type:", virement.getType().name(), bodyFont);
            addTableRow(table, "Compte Émetteur:", virement.getCompteEmetteur().getRib(), bodyFont);
            addTableRow(table, "Compte Récepteur:", virement.getCompteRecepteur().getRib(), bodyFont);
            document.add(table);
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
        return baos.toByteArray();
    }

    private void addTableRow(PdfPTable table, String label, String value, Font font) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, font));
        PdfPCell cell2 = new PdfPCell(new Phrase(value, font));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
    }
}
