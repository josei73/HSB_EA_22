let svg;

let attrs = {
    data: null,
    svgWidth: 600,//document.getElementById('graph').clientWidth,
    svgHeight: 400,//document.getElementById('graph').clientHeight,
    zoomSpeed: 3000,
};


svg = d3.select("#tsp_graph").append("svg");

$(document).ready(function () {
    if (window.data !== undefined) {
        renderGraph(data);
        window.addEventListener("resize", renderGraph);
    }
});

function renderGraph(data) {
    attrs.data = data;
    console.log(attrs.data);
}