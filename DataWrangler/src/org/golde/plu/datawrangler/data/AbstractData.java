package org.golde.plu.datawrangler.data;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public abstract class AbstractData {



    public AbstractData(String csvLine) throws CSVParseException {
        if(csvLine == null || csvLine.isEmpty()) {
            throw new CSVParseException("CSV line is empty");
        }
    }

    protected final String[] split(String csvLine, int expectedLength) throws CSVParseException {
        csvLine = removeCommasInQuotes(csvLine);
        String[] values = csvLine.split(",");

        //Remove quotes and trim
        for(int i = 0; i < values.length; i++) {
            values[i] = values[i].replace("\"", "").trim();
        }

        if(values.length != expectedLength){
            throw new CSVParseException("Expected " + expectedLength + " values, but got " + values.length + " in " + csvLine);
        }

        return values;
    }

    private static String removeCommasInQuotes(String line) {
        StringBuilder result = new StringBuilder();
        boolean insideQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                insideQuotes = !insideQuotes;
            } else if (c == ',' && insideQuotes) {
                // Ignore commas inside quotes
                continue;
            }
            result.append(c);
        }

        return result.toString();
    }

    protected final int parseInteger(String s) throws CSVParseException {
        try {
            return Integer.parseInt(s);
        } catch(NumberFormatException e) {
            throw new CSVParseException("Invalid integer: " + s, e);
        }
    }

    protected final Date parseDate(SimpleDateFormat SDF, String s) throws CSVParseException {
        try {
            return SDF.parse(s);
        } catch(java.text.ParseException e) {
            throw new CSVParseException("Invalid date: " + s, e);
        }
    }

    public static boolean isInTheSameDay(Date date1, Date date2) {
        return date1.getYear() == date2.getYear() &&
                date1.getMonth() == date2.getMonth() &&
                date1.getDate() == date2.getDate();
    }

}
