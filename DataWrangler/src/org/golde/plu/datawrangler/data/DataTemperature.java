package org.golde.plu.datawrangler.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class DataTemperature extends AbstractData {

    //Format 2/19/2024
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    //https://en.wikipedia.org/wiki/Climate_of_Seattle
    private static final int MIN_TEMP_RANGE = 0; //F
    private static final int MAX_TEMP_RANGE = 108; //F

    private final String stationID;
    private final String displayName;
    private final Date date;
    private final int minTemp;
    private final int maxTemp;

    private final float avgTemp;

    public DataTemperature(String csvLine) throws CSVParseException {
        super(csvLine);
        final String[] values = split(csvLine, 5);
        this.stationID = values[0];
        this.displayName = values[1];
        this.date = parseDate(SDF, values[2]);
        this.maxTemp = parseInteger(values[3]);
        this.minTemp = parseInteger(values[4]);


        if(minTemp > maxTemp) {
            throw new CSVParseException("Min temp " + minTemp + " is greater than max temp " + maxTemp + "!");
        }

        if(minTemp < MIN_TEMP_RANGE) {
            throw new CSVParseException("Min Temperature " + minTemp + " out of range! Min temp must be greater than " + MIN_TEMP_RANGE);
        }

        if(maxTemp > MAX_TEMP_RANGE) {
            throw new CSVParseException("Max Temperature " + maxTemp + " out of range! Max temp must be less than " + MAX_TEMP_RANGE);
        }

        this.avgTemp = (minTemp + maxTemp) / 2f;
    }
}
