package src;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.JFormattedTextField.AbstractFormatter;

public class CustomDateFormatter extends AbstractFormatter {

    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }

        return "";
    }

    public String logDate (String text) throws ParseException {
        String output = "";
        Locale l = Locale.US;

        if (text != null) {
            if(text.length() == 9) {
                DateTimeFormatter fmtr = DateTimeFormatter.ofPattern("MM/dd/uu/", l);
                LocalDate newDate = LocalDate.parse(text, fmtr);
                output = newDate.toString();
            } else if(text.length() == 7) {
                DateTimeFormatter fmtr = DateTimeFormatter.ofPattern("M/dd/uu", l);
                LocalDate newDate = LocalDate.parse(text, fmtr);
                output = newDate.toString();
            }
        }

        return output;
    }

}
