import org.golde.plu.datawrangler.DataWrangler;

public class Main {
    public static void main(String[] args) {

        DataWrangler.wrangle();

    }
}

/*
package org.golde.plu.datawrangler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.golde.plu.datawrangler.data.CSVParseException;
import org.golde.plu.datawrangler.data.DataCyclist;
import org.golde.plu.datawrangler.data.old.DataGroupRoadWeather;
import org.golde.plu.datawrangler.data.old.DataRoadWeather;

public class DataWrangler {

    public static void wrangle() {
        //read csv
        final List<DataRoadWeather> roadWeathers = new ArrayList<>();
        final List<DataCyclist> cyclists = new ArrayList<>();

        try {
            final List<String> lines = new ArrayList();
            lines.addAll(Files.readAllLines(Paths.get("../RoadWeather.csv")));
            lines.remove(0); // remove header
            for (String line : lines) {
                DataRoadWeather roadWeather = DataRoadWeather.of(line);
                if(roadWeather != null) {
                    //We only want data from the AuroraBridge
                    if(roadWeather.getStationName().equals("AuroraBridge")) {
                        roadWeathers.add(roadWeather);
                    }
                }
            }
        } catch (IOException | ParseException | NumberFormatException e) {
            e.printStackTrace();
        }

        System.out.println("Loaded RAW RoadWeathers: " + roadWeathers.size());

        try {
            final List<String> lines = new ArrayList();
            lines.addAll(Files.readAllLines(Paths.get("../CyclistCounter.csv")));
            lines.remove(0); // remove header
            for (String line : lines) {
                try {
                    DataCyclist cyclist = new DataCyclist(line);
                    cyclists.add(cyclist);
                }
                catch(CSVParseException e) {
                    System.err.println("Invalid CSV line: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded RAW Cyclists: " + cyclists.size());

       // Each data road weather updates roughly once a minute.
       // We want to group the data by station name, longitude, latitude, and date PER HOUR.


// Station name to every data point
Map<String, List<DataRoadWeather>> stationNameToEveryDataPoint = roadWeathers.stream()
        .collect(Collectors.groupingBy(DataRoadWeather::getStationName));

List<DataGroupRoadWeather> dataGroupRoadWeathers = new ArrayList<>();
Map<Date, DataGroupRoadWeather> dateToDataGroupRoadWeather = new HashMap<>();

        for(Map.Entry<String, List<DataRoadWeather>> entry : stationNameToEveryDataPoint.entrySet()) {
final String stationName = entry.getKey();
final List<DataRoadWeather> allPoints = entry.getValue();

// Group by date if the date is within the same hour
Map<Date, List<DataRoadWeather>> dateToEveryDataPoint = allPoints.stream()
        .collect(Collectors.groupingBy(DataRoadWeather::getDateJustHour));

            for(Map.Entry<Date, List<DataRoadWeather>> entry2 : dateToEveryDataPoint.entrySet()) {
final Date date = entry2.getKey();
final List<DataRoadWeather> allPoints2 = entry2.getValue();
final DataGroupRoadWeather dataGroupRoadWeather = DataGroupRoadWeather.of(stationName, date, allPoints2);

                dataGroupRoadWeathers.add(dataGroupRoadWeather);
                dateToDataGroupRoadWeather.put(date, dataGroupRoadWeather);
            }
                    }

                    cyclists.sort(DataCyclist.byDate());

Map<Date, Pair<DataGroupRoadWeather, DataCyclist>> dateToDataGroupRoadWeatherAndCyclist = new HashMap<>();
        for(DataCyclist cyclist : cyclists) {
final Date date = cyclist.getDateJustHour();
final DataGroupRoadWeather dataGroupRoadWeather = dateToDataGroupRoadWeather.get(date);
            if(dataGroupRoadWeather != null) {
        dateToDataGroupRoadWeatherAndCyclist.put(date, new Pair(dataGroupRoadWeather, cyclist));
        }
        }

        //write csvs
//        try {
//            final List<String> lines = new ArrayList();
//            lines.add("stationName,date,averageRoadTemperature,averageAirTemperature");
//            dataGroupRoadWeathers.sort(org.golde.plu.datawrangler.data.old.DataGroupRoadWeather.byDate());
//            for (org.golde.plu.datawrangler.data.old.DataGroupRoadWeather dataGroupRoadWeather : dataGroupRoadWeathers) {
//                lines.add(dataGroupRoadWeather.getStationName() + "," + dataGroupRoadWeather.getDate() + "," + dataGroupRoadWeather.getAverageRoadTemperature() + "," + dataGroupRoadWeather.getAverageAirTemperature());
//            }
//            Files.write(Paths.get("RoadWeatherGrouped.csv"), lines);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {

final List<String> lines = new ArrayList();
            lines.add("date,averageRoadTemperature,averageAirTemperature,totalCyclists,eastBoundCyclists,westBoundCyclists");
            System.out.println("dateToDataGroupRoadWeatherAndCyclist.size() = " + dateToDataGroupRoadWeatherAndCyclist.size());
        for (final Date date : dateToDataGroupRoadWeatherAndCyclist.keySet().stream().sorted(Date::compareTo).collect(Collectors.toList())) {
final Pair<DataGroupRoadWeather, DataCyclist> pair = dateToDataGroupRoadWeatherAndCyclist.get(date);
final DataGroupRoadWeather dataGroupRoadWeather = pair.getA();
final DataCyclist cyclist = pair.getB();

final float averageRoadTemperature = dataGroupRoadWeather.getAverageRoadTemperature();
final float averageAirTemperature = dataGroupRoadWeather.getAverageAirTemperature();
final int totalCyclists = cyclist.getTotal();
final int eastBoundCyclists = cyclist.getEast();
final int westBoundCyclists = cyclist.getWest();

                if(date.getYear() != 2020-1900) {
        continue;
        }

        lines.add(date + "," + averageRoadTemperature + "," + averageAirTemperature + "," + totalCyclists + "," + eastBoundCyclists + "," + westBoundCyclists);
            }
                    Files.write(Paths.get("RoadWeatherGroupedWithCyclists2020.csv"), lines);
        } catch (
IOException e) {
        e.printStackTrace();

        }
                catch (Exception e) {
        e.printStackTrace();
        }

                System.out.println("Done!");
    }

            }

 */