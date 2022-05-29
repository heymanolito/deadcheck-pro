package com.example.application.backend.data.service;

import com.example.application.backend.data.entity.Tracking;
import com.example.application.backend.data.entity.User;
import com.example.application.backend.data.util.AbstractReport;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;

import static com.itextpdf.text.PageSize.A4;

public class ReportService extends AbstractReport {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    private Document document;

    public ReportService(Locale locale) {
        super(locale);
    }

    public void createTrackingTable(User user) {

        document = new Document(A4);
        document.open();
        PdfPTable table = new PdfPTable(3);
        String nombre = user.getName() + " " + user.getSurname();
        List<Tracking> trackings = user.getTrackingList();
        addCell(table, "Hora de entrada", 8);
        addCell(table, "Hora de salida", 8);
        addCell(table, "Horas trabajadas", 8);
        addCell(table, nombre, 8);
        for (Tracking tracking : trackings) {
            addCell(table, tracking.getWorkCheckIn().toString(), 8);
            addCell(table, tracking.getWorkCheckOut().toString(), 8);
            addCell(table, tracking.getTotalWorkTime() + " horas trabajadas", 8);
        }
        document.close();

    }


}
