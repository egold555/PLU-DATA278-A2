import * as d3 from "d3";
export abstract class AbstractDataViz {

    width: number;
    height: number;
    margin: { top: number, right: number, bottom: number, left: number };

    abstract getKeys(): string[];
    abstract getKeyColors(): string[];

    abstract getCSVName(): string;
    abstract getGraphTitle(): string;

    abstract parse(data: d3.DSVRowString<string>): Object;
    abstract draw(data: d3.DSVParsedArray<Object>, svg: d3.Selection<d3.BaseType, unknown, HTMLElement, any>): void;

    public go(): void {

        this.margin = { top: 10, right: 0, bottom: 30, left: 100 };
        this.width = window.innerWidth - this.margin.left - this.margin.right;
        this.height = window.innerHeight - this.margin.top - this.margin.bottom;

        let svg = d3.select("#dataviz")
            .append("svg")
            .attr("width", this.width + this.margin.left + this.margin.right)
            .attr("height", this.height + this.margin.top + this.margin.bottom)
            .append("g")
            .attr("transform",
                "translate(" + this.margin.left + "," + this.margin.top + ")");

        d3.csv("data/" + this.getCSVName() + ".csv", null,
            (data) => {
                return this.parse(data);
            })
            .then((data) => {
                this.draw(data, svg);
                this.drawKeys(svg);
                this.drawTitle(svg);
            });

    }

    private drawKeys(svg: d3.Selection<d3.BaseType, unknown, HTMLElement, any>): void {
        let cx: number = 100;
        let cy: number = 50;
        let yOff: number = 30;
        for (let i = 0; i < this.getKeys().length; i++) {
            svg.append("circle").attr("cx", cx).attr("cy", cy + (i * yOff)).attr("r", 6).style("fill", this.getKeyColors()[i])
            svg.append("text").attr("x", cx + 20).attr("y", cy + (i * yOff)).text(this.getKeys()[i]).style("font-size", "15px").attr("alignment-baseline", "middle")
        }
    }

    private drawTitle(svg: d3.Selection<d3.BaseType, unknown, HTMLElement, any>): void {
        svg.append("text")
            .attr("x", (this.width / 2))
            .attr("y", 10 - (this.margin.top / 2))
            .attr("text-anchor", "middle")
            .style("font-size", "16px")
            .style("text-decoration", "underline")
            .text(this.getGraphTitle());
    }
}