let svg;

let attrs = {
    data: null,
    svgWidth: 960,//document.getElementById('#tsp_graph').clientWidth,
    svgHeight: 600,//document.getElementById('#tsp_graph').clientHeight,
    zoomSpeed: 3000,
};

const loadData = async () => {
    const response = await fetch('http://localhost:8080/tsp/api/nodes');
    attrs.data = await response.json(); //extract JSON from the http response
    renderGraph(attrs.data);
}


function renderGraph(data) {
    let nodeArray = [];
    for (let id in attrs.data) {
        nodeArray.push(attrs.data[id]);
    }
    console.log(nodeArray);

    let behaviours = {};

    let svg = d3.select("#tsp_graph").append("svg")
        .attr("width", attrs.svgWidth)
        .attr("height", attrs.svgHeight)
        .style("background", "lightgrey");

    let svgWrapper = svg.append('g').attr("class", 'svg-wrapper');
    let nodesWrapper = svgWrapper.append('g').attr("class", 'nodes-wrapper');

    //########################### BEHAVIORS #########################
    behaviours.zoom = d3.zoom()
        .scaleExtent([
            [-attrs.svgWidth,-attrs.svgHeight],
            [2*attrs.svgWidth,2*attrs.svgHeight]
        ])
        .on('zoom', function(event) {
            svgWrapper.selectAll('.nodes-wrapper')
                .attr('transform', event.transform);
        });
    svg.call(behaviours.zoom);

    let nodes = nodesWrapper.selectAll(".node-group").data(nodeArray)
    nodes.enter().append('circle')
        .attr("class", "node-group")
        .attr("cx", d => d.x / 10)
        .attr("cy", d => d.y / 10)
        .attr("r", 10)
        .attr('stroke', 'black')
        .attr('fill', '#69a3b2');

    // ######### ZOOM FUNCTIONS ###########
    function zoomed() {
        let transform = d3.event.transform;

        // apply transform event props to the wrapper
        svgWrapper.attr('transform', transform)

        svgWrapper.selectAll('.node-group').attr("transform", function (d)
        {
            return `translate(${d.x},${d.y}) scale(${1 / transform.k})`;
        });
    }
}

loadData();