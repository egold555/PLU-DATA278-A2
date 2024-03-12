package org.golde.plu.datawrangler.data;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString

public class DataCyclist extends AbstractData {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

    private final Date date;
    @Setter
    private int total;
    @Setter private int west;
    @Setter private int east;

    public DataCyclist(String csvLine) throws CSVParseException {
        super(csvLine);

        final String[] values = split(csvLine, 4);
        this.date = parseDate(SDF, values[0]);
        this.total = parseInteger(values[1]);
        this.west = parseInteger(values[2]);
        this.east = parseInteger(values[3]);

        //Sanity check
        if(west + east != total) {
            System.err.println("West + East != Total: " + csvLine + "! Fixing....");
            total = west + east;
        }
    }

    public Date getDateNoHour() {
        return new Date(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
    }
}
