package app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateFormat {
    // Date format info:
    // https://code.makery.ch/blog/javafx-8-tableview-cell-renderer/
    // http://tutorials.jenkov.com/java-date-time/parsing-formatting-dates.html

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YY");

    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }
}
