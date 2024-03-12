package org.golde.plu.datawrangler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.golde.plu.datawrangler.data.CSVParseException;
import org.golde.plu.datawrangler.data.DataCyclist;
import org.golde.plu.datawrangler.data.DataTemperature;
import org.golde.plu.datawrangler.storage.StorageMPDOTW;

public class DataWrangler {




    public static void wrangle() {
        //read csv
        List<DataCyclist> cyclistsRAW = new ArrayList<>();
        final List<DataTemperature> temperatures = new ArrayList<>();
        parseCyclists(cyclistsRAW);
        System.out.println("Loaded Cyclists data count: " + cyclistsRAW.size());
        parseTemperature(temperatures);
        System.out.println("Loaded Temperature data count: " + temperatures.size());

        List<DataCyclist> cyclists = groupCyclistsByDay(cyclistsRAW);

//        cyclists.sort(Comparator.comparing(DataCyclist::getDate));
//        temperatures.sort(Comparator.comparing(DataTemperature::getDate));

        Map<Date, Pair<DataCyclist, DataTemperature>> dateToCyclistTemperature = new HashMap<>();
        for(DataCyclist cyclist : cyclists) {

            //Clone the date so we can set the time to 0
            final Date date = (Date) cyclist.getDate().clone();
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);


            final DataTemperature temperature = temperatures.stream().filter(t -> DataTemperature.isInTheSameDay(t.getDate(), date)).findFirst().orElse(null);
            if(temperature == null) {
                System.err.println("No temperature data for date: " + date);
                continue;
            }
            dateToCyclistTemperature.put(date, new Pair<>(cyclist, temperature));


        }

        Date startDate = null;
        int weekTotalCyclists = 0;
        int weekTotalEast = 0;
        int weekTotalWest = 0;
        float weekTempMin = 0;
        float weekTempMax = 0;
        float weekTempAvg = 0;

        List<StorageMPDOTW> perWeek = new ArrayList<>();

        for(Date date : dateToCyclistTemperature.keySet().stream().sorted(Date::compareTo).collect(Collectors.toList())) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int dotw = cal.get(Calendar.DAY_OF_WEEK);

            if(startDate == null) {
                startDate = date;
            }

            if(dotw == Calendar.TUESDAY) {
                startDate = date;
            }

            if(dotw == Calendar.MONDAY) {
                //write it to a file and zero it out
                Date endDate = date;
                weekTempMax = weekTempMax / 7f;
                weekTempMin = weekTempMin / 7f;
                weekTempAvg = weekTempAvg / 7f;

                perWeek.add(new StorageMPDOTW(startDate, endDate, weekTotalCyclists, weekTotalEast, weekTotalWest, weekTempMin, weekTempMax, weekTempAvg));

                weekTotalCyclists = 0;
                weekTotalEast = 0;
                weekTotalWest = 0;
                weekTempMin = 0;
                weekTempMax = 0;
                weekTempAvg = 0;
                startDate = null;
                endDate = null;
            }

            final Pair<DataCyclist, DataTemperature> pair = dateToCyclistTemperature.get(date);
            final DataCyclist cyclist = pair.getA();
            final DataTemperature temperature = pair.getB();

            final int cyclistsEast = cyclist.getEast();
            final int cyclistsWest = cyclist.getWest();
            final int cyclistTotal = cyclist.getTotal();

            final int tempMin = temperature.getMinTemp();
            final int tempMax = temperature.getMaxTemp();
            final float tempAvg = temperature.getAvgTemp();

            weekTotalCyclists += cyclistTotal;
            weekTotalEast += cyclistsEast;
            weekTotalWest += cyclistsWest;
            weekTempMin += tempMin;
            weekTempMax += tempMax;
            weekTempAvg += tempAvg;

        }


        try {
            //Printer.printAll(dateToCyclistTemperature);
            //Printer.printEveryWednesday2016(dateToCyclistTemperature);
            //Printer.printEveryWeekAVG(perWeek);
            //Printer.printMostPoplarDay(dateToCyclistTemperature);
            //Printer.printEverySunday2016(dateToCyclistTemperature);
            //Printer.printCyclistsPerYear(dateToCyclistTemperature);
            Printer.printCyclistsPerHour(cyclistsRAW);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static List<DataCyclist> groupCyclistsByDay(List<DataCyclist> cyclists) {
        Map<Date, DataCyclist> dateToCyclist = new HashMap<>();
        for(DataCyclist cyclist : cyclists) {
            final Date date = cyclist.getDateNoHour();
            if(dateToCyclist.containsKey(date)) {
                final DataCyclist existing = dateToCyclist.get(date);
                existing.setEast(existing.getEast() + cyclist.getEast());
                existing.setWest(existing.getWest() + cyclist.getWest());
                existing.setTotal(existing.getTotal() + cyclist.getTotal());
            }
            else {
                dateToCyclist.put(date, cyclist);
            }
        }
        return new ArrayList<>(dateToCyclist.values());
    }

    private static void parseTemperature(List<DataTemperature> temperatures) {
        try {
            final List<String> lines = new ArrayList();
            lines.addAll(Files.readAllLines(Paths.get("../NOAATemperature.csv")));
            lines.remove(0); // remove header
            for (String line : lines) {
                try {
                    DataTemperature temperature = new DataTemperature(line);
                    temperatures.add(temperature);
                }
                catch(CSVParseException e) {
                    System.err.println(e.getMessage() + " in file NOAATemperature.csv");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void parseCyclists(List<DataCyclist> cyclists) {
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
                    System.err.println(e.getMessage() + " in file CyclistCounter.csv");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
