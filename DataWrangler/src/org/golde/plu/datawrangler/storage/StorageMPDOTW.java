package org.golde.plu.datawrangler.storage;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StorageMPDOTW {
    private final Date startDate;
    private final Date endDate;
    private final int totalCyclists;
    private final int totalEast;
    private final int totalWest;
    private final float tempMin;
    private final float tempMax;
    private final float tempAvg;

    @Override
    public String toString() {
        return startDate + "," + endDate + "," + totalCyclists + "," + totalEast + "," + totalWest + "," + tempMin + "," + tempMax + "," + tempAvg;
    }
}
