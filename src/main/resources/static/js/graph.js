let svg, svgWrapper, linksWrapper, nodesWrapper, axisWrapper;
let xAxis, yAxis;
let xScale, yScale;
let data = {};
let behaviours = {}
let attrs = {
    svgWidth: 750,
    svgHeight: 400,
    transform: {x: 0, y: 0, k: 1},
    margin: { bottom: 20, left: 40 },
    scaleMin: 0.3,
    scaleMax: 5
};

const loadData = async () => {
    const url = `http://localhost:8080/tsp/api/problems/models`
    data = await fetch(url).then(response => response.json());
}

function initGraph() {
    console.log("init graph!")
    //########################### WRAPPERS #########################
    svg = d3.select("#tsp_graph").classed("svg-container", true).append("svg")
        .attr("preserveAspectRatio", "xMinYMin meet")
        .attr("viewBox", `0 0 ${attrs.svgWidth} ${attrs.svgHeight}`)
        .classed("svg-content-responsive", true);

    svgWrapper = svg.append('g').attr("class", 'svg-wrapper');
    axisWrapper = svg.append('g').attr("class", 'axis-wrapper');
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
    let nodes, tour;

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

    if (tour != null) {
        let tourNodes = tour.nodes;
        populateMetrics(tour);

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
    axisWrapper.selectAll("g").remove();

    updateGraph(nodeArray, linkArray);
}

function updateGraph(nodeArray, linkArray) {
    //########################### AXIS #########################
    //const minimum = getMinimum(nodeArray);
    const maximum = getMaximum(nodeArray);

    const xmin = d3.min(nodeArray, d => d.x);
    const ymin = d3.min(nodeArray, d => d.y);

    xScale = d3.scaleLinear()
        .domain([xmin, maximum])
        .range([attrs.margin.left, attrs.svgWidth-attrs.margin.left]);
    yScale = d3.scaleLinear()
        .domain([ymin, maximum])
        .range([attrs.svgHeight - attrs.margin.bottom,  attrs.margin.bottom]);

    xAxis = d3.axisBottom(xScale)
    yAxis = d3.axisLeft(yScale)

    axisWrapper.append("g")
        .attr("class", "x-axis")
        .attr("transform", `translate(0, ${attrs.svgHeight - attrs.margin.bottom})`)
        .call(xAxis);
    axisWrapper.append("g")
        .attr("class", "y-axis")
        .attr("transform", `translate(${attrs.margin.left}, 0)`)
        .call(yAxis);

    //########################### GRAPH #########################
    const nodes = nodesWrapper.selectAll(".node-group").data(nodeArray);
    const nodesEntering = nodes.enter().append("g")
        .attr("class", "node-group")

    const radius = rescaleNodeSize(nodeArray);
    nodesEntering.append("circle")
        .attr("cx", d => xScale(d.x))
        .attr("cy", d => yScale(d.y))
        .attr("r", radius)
        .attr("stroke", nodeArray.length < 1000 ? "black" : "none")

    nodesEntering.append("text")
        .text(d => d.id)
        .attr("opacity", nodeArray.length < 100 ? 1 : 0)
        .attr("x", d => xScale(d.x))
        .attr("y", d => yScale(d.y) + 3);

    const links = linksWrapper.selectAll(".link-group").data(linkArray);
    const linksEntering = links.enter().append("g")
        .attr("class", "link-group")

    linksEntering.append("line")
        .attr("x1", d => xScale(d.source.x))
        .attr("y1", d => yScale(d.source.y))
        .attr("x2", d => xScale(d.target.x))
        .attr("y2", d => yScale(d.target.y))

    onZoomReset();
    animateTour();

    //########################### DASH LINE ANIMATION #########################
    let offset = 1;
    setInterval( function() {
        linksEntering.selectAll('line')
            .style('stroke-dashoffset', offset);
        offset -= 1;
    }, 50);

    function animateTour() {
        let duration = $("#duration").val()
        let size = linkArray.length
        if (size > 0 && duration > 0) {
            size = 1 / size
            duration = Math.trunc(normalize(size,0,1) * duration * 500)

            linksWrapper.selectAll("line")
                .interrupt()
                .attr("x1", d => xScale(d.source.x))
                .attr("y1", d => yScale(d.source.y))
                .attr("x2", d => xScale(d.source.x))
                .attr("y2", d => yScale(d.source.y))
                .transition()
                .delay((d,i) => i * duration)
                .duration(duration)
                .ease(d3.easeQuadInOut)
                .attr("x1", d => xScale(d.source.x))
                .attr("y1", d => yScale(d.source.y))
                .attr("x2", d => xScale(d.target.x))
                .attr("y2", d => yScale(d.target.y))
        }
    }
}

// ######### ZOOM FUNCTIONS ###########
function onZoom(event) {
    attrs.transform = event.transform;
    svgWrapper.attr("transform", attrs.transform);

    let new_xScale = event.transform.rescaleX(xScale);
    let new_yScale = event.transform.rescaleY(yScale);
    d3.select(".x-axis").call(xAxis.scale(new_xScale));
    d3.select(".y-axis").call(yAxis.scale(new_yScale));
}

function onZoomReset() {
    svg.transition().duration(500)
        .call(behaviours.zoom.transform, d3.zoomIdentity.translate(attrs.margin.left, attrs.margin.bottom).scale(0.9));
}

function normalize(value, min, max) {
    return (value - min) / (max - min);
}

function getMinimum(nodeArray) {
    const xmin = d3.min(nodeArray, d => d.x);
    const ymin = d3.min(nodeArray, d => d.y);

    if (xmin < ymin) {
        return xmin;
    } else {
        return ymin;
    }
}

function getMaximum(nodeArray) {
    const xmax = d3.max(nodeArray, d => d.x);
    const ymax = d3.max(nodeArray, d => d.y);

    if (xmax > ymax) {
        return xmax;
    } else {
        return ymax;
    }
}

function populateMetrics(tour) {
    $("#costInput").val(tour.cost);
    $("#timeInput").val(setTime(tour.time));
}

function setTime(time) {
    if (time === undefined) return "";
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

function rescaleNodeSize(nodeArray) {
    let radius = 10;
    if (nodeArray.length > 50) radius = 5;
    if (nodeArray.length > 100) radius = 3;
    if (nodeArray.length > 500) radius = 2;
    if (nodeArray.length > 1000) radius = 1;
    return radius;
}

loadData();
initGraph();

async function loadSolution() {
    selectedInstance = $("#dropInstances option:selected");
    selectedAlgoNames = $("#dropAlgorithms option:selected")
    algoName = selectedAlgoNames.val()
    let model = data.find(d => d.name === selectedInstance.val());
    url = moduleURL + "api/algorithm/" + algoName + "/nodes/" + model.problemName;

    $.get(url, function (responseJson) {
        const index = data.findIndex((el) => el.name === responseJson.name)
        data[index] = responseJson
        renderGraph(responseJson.name);
    }).fail(function () {
        alert("Failed to connect to the server")
    })/*.error(function (error){
        alert(error)
    })*/
}