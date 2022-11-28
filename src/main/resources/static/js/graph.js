let svg;

let attrs = {
    data: null,
    svgWidth: 750,
    svgHeight: 400,
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

    let svg = d3.select("#tsp_graph").classed("svg-container", true).append("svg")
        .attr("preserveAspectRatio", "xMinYMin meet")
        .attr("viewBox", `0 0 ${attrs.svgWidth} ${attrs.svgHeight}`)
        .classed("svg-content-responsive", true);

    let svgWrapper = svg.append('g').attr("class", 'svg-wrapper');
    let nodesWrapper = svgWrapper.append('g').attr("class", 'nodes-wrapper');

    //########################### BEHAVIORS #########################
    behaviours.zoom = d3.zoom()
        .scaleExtent([0.1, 3])
        .on("zoom", onZoom);
    svg.call(behaviours.zoom)//.on("dblclick.zoom", onZoomReset);

    //########################### GRAPH #########################
    let nodes = nodesWrapper.selectAll(".node-group").data(nodeArray);
    let nodesEntering = nodes.enter().append("g")
        .attr("class", "node-group")

    nodesEntering.append("circle")
        .attr("cx", d => d.x)
        .attr("cy", d => d.y)
        .attr("r", 20)

    nodesEntering.append("text")
        .text(d => d.id)
        .attr("x", d => d.x)
        .attr("y", d => d.y + 5);

    // ######### ZOOM FUNCTIONS ###########
    function onZoom(event) {
        const { transform } = event;
        nodesWrapper.attr("transform", transform);
    }

    function onZoomReset() {
        // TODO not working as intended -> fix
        nodesWrapper.transition().duration(500)
            .call(behaviours.zoom.transform , d3.zoomIdentity);
    }

}

loadData();