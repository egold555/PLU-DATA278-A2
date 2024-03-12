import * as d3 from "d3";
import { AbstractDataViz } from "./AbstractDataViz";
export class CyclistPerYear extends AbstractDataViz {

    getKeys(): string[] {
        return ["2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023"];
    }

    getKeyColors(): string[] {
        return ["red", "blue", "green", "yellow", "purple", "orange", "black", "brown", "pink", "gray", "cyan", "magenta"];
    }

    getCSVName(): string {
        return "NOAATempCyclist-CyclistsPerYear";
    }

    getGraphTitle(): string {
        return "Cyclists per year";
    }

    parse(data: d3.DSVRowString<string>): Object {
        return {
            year: +data.Year,
            totalCyclists: +data.TotalCyclists,
        }
    }
    draw(data: d3.DSVParsedArray<Object>, svg: d3.Selection<d3.BaseType, unknown, HTMLElement, any>): void {
        console.log(data);

        var this2 = this;

        var x = d3.scaleBand()
            .range([0, this.width])
            .domain(data.map(function (d: any) { return d.year; }))


        svg.append("g")
            .attr("transform", "translate(0," + this.height + ")")
            .call(d3.axisBottom(x));

        // Add Y axis
        var y = d3.scaleLinear()
            .domain([0, d3.max(data, function (d: any) { return d.totalCyclists; })] as [number, number])
            .range([this.height, 0]);


        svg.append("g")
            .call(d3.axisLeft(y));



        //Bars
        svg.selectAll("mybar")
            .data(data)
            .enter()
            .append("rect")
            .attr("x", function (d: any) { return x(d.year); })
            .attr("y", function (d: any) { return y(d.totalCyclists); })
            .attr("width", x.bandwidth())
            .attr("height", function (d: any) { return this2.height - y(d.totalCyclists); })
            .attr("fill", function (d: any, i: number) { return this2.getKeyColors()[i]; })
    }
}