import * as d3 from "d3";
import { AbstractDataViz } from "./AbstractDataViz";
export class MostPopularDay extends AbstractDataViz {

    getKeys(): string[] {
        return ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
    }

    getKeyColors(): string[] {
        return ["red", "orange", "yellow", "green", "blue", "indigo", "violet"];
    }

    getCSVName(): string {
        return "NOAATempCyclist-MostPoplarDay";
    }

    getGraphTitle(): string {
        return "Most popular day";
    }

    parse(data: d3.DSVRowString<string>): Object {
        return {
            dayOfWeek: +data.DayOfTheWeek,
            minTemp: +data.TempMin,
            maxTemp: +data.TempMax,
            avgTemp: +data.TempAvg,
            cyclistsEast: +data.TotalWest,
            cyclistsWest: +data.TotalEast,
            cyclistsTotal: +data.TotalCyclists,
            dayCount: +data.DayCount,
            dayName: this.getKeys()[+data.DayOfTheWeek - 1]
        }
    }
    draw(data: d3.DSVParsedArray<Object>, svg: d3.Selection<d3.BaseType, unknown, HTMLElement, any>): void {
        console.log(data);

        var this2 = this;

        var x = d3.scaleBand()
            .range([0, this.width])
            .domain(data.map(function (d: any) { return d.dayName; }))


        svg.append("g")
            .attr("transform", "translate(0," + this.height + ")")
            .call(d3.axisBottom(x));

        // Add Y axis
        var y = d3.scaleLinear()
            .domain([0, d3.max(data, function (d: any) { return d.cyclistsTotal; })] as [number, number])
            .range([this.height, 0]);


        svg.append("g")
            .call(d3.axisLeft(y));



        //Bars
        svg.selectAll("mybar")
            .data(data)
            .enter()
            .append("rect")
            .attr("x", function (d: any) { return x(d.dayName); })
            .attr("y", function (d: any) { return y(d.cyclistsTotal); })
            .attr("width", x.bandwidth())
            .attr("height", function (d: any) { return this2.height - y(d.cyclistsTotal); })
            .attr("fill", function (d: any) { return this2.getKeyColors()[d.dayOfWeek - 1]; })
    }
}