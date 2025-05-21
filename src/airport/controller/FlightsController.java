/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.controller.utils.Response;
import airport.controller.utils.Status;
import java.time.DateTimeException;
import java.time.LocalDateTime;

/**
 *
 * @author plobb
 */
public class FlightsController {
    public static Response validateFlightData(
        String id,
        String planeId,
        String departureLocationId,
        String arrivalLocationId,
        String scaleLocationId,
        String yearStr,
        String monthStr,
        String dayStr,
        String hourStr,
        String minuteStr,
        String durationHourStr,
        String durationMinuteStr,
        String scaleDurationHourStr,
        String scaleDurationMinuteStr
    ) {
        if (id == null || id.trim().isEmpty()) return new Response("ID no puede estar vacío", Status.BAD_REQUEST);
        if (planeId == null || planeId.trim().isEmpty()) return new Response("Debe seleccionar un avión", Status.BAD_REQUEST);
        if (departureLocationId == null || departureLocationId.trim().isEmpty()) return new Response("Debe seleccionar lugar de salida", Status.BAD_REQUEST);
        if (arrivalLocationId == null || arrivalLocationId.trim().isEmpty()) return new Response("Debe seleccionar lugar de llegada", Status.BAD_REQUEST);

        try {
            int year = Integer.parseInt(yearStr);
            int month = Integer.parseInt(monthStr);
            int day = Integer.parseInt(dayStr);
            int hour = Integer.parseInt(hourStr);
            int minute = Integer.parseInt(minuteStr);

            LocalDateTime.of(year, month, day, hour, minute);
        } catch (NumberFormatException | DateTimeException e) {
            return new Response("Fecha o hora inválida", Status.BAD_REQUEST);
        }

        try {
            int durHour = Integer.parseInt(durationHourStr);
            int durMinute = Integer.parseInt(durationMinuteStr);
            if (durHour < 0 || durMinute < 0) return new Response("Duración inválida", Status.BAD_REQUEST);

            if (scaleDurationHourStr != null && !scaleDurationHourStr.isEmpty() &&
                scaleDurationMinuteStr != null && !scaleDurationMinuteStr.isEmpty()) {
                int scaleDurHour = Integer.parseInt(scaleDurationHourStr);
                int scaleDurMinute = Integer.parseInt(scaleDurationMinuteStr);
                if (scaleDurHour < 0 || scaleDurMinute < 0) return new Response("Duración de escala inválida", Status.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            return new Response("Duración inválida", Status.BAD_REQUEST);
        }

        return new Response("Validación exitosa", Status.CREATED);
    }
}
