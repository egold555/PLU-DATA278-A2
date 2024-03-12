import * as d3 from "d3";
import { AbstractDataViz } from "./AbstractDataViz";
export class CyclistsPerHour extends AbstractDataViz {

    getKeys(): string[] {
        return ["12 AM", "1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM", "7 AM", "8 AM", "9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM", "9 PM", "10 PM", "11 PM"];
    }

    getKeyColors(): string[] {
        //24 unique colors
        return ["#a9a9a9", "#2f4f4f", "#556b2f", "#483d8b", "#b22222", "#008000", "#9acd32", "#00008b", "#8b008b", "#48d1cc", "#ff4500", "#ffa500", "#ffff00", "#00ff00", "#00ff7f", "#00bfff", "#0000ff", "#ff00ff", "#1e90ff", "#db7093", "#eee8aa", "#ff1493", "#ffa07a", "#ee82ee"];
    }

    getCSVName(): string {
        return "NOAATempCyclist-CyclistsHour";
    }

    getGraphTitle(): string {
        return "Cyclists per hour";
    }

    parse(data: d3.DSVRowString<string>): Object {
        return {
            hour: +data.Hour,
            hour12: this.getKeys()[+data.Hour],
            totalCyclists: +data.TotalCyclists,
        }
    }
    draw(data: d3.DSVParsedArray<Object>, svg: d3.Selection<d3.BaseType, unknown, HTMLElement, any>): void {
        console.log(data);

        var this2 = this;

        var x = d3.scaleBand()
            .range([0, this.width])
            .domain(data.map(function (d: any) { return d.hour12; }))


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
            .attr("x", function (d: any) { return x(d.hour12); })
            .attr("y", function (d: any) { return y(d.totalCyclists); })
            .attr("width", x.bandwidth())
            .attr("height", function (d: any) { return this2.height - y(d.totalCyclists); })
            .attr("fill", function (d: any, i: number) { return this2.getKeyColors()[i]; })
    }
}