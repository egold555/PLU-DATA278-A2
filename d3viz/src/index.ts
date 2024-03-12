import * as d3 from "d3";
import { Wednesday2016 } from "./Wednesday2016";
import { Wednesday2016Scatter } from "./Wednesday2016Scatter";
import { FullDatasetTest } from "./FullDatasetTest";
import { FullScatter } from "./FullScatter";
import { AbstractDataViz } from "./AbstractDataViz";
import { MostPopularDay } from "./MostPopularDay";
import { OnlyWednesdays } from "./OnlyWednesdays";
import { Sunday2016 } from "./Sunday2016";
import { CyclistPerYear } from "./CyclistsPerYear";
import { CyclistsPerHour } from "./CyclistsPerHour";

function init() {

    let URLS: Record<string, AbstractDataViz> = {};
    URLS["full"] = new FullDatasetTest();
    URLS["fullScatter"] = new FullScatter();
    URLS["wed2016"] = new Wednesday2016();
    URLS["wed2016Scatter"] = new Wednesday2016Scatter();
    URLS["sun2016"] = new Sunday2016();
    URLS["mostPopularDay"] = new MostPopularDay();
    URLS["onlyWednesdays"] = new OnlyWednesdays();
    URLS["cyclistsPerYear"] = new CyclistPerYear();
    URLS["cyclistsPerHour"] = new CyclistsPerHour();

    const urlParams = new URLSearchParams(window.location.search);
    let visName = urlParams.get('vis');
    if (visName != null || (visName in URLS)) {
        let vis = URLS[visName];
        vis.go();
    }
    else {

        let html = "Invalid graph selected. Try some of the following:<ul>";
        for (let key in URLS) {
            let graph = URLS[key];
            let title = graph.getGraphTitle();
            html += "<li><a href='?vis=" + key + "'>" + title + "</a></li>";
        }
        html += "</ul>";
        document.getElementById("dataviz").innerHTML = html;
    }



}

// MUST BE AT THE END OF THE FILE
init();