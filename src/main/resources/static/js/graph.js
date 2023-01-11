let svg, svgWrapper, linksWrapper, nodesWrapper;
let data = {};
let behaviours = {}
let attrs = {
    svgWidth: 750,
    svgHeight: 400,
    transform: { x: 0, y: 0, k: 1 },
    scaleMin: 0.033,
    scaleMax: 2
};

const loadData = async () => {
    const url = `http://localhost:8080/tsp/api/problems/models`
    data = await fetch(url).then(response => response.json());
    InitDropdown(data);
}

function InitDropdown() {
    console.log(data)
    const menu = d3.select("#dropProblems").selectAll("options").data(data)
    let menuEnter = menu.enter().append("option")
        .text(d => d.name)
        .on("click", function (event, d) { renderGraph(d); });
}

function initGraph() {
    console.log("init graph!")
    //########################### WRAPPERS #########################
    svg = d3.select("#tsp_graph").classed("svg-container", true).append("svg")
        .attr("preserveAspectRatio", "xMinYMin meet")
        .attr("viewBox", `0 0 ${attrs.svgWidth} ${attrs.svgHeight}`)
        .classed("svg-content-responsive", true);

    svgWrapper = svg.append('g').attr("class", 'svg-wrapper');
    linksWrapper = svgWrapper.append('g').attr("class", 'links-wrapper');
    nodesWrapper = svgWrapper.append('g').attr("class", 'nodes-wrapper');

    //########################### BEHAVIORS #########################
    behaviours.zoom = d3.zoom()
        .scaleExtent([attrs.scaleMin, attrs.scaleMax])
        .on("zoom", onZoom);
    svg.call(behaviours.zoom).on("dblclick.zoom", onZoomReset);
    onZoomReset() // call zoomReset once on initialization;

    renderGraph( {} );
}

//TODO rescale nodes with mix-max normalization
function renderGraph( model ) {
    let nodes = {}
    let tour = {}

    if(!jQuery.isEmptyObject(model)) {
        nodes = model.nodes;
        //tour = model.tour;
    }

    let linkArray = [];
    let nodeArray = [];
    for (let index in nodes) {
        nodeArray.push(nodes[index]);
    }

    // for (let i = 0; i<tour.length-1; i++) {
    //     linkArray.push({
    //         source: nodeArray.find(d => d.id === tour[i]),
    //         target: nodeArray.find(d => d.id === tour[i+1])
    //     });
    // }
    // linkArray.push({
    //     source: nodeArray.find(d => d.id === tour[nodeArray.length-1]),
    //     target: nodeArray.find(d => d.id === tour[0])
    // });
    updateGraph(nodeArray, linkArray);
}

function updateGraph(nodeArray, linkArray) {
    console.log(nodeArray);
    console.log(linkArray);
    //########################### GRAPH #########################
    let nodes = nodesWrapper.selectAll(".node-group").data(nodeArray);
    nodes.exit().remove();
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
    links.exit().remove();
    let linksEntering = links.enter().append("g")
        .attr("class", "link-group")

    linksEntering.append("line")
        .attr("x1", d => d.source.x)
        .attr("y1", d => d.source.y)
        .attr("x2", d => d.target.x)
        .attr("y2", d => d.target.y)
}

// ######### ZOOM FUNCTIONS ###########
function onZoom(event) {
    attrs.transform = event.transform;
    svgWrapper.attr("transform", attrs.transform);
}

function onZoomReset() {
    svg.transition().duration(500)
        .call(behaviours.zoom.transform, d3.zoomIdentity.translate(0,0).scale(attrs.scaleMin));
}

loadData();
initGraph();