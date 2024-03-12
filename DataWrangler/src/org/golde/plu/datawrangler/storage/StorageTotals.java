package org.golde.plu.datawrangler.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StorageTotals {
    private int totalCyclists;
    private int totalEast;
    private int totalWest;
    private float tempMin;
    private float tempMax;
    private float tempAvg;
    private int dayCount;
}
