let svg;
let data = {};
let attrs = {
    svgWidth: 750,
    svgHeight: 400,
    transform: { x: 0, y: 0, k: 1 },
    scaleMin: 0.033,
    scaleMax: 2
};

const loadData = async () => {
    const nodes = await fetch('http://localhost:8080/tsp/api/nodes');
    const solution = await fetch('http://localhost:8080/tsp/api/tour');
    data.nodes = await nodes.json(); //extract JSON from the http response
    data.solution = await solution.json();
    initGraph(data);
}

function initGraph({nodes, solution}) {
    let nodeArray = [];
    for (let index in nodes) {
        nodeArray.push(nodes[index]);
    }
    let linkArray = [];
    for (let i = 0; i<solution.length-1; i++) {
        linkArray.push({
            source: nodeArray.find(d => d.id === solution[i]),
            target: nodeArray.find(d => d.id === solution[i+1])
        });
    }
    linkArray.push({
        source: nodeArray.find(d => d.id === solution[nodeArray.length-1]),
        target: nodeArray.find(d => d.id === solution[0])
    });
    console.log(nodeArray);
    console.log(linkArray);

    renderGraph(nodeArray, linkArray);
}

function renderGraph(nodeArray, linkArray) {
    let behaviours = {};

    svg = d3.select("#tsp_graph").classed("svg-container", true).append("svg")
        .attr("preserveAspectRatio", "xMinYMin meet")
        .attr("viewBox", `0 0 ${attrs.svgWidth} ${attrs.svgHeight}`)
        .classed("svg-content-responsive", true);

    let svgWrapper = svg.append('g').attr("class", 'svg-wrapper');
    let linksWrapper = svgWrapper.append('g').attr("class", 'links-wrapper');
    let nodesWrapper = svgWrapper.append('g').attr("class", 'nodes-wrapper');

    //########################### BEHAVIORS #########################
    behaviours.zoom = d3.zoom()
        .scaleExtent([attrs.scaleMin, attrs.scaleMax])
        .on("zoom", onZoom);
    svg.call(behaviours.zoom).on("dblclick.zoom", onZoomReset);
    onZoomReset() // call zoomReset once on initialization;

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

    let links = linksWrapper.selectAll(".link-group").data(linkArray);
    let linksEntering = links.enter().append("g")
        .attr("class", "link-group")

    linksEntering.append("line")
        .attr("x1", d => d.source.x)
        .attr("y1", d => d.source.y)
        .attr("x2", d => d.target.x)
        .attr("y2", d => d.target.y)


    // ######### ZOOM FUNCTIONS ###########
    function onZoom(event) {
        attrs.transform = event.transform;
        svgWrapper.attr("transform", attrs.transform);
    }

    function onZoomReset() {
        svg.transition().duration(500)
            .call(behaviours.zoom.transform, d3.zoomIdentity.translate(0,0).scale(attrs.scaleMin));
    }
}

loadData();