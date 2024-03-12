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
import org.golde.plu.datawrangler.data.DataCyclist;
import org.golde.plu.datawrangler.data.DataTemperature;
import org.golde.plu.datawrangler.storage.StorageMPDOTW;
import org.golde.plu.datawrangler.storage.StorageTotals;

public class Printer {

    public static final void printAll(Map<Date, Pair<DataCyclist, DataTemperature>> dateToCyclistTemperature) throws IOException {
        final List<String> lines = new ArrayList<>();
        lines.add("Date,CyclistsEast,CyclistsWest,CyclistsTotal,MinTemp,MaxTemp,AvgTemp,DayOfTheWeek");

        for(Date date : dateToCyclistTemperature.keySet().stream().sorted(Date::compareTo).collect(Collectors.toList())) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            final int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

            final Pair<DataCyclist, DataTemperature> pair = dateToCyclistTemperature.get(date);
            final DataCyclist cyclist = pair.getA();
            final DataTemperature temperature = pair.getB();

            final int cyclistsEast = cyclist.getEast();
            final int cyclistsWest = cyclist.getWest();
            final int cyclistTotal = cyclist.getTotal();

            final int tempMin = temperature.getMinTemp();
            final int tempMax = temperature.getMaxTemp();
            final float tempAvg = temperature.getAvgTemp();

            lines.add("\"" + date + "\"," + cyclistsEast + "," + cyclistsWest + "," + cyclistTotal + "," + tempMin + "," + tempMax + "," + tempAvg + "," + dayOfWeek);

        }

        Files.write(Paths.get("NOAATempCyclist.csv"), lines);
    }

    public static final void printEveryWednesday2016(Map<Date, Pair<DataCyclist, DataTemperature>> dateToCyclistTemperature) throws IOException {
        final List<String> lines = new ArrayList<>();
        lines.add("Date,CyclistsEast,CyclistsWest,CyclistsTotal,MinTemp,MaxTemp,AvgTemp,DayOfTheWeek");

        for(Date date : dateToCyclistTemperature.keySet().stream().sorted(Date::compareTo).collect(Collectors.toList())) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            final int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

            if(dayOfWeek != Calendar.WEDNESDAY) {
                continue;
            }

            if(cal.get(Calendar.YEAR) != 2016) {
                continue;
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

            lines.add("\"" + date + "\"," + cyclistsEast + "," + cyclistsWest + "," + cyclistTotal + "," + tempMin + "," + tempMax + "," + tempAvg);

        }

        Files.write(Paths.get("NOAATempCyclist-Wednesday2016.csv"), lines);
    }

    public static final void printEverySunday2016(Map<Date, Pair<DataCyclist, DataTemperature>> dateToCyclistTemperature) throws IOException {
        final List<String> lines = new ArrayList<>();
        lines.add("Date,CyclistsEast,CyclistsWest,CyclistsTotal,MinTemp,MaxTemp,AvgTemp,DayOfTheWeek");

        for(Date date : dateToCyclistTemperature.keySet().stream().sorted(Date::compareTo).collect(Collectors.toList())) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            final int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

            if(dayOfWeek != Calendar.SUNDAY) {
                continue;
            }

            if(cal.get(Calendar.YEAR) != 2016) {
                continue;
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

            lines.add("\"" + date + "\"," + cyclistsEast + "," + cyclistsWest + "," + cyclistTotal + "," + tempMin + "," + tempMax + "," + tempAvg);

        }

        Files.write(Paths.get("NOAATempCyclist-Sunday2016.csv"), lines);
    }

    public static final void printEveryWeekAVG(List<StorageMPDOTW> perWeek) throws IOException {
        final List<String> lines = new ArrayList<>();



        lines.add("StartDate,EndDate,TotalCyclists,TotalEast,TotalWest,TempMin,TempMax,TempAvg");

        for(StorageMPDOTW s : perWeek) {
            lines.add(s.toString());
        }

        Files.write(Paths.get("NOAATempCyclist-EveryWeekAVG.csv"), lines);
    }

    public static final void printMostPoplarDay(Map<Date, Pair<DataCyclist, DataTemperature>> dateToCyclistTemperature) throws IOException {
        final List<String> lines = new ArrayList<>();

        Map<Integer, StorageTotals> tmpStorage = new HashMap<>();

        for(Date date: dateToCyclistTemperature.keySet()) {
            final Pair<DataCyclist, DataTemperature> pair = dateToCyclistTemperature.get(date);
            final DataCyclist cyclist = pair.getA();
            final DataTemperature temperature = pair.getB();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int day = cal.get(Calendar.DAY_OF_WEEK);

            StorageTotals st = tmpStorage.get(day);
            if(st == null) {
                st = new StorageTotals(0, 0, 0, 0, 0, 0, 0);
                tmpStorage.put(day, st);
                System.out.println("Creating new StorageTotals for day " + day);
            }

            st.setTotalCyclists(st.getTotalCyclists() + cyclist.getTotal());
            st.setTotalEast(st.getTotalEast() + cyclist.getEast());
            st.setTotalWest(st.getTotalWest() + cyclist.getWest());
            st.setTempMin(st.getTempMin() + temperature.getMinTemp());
            st.setTempMax(st.getTempMax() + temperature.getMaxTemp());
            st.setTempAvg(st.getTempAvg() + temperature.getAvgTemp());
            st.setDayCount(st.getDayCount() + 1);

        }

        lines.add("DayOfTheWeek,TotalCyclists,TotalEast,TotalWest,TempMin,TempMax,TempAvg,DayCount");
        for(int i = 1; i <= 7; i++) {
            StorageTotals st = tmpStorage.get(i);
            int dayCount = st.getDayCount();
            float tempMin = st.getTempMin() / dayCount;
            float tempMax = st.getTempMax() / dayCount;
            float tempAvg = st.getTempAvg() / dayCount;

            lines.add(i + "," + st.getTotalCyclists() + "," + st.getTotalEast() + "," + st.getTotalWest() + "," + tempMin + "," + tempMax + "," + tempAvg + "," + dayCount);
        }

        Files.write(Paths.get("NOAATempCyclist-MostPoplarDay.csv"), lines);

    }

    public static final void printCyclistsPerYear(Map<Date, Pair<DataCyclist, DataTemperature>> dateToCyclistTemperature) throws IOException {
        final List<String> lines = new ArrayList<>();

        Map<Integer, Integer> tmpStorage = new HashMap<>();

        for(Date date: dateToCyclistTemperature.keySet()) {
            final Pair<DataCyclist, DataTemperature> pair = dateToCyclistTemperature.get(date);
            final DataCyclist cyclist = pair.getA();
            final DataTemperature temperature = pair.getB();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            int year = cal.get(Calendar.YEAR);
            if(!tmpStorage.containsKey(year)) {
                tmpStorage.put(year, 0);
            }

            tmpStorage.put(year, tmpStorage.get(year) + cyclist.getTotal());

        }

        lines.add("Year,TotalCyclists");
        for(Integer key : tmpStorage.keySet()) {
            lines.add(key + "," + tmpStorage.get(key));
        }

        Files.write(Paths.get("NOAATempCyclist-CyclistsPerYear.csv"), lines);

    }

    public static final void printCyclistsPerHour(List<DataCyclist> cyclists) throws IOException {
        final List<String> lines = new ArrayList<>();

        Map<Integer, Integer> tmpStorage = new HashMap<>();

        for(DataCyclist cycl: cyclists) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(cycl.getDate());

            int year = cal.get(Calendar.HOUR_OF_DAY);
            if(!tmpStorage.containsKey(year)) {
                tmpStorage.put(year, 0);
            }

            tmpStorage.put(year, tmpStorage.get(year) + cycl.getTotal());

        }

        lines.add("Hour,TotalCyclists");
        for(Integer key : tmpStorage.keySet()) {
            lines.add(key + "," + tmpStorage.get(key));
        }

        Files.write(Paths.get("NOAATempCyclist-CyclistsHour.csv"), lines);

    }

}
