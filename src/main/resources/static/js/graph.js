let svg, svgWrapper, linksWrapper, nodesWrapper;
let data = {};
let behaviours = {}
let attrs = {
    svgWidth: 750,
    svgHeight: 400,
    transform: {x: 0, y: 0, k: 1},
    scaleMin: 0.5,
    scaleMax: 2
};

const loadData = async () => {
    const url = `http://localhost:8080/tsp/api/problems/models`
    data = await fetch(url).then(response => response.json());
    //InitDropdown(data);
}

function InitDropdown() {
    console.log(data)
    const menu = d3.select("#dropProblems").selectAll("options").data(data)
    let menuEnter = menu.enter().append("option")
        .text(d => d.name)
        .on("click", function (event, d) {
            renderGraph(d);
        });
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

    renderGraph({});
}

//TODO render a graph with adjacency matrix
//TODO draw a geo map if an instance has GEO data
function renderGraph(selection) {
    console.log("renderGraph function called")
    let nodes = {}
    let tour = {}


    if (!jQuery.isEmptyObject(selection)) {
        let model = data.find(d => d.name === selection);
        nodes = model.nodes;
        tour = model.tour;
    }

    let linkArray = [];
    let nodeArray = [];
    for (let index in nodes) {
        nodeArray.push(nodes[index]);
    }
    nodeArray = normalizeNodes(nodeArray);

    if (tour != null) {
        let tourNodes = tour.nodes;
        data.cost = tour.cost;
        data.time = tour.time;
        for (let i = 0; i < tourNodes.length - 1; i++) {
            linkArray.push({
                source: nodeArray.find(d => d.id === tourNodes[i]),
                target: nodeArray.find(d => d.id === tourNodes[i + 1])
            });
        }
        linkArray.push({
            source: nodeArray.find(d => d.id === tourNodes[nodeArray.length - 1]),
            target: nodeArray.find(d => d.id === tourNodes[0])
        });
    }

    nodesWrapper.selectAll(".node-group").remove();
    linksWrapper.selectAll(".link-group").remove();
    svg.selectAll(".metric-group").remove();
    updateGraph(nodeArray, linkArray);
}

function updateGraph(nodeArray, linkArray) {
    console.log(nodeArray);
    console.log(linkArray);
    //########################### GRAPH #########################
    let nodes = nodesWrapper.selectAll(".node-group").data(nodeArray);
    let nodesEntering = nodes.enter().append("g")
        .attr("class", "node-group")

    let radius = rescaleNodeSize(nodeArray);
    nodesEntering.append("circle")
        .attr("cx", d => d.x)
        .attr("cy", d => d.y)
        .attr("r", radius)
        .attr("stroke", nodeArray.length < 100 ? "black" : "none")

    nodesEntering.append("text")
        .text(d => d.id)
        .attr("opacity", nodeArray.length < 100 ? 1 : 0)
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

    let metrics = svg.append("g")
        .attr("class", "metric-group")

    metrics.append("text")
        .attr("x", 5)
        .attr("y", 20)
        .text(() => "cost:\t"+data.cost)

    metrics.append("text")
        .attr("x", 5)
        .attr("y", 40)
        .text(() => "time:\t"+setTime(data.time))

    onZoomReset();
}

// ######### ZOOM FUNCTIONS ###########
function onZoom(event) {
    attrs.transform = event.transform;
    svgWrapper.attr("transform", attrs.transform);
}

function onZoomReset() {
    svg.transition().duration(500)
        .call(behaviours.zoom.transform, d3.zoomIdentity.translate(30, 15).scale(0.9));
}

/**
 * find highest x & y values
 * normalize all nodes to [0,1] values
 * rescale to SVG size
 * @param nodeArray
 * @returns nodeArray
 */
function normalizeNodes(nodeArray) {
    let xMax = d3.max(nodeArray, d => d.x);
    let yMax = d3.max(nodeArray, d => d.y);
    let xMin = d3.min(nodeArray, d => d.x);
    let yMin = d3.min(nodeArray, d => d.y);
    nodeArray.forEach(function (d) {
        d.x = (d.x - xMin) / (xMax - xMin) * attrs.svgWidth;
        d.y = (d.y - yMin) / (yMax - yMin) * attrs.svgHeight;
    })

    return nodeArray
}

function setTime(time) {
    if (time < 1000) {
        return time + " ns"
    } else if (time < 1000000) {
        return time/1000 + " \u03BC"+"s"
    } else if (time < 1000000000) {
        return time / 1000000 + " ms"
    } else {
        return (time / 1000000000) + " s"
    }

}

//TODO reiterate rescaling for aesthetic reasons
function rescaleNodeSize(nodeArray) {
    let radius = 10;
    if (nodeArray.length > 100) {
        radius = 3;
    }
    if (nodeArray.length > 1000) {
        radius = 1;
    }
    return radius;
}

loadData();
initGraph();

async function loadSolution(selection) {
    selectedInstance = $("#dropInstances option:selected");
    selectedAlgoNames = $("#dropAlgorithms option:selected")
    algoName = selectedAlgoNames.val()
    let model = data.find(d => d.name === selectedInstance.val());
    url = moduleURL + "api/algorithm/" + algoName + "/nodes/" + model.problemName;
    console.log(url)


    $.get(url, function (responseJson) {
        console.log(responseJson)
        console.log("Render")
        const index = data.findIndex((el) => el.name === responseJson.name)
        data[index] = responseJson
        renderGraph(responseJson.name);
    }).fail(function () {
        alert("Failed to connect to the server")
    })

}