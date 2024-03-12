package org.golde.plu.datawrangler.data.old;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class DataGroupRoadWeather {

    @Getter private final String stationName;
    @Getter private final Date date;
    private List<DataRoadWeather> roadWeathers;

    @Getter private final float averageRoadTemperature;
    @Getter private final float averageAirTemperature;

    public static DataGroupRoadWeather of(String stationName, Date date, List<DataRoadWeather> roadWeathers) {

        float averageRoadTemperature = 0;
        float averageAirTemperature = 0;

        for(DataRoadWeather roadWeather : roadWeathers) {
            averageRoadTemperature += roadWeather.getRoadTemperature();
            averageAirTemperature += roadWeather.getAirTemperature();
        }

        averageRoadTemperature /= roadWeathers.size();
        averageAirTemperature /= roadWeathers.size();

        return new DataGroupRoadWeather(stationName, date, roadWeathers, averageRoadTemperature, averageAirTemperature);
    }

    public static Comparator<DataGroupRoadWeather> byDate() {
        return Comparator.comparing(DataGroupRoadWeather::getDate);
    }

}
