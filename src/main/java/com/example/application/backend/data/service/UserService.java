package com.example.application.backend.data.service;

import com.example.application.backend.data.entity.Status;
import com.example.application.backend.data.entity.Tracking;
import com.example.application.backend.data.entity.User;
import com.example.application.backend.data.exception.ParteNotFoundException;
import com.example.application.backend.data.repository.UserRepository;
import com.example.application.backend.data.util.DateUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.application.backend.data.util.AbstractReport.DEFAULT_FONT_SIZE;
import static com.itextpdf.text.FontFactory.HELVETICA;
import static com.itextpdf.text.PageSize.A4;

@Service
public class UserService implements IUserService{

    private static final Logger log = LoggerFactory.getLogger(BreakServiceImpl.class);
    private final UserRepository repository;
    private Document document;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;

    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public User update(User entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }


    public List<User> orderByUserId() {
        return repository.orderByUserId();
    }

    public LocalDateTime getLastAttendance(User user) {
        User currentUser = null;
        try {

            if (repository.findById(user.getUser_id()).isPresent()) {
                currentUser = repository.findById(user.getUser_id()).get();
            }
            assert currentUser != null;
            if (Status.Entrada.equals(currentUser.getStatus())) {
                if(currentUser.getTrackingList().get(currentUser.getTrackingList().size() - 1).getWorkCheckIn() != null){
                    return currentUser.getTrackingList().get(currentUser.getTrackingList().size() - 1).getWorkCheckIn();
                } else {
                    return null;
                }
            } else if (currentUser.getStatus() == Status.Salida) {
                if(currentUser.getTrackingList().get(currentUser.getTrackingList().size() - 1).getWorkCheckOut() != null){
                    return currentUser.getTrackingList().get(currentUser.getTrackingList().size() - 1).getWorkCheckOut();
                } else {
                    return null;
                }
            } else {
                throw new ParteNotFoundException("No se ha encontrado ningun parte");
            }
        } catch (ParteNotFoundException ex) {
            log.info("El usuario " + currentUser.getName() + "no tiene ninguna fecha de " + currentUser.getStatus(), ex);
        }
        return null;
    }

    public byte[] generatePDF(User user) {
        var border = (float) 1.5;
        try {
            try (var byteArrayOutputStream = new ByteArrayOutputStream()) {
                String nombre = user.getName() + " " + user.getSurname();

                document = new Document(A4, border, border, border, border);
                document.open();
                var pdfWriter = PdfWriter.getInstance(document, byteArrayOutputStream);
                document.open();
                document.newPage();

                Paragraph paragraph = new Paragraph("Reporte de " + nombre, FontFactory.getFont(HELVETICA, DEFAULT_FONT_SIZE, Font.BOLD));
                paragraph.setSpacingAfter(48f);
                document.add(paragraph);
                //Contenido
                PdfPTable table = new PdfPTable(3); // 3 columns.
                table.setSpacingBefore(48f);
                List<Tracking> trackings = user.getTrackingList();
                var cellHoraEntrada = new PdfPCell(new Phrase("Hora de entrada", FontFactory.getFont(HELVETICA, DEFAULT_FONT_SIZE)));
                var cellHoraSalida = new PdfPCell(new Phrase("Hora de salida", FontFactory.getFont(HELVETICA, DEFAULT_FONT_SIZE)));
                var cellHorasTrabajadas = new PdfPCell(new Phrase("Horas trabajadas", FontFactory.getFont(HELVETICA, DEFAULT_FONT_SIZE)));
                table.addCell(cellHoraEntrada);
                table.addCell(cellHoraSalida);
                table.addCell(cellHorasTrabajadas);
                for (Tracking tracking : trackings) {
                    if (tracking.getWorkCheckIn() == null) {
                        table.addCell("No ha fichado");
                    } else {
                        table.addCell(new Phrase(DateUtil.formatDate(tracking.getWorkCheckInIfNull()), FontFactory.getFont(HELVETICA, DEFAULT_FONT_SIZE)));
                    }
                    if (tracking.getWorkCheckOut() == null) {
                        table.addCell("No ha fichado");
                    } else {
                        table.addCell(new Phrase(DateUtil.formatDate(tracking.getWorkCheckOutIfNull()), FontFactory.getFont(HELVETICA, DEFAULT_FONT_SIZE)));
                    }

                    if (tracking.getTotalWorkTime() == null) {
                        table.addCell("Error de cálculo");
                    } else {
                        table.addCell(new Phrase(tracking.getTotalWorkTime(), FontFactory.getFont(HELVETICA, DEFAULT_FONT_SIZE)));
                    }
                }

                document.add(table);
                document.close();
                pdfWriter.flush();
                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            return new byte[0];
        } finally {
            log.info("Se ha generado el PDF del usuario " + user.getName());

        }
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}