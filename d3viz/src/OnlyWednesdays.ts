import * as d3 from "d3";
import { AbstractDataViz } from "./AbstractDataViz";
export class OnlyWednesdays extends AbstractDataViz {

    getKeys(): string[] {
        return ["Temperature (F)", "Number of Cyclists"];
    }

    getKeyColors(): string[] {
        return ["#D2691E", "steelblue"];
    }

    getCSVName(): string {
        return "NOAATempCyclist-OnlyWednesdays";
    }

    getGraphTitle(): string {
        return "Only wednesdays";
    }

    parse(data: d3.DSVRowString<string>): Object {
        return {
            date: new Date(data.Date),
            minTemp: +data.MinTemp,
            maxTemp: +data.MaxTemp,
            avgTemp: +data.AvgTemp,
            cyclistsEast: +data.CyclistsEast,
            cyclistsWest: +data.CyclistsEast,
            cyclistsTotal: +data.CyclistsTotal,
            dayOfWeek: +data.DayOfWeek
        }
    }
    draw(data: d3.DSVParsedArray<Object>, svg: d3.Selection<d3.BaseType, unknown, HTMLElement, any>): void {
        console.log(data);


        var x = d3.scaleTime()
            .domain(d3.extent(data, function (d: any) { return d.date; }) as [Date, Date])
            .range([0, this.width]);
        svg.append("g")
            .attr("transform", "translate(0," + this.height + ")")
            .call(d3.axisBottom(x));

        // Add Y axis
        var y = d3.scaleLinear()
            .domain(d3.extent(data, function (d: any) { return d.avgTemp; }) as [number, number])
            .range([this.height, 0]);
        svg.append("g")
            .call(d3.axisLeft(y));

        var y2 = d3.scaleLinear()
            .domain(d3.extent(data, function (d: any) { return d.cyclistsTotal; }) as [number, number])
            .range([this.height, 0]);
        svg.append("g")
            .call(d3.axisRight(y2));


        svg.append("path")
            .datum(data)
            .attr("fill", "none")
            .attr("stroke", this.getKeyColors()[1])
            .attr("stroke-width", 1.5)
            .attr("d", d3.line<any>()
                .x(function (d) { return x(d.date) })
                .y(function (d) { return y(d.avgTemp) })
            );

        svg.append("path")
            .datum(data)
            .attr("fill", "none")
            .attr("stroke", this.getKeyColors()[0])
            .attr("stroke-width", 1.5)
            .attr("d", d3.line<any>()
                .x(function (d) { return x(d.date) })
                .y(function (d) { return y2(d.cyclistsTotal) })
            );
    }
}