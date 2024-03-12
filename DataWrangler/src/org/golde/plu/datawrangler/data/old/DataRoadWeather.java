package org.golde.plu.datawrangler.data.old;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class DataRoadWeather {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

    private final String stationName;
    private final String longitude;
    private final String latitude;
    private final Date date;
    private final String recordId;
    private final float roadTemperature;
    private final float airTemperature;

    public static DataRoadWeather of(String csvLine) throws ParseException, NumberFormatException {
        final String[] values = csvLine.split(",");
        if(values.length != 6){
            System.err.println("Invalid CSV line: " + csvLine);
            return null;
        }

        final String stationName = values[0];

        //https://dev.socrata.com/docs/datatypes/point.html#, "POINT(-122.403 37.775)". Longitude comes first weirdly.
        String pointStr = values[1];
        pointStr.substring(7, pointStr.length() - 1); // remove "POINT(" and ")"
        final  String[] point = pointStr.split(" ");
        final String longitude = point[0];
        final String latitude = point[1];

        final Date date = SDF.parse(values[2]);
        final String recordId = values[3];
        final float roadTemperature = Float.parseFloat(values[4]);
        final float airTemperature = Float.parseFloat(values[5]);

        return new DataRoadWeather(stationName, longitude, latitude, date, recordId, roadTemperature, airTemperature);
    }

    public static Comparator<DataRoadWeather> byDate() {
        return Comparator.comparing(DataRoadWeather::getDate);
    }

    public static Comparator<DataRoadWeather> byStationName() {
        return Comparator.comparing(DataRoadWeather::getStationName);
    }

    public Date getDateJustHour() {
        final Date dateClone = (Date) date.clone();
        dateClone.setMinutes(0);
        dateClone.setSeconds(0);

        return dateClone;
    }

}
