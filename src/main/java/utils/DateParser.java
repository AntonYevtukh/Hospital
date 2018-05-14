package utils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class DateParser {

    private DateParser() {

    }

    public static Date parseDate(String dateString, Locale locale, String formatKey) {
        try {
            String formatString = ResourceBundle.getBundle("formats", locale).getString(formatKey);
            java.util.Date utilDate = new SimpleDateFormat(formatString).parse(dateString);
            return new Date(utilDate.getTime());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
