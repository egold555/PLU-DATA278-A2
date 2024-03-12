import * as d3 from "d3";
import { AbstractDataViz } from "./AbstractDataViz";
export class FullScatter extends AbstractDataViz {

    getKeys(): string[] {
        return [];
    }

    getKeyColors(): string[] {
        return [];
    }

    getCSVName(): string {
        return "NOAATempCyclist";
    }

    getGraphTitle(): string {
        return "Temperature and Number of Cyclists (Scatter)";
    }

    parse(data: d3.DSVRowString<string>): Object {
        return {
            avgTemp: +data.AvgTemp,
            cyclistsTotal: +data.CyclistsTotal
        }
    }
    draw(data: d3.DSVParsedArray<Object>, svg: d3.Selection<d3.BaseType, unknown, HTMLElement, any>): void {
        console.log(data);


        var x = d3.scaleLinear()
            .domain([0, d3.max(data, function (d: any) { return d.avgTemp; })] as [number, number])
            .range([0, this.width]);
        svg.append("g")
            .attr("transform", "translate(0," + this.height + ")")
            .call(d3.axisBottom(x));

        // Add Y axis
        var y = d3.scaleLinear()
            .domain([0, d3.max(data, function (d: any) { return d.cyclistsTotal; })] as [number, number])
            .range([this.height, 0]);
        svg.append("g")
            .call(d3.axisLeft(y));

        // Add dots
        svg.append('g')
            .selectAll("dot")
            .data(data)
            .enter()
            .append("circle")
            .attr("cx", function (d: any) { return x(d.avgTemp); })
            .attr("cy", function (d: any) { return y(d.cyclistsTotal); })
            .attr("r", 1.5)
            .style("fill", "#69b3a2")
    }
}