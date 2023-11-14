package de.hsos.swa.project.fieldbet.shared.boundary.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.quarkus.qute.TemplateExtension;

/**
 * TemplateExtensions
 * 
 * @author Patrick Felschen
 */
@TemplateExtension
public class TemplateExtensions {

    /**
     * Formatiert ein DateTime in eine anzeigbare Zeichenkette um.
     * 
     * @param dateTime Objekt, welches formatiert werden soll
     * @return formatierte Zeichenkette
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return dateTime.format(formatter);
    }

    /**
     * Prueft ob eine Zeitangabe in der Zukunft liegt
     * 
     * @param dateTime Objekt, welches geprueft werden soll
     * @return true, wenn dateTime in Zukunft ist
     */
    public static boolean isInFuture(LocalDateTime dateTime) {
        return dateTime.isAfter(LocalDateTime.now());
    }
}
