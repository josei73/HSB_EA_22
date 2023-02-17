let svg, svgWrapper, linksWrapper, nodesWrapper, axisWrapper, distanceWrapper, hullWrapper;
let xAxis, yAxis;
let xScale, yScale;
let data = {};
let behaviours = {}
let attrs = {
    svgWidth: 750,
    svgHeight: 400,
    transform: {x: 0, y: 0, k: 1},
    margin: { bottom: 20, left: 40 },
    scaleMin: 0.2,
    scaleMax: 5
};

const loadData = async () => {
    const url = `http://localhost:8080/tsp/api/problems/models`
    data = await fetch(url).then(response => response.json());
}

function addTour(tour, nodeArray) {
    let linkArray = []
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
    return linkArray
}

function initGraph() {
    console.log("init graph!")
    //########################### WRAPPERS #########################
    svg = d3.select("#tsp_graph").classed("svg-container", true).append("svg")
        .attr("preserveAspectRatio", "xMinYMin meet")
        .attr("viewBox", `0 0 ${attrs.svgWidth} ${attrs.svgHeight}`)
        .classed("svg-content-responsive", true);

    hullWrapper = svg.append('g').attr("class", 'hull-wrapper');
    svgWrapper = svg.append('g').attr("class", 'svg-wrapper');
    axisWrapper = svg.append('g').attr("class", 'axis-wrapper');
    linksWrapper = svgWrapper.append('g').attr("class", 'links-wrapper');
    distanceWrapper = svgWrapper.append('g').attr("class", 'distance-wrapper');
    nodesWrapper = svgWrapper.append('g').attr("class", 'nodes-wrapper');

    //########################### BEHAVIORS #########################
    behaviours.zoom = d3.zoom()
        .scaleExtent([attrs.scaleMin, attrs.scaleMax])
        .on("zoom", onZoom);
    svg.call(behaviours.zoom).on("dblclick.zoom", onZoomReset);
    onZoomReset() // call zoomReset once on initialization;

    renderGraph({});
}

function renderGraph(selection) {
    console.log("renderGraph function called")
    let nodes, links, numberOfNodes, tour;
    let nodeArray = [];
    let linkArray = [];
    let distanceArray = [];

    nodesWrapper.selectAll(".node-group").remove();
    linksWrapper.selectAll(".link-group").remove();
    distanceWrapper.selectAll(".distance-link-group").remove();
    axisWrapper.selectAll("g").remove();
    hullWrapper.selectAll("polygon").remove();

    if (!$.isEmptyObject(selection)) {
        let model = data.find(d => d.name === selection);
        nodes = model.nodes;
        links = model.links;
        numberOfNodes = model.numberOfNodes;
        tour = model.tour;
    }

    if ($.isEmptyObject(nodes) && !$.isEmptyObject(links)) {
        for (let i=0; i<numberOfNodes; i++) {
            nodeArray.push({ id: i+1, x:0, y:0 });
        }
        distanceArray = links;
    } else {
        for (let index in nodes) {
            nodeArray.push(nodes[index]);
        }
    }
    linkArray = addTour(tour, nodeArray);
    updateGraph(nodeArray, linkArray, distanceArray);
}

function updateGraph(nodeArray, linkArray, distanceArray) {
    //################### INITIALIZE FORCE GRAPH #####################
    if(distanceArray.length>0) {
        const simulation = d3.forceSimulation(nodeArray)
            .force("link", d3.forceLink(distanceArray).id(d => d.id).distance(d => d.distance))
            .force("charge", d3.forceManyBody().strength(-5))
            .force("center",  d3.forceCenter(attrs.svgWidth / 2, attrs.svgHeight / 2))
            .force('collision', d3.forceCollide().radius(5))
        simulation.restart();
        simulation.tick(200);
        simulation.stop();
    }
    //########################### AXIS #########################
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

    // ########################### ISLAND ##########################
    const hull = hullWrapper.append("polygon")
        .attr("points", polygonString(convexHull(nodeArray)));

    //########################### GRAPH #########################
    //########################### NODES #########################
    const nodes = nodesWrapper.selectAll(".node-group").data(nodeArray);
    const nodesEntering = nodes.enter().append("g")
        .attr("class", "node-group")

    nodesEntering.append("circle")
        .attr("cx", d => xScale(d.x))
        .attr("cy", d => yScale(d.y))
        .attr("r", rescaleNodeSize(nodeArray.length))
        .style("fill", nodeArray.length < 500 ? "white" : "black")
        .style("stroke", nodeArray.length < 500 ? "black" : "white")
        .style("stroke-width", rescaleNodeSize(nodeArray.length) / 10)

    if(nodeArray.length < 100) {
        nodesEntering.append("text")
            .text(d => d.id)
            .attr("x", d => xScale(d.x))
            .attr("y", d => yScale(d.y) + 3);
    }

    //########################### LINKS #########################
    const links = linksWrapper.selectAll(".link-group").data(linkArray);
    const linksEntering = links.enter().append("g")
        .attr("class", "link-group")

    linksEntering.append("line")
        .attr("x1", d => xScale(d.source.x))
        .attr("y1", d => yScale(d.source.y))
        .attr("x2", d => xScale(d.target.x))
        .attr("y2", d => yScale(d.target.y))
        .style("stroke-width", nodeArray.length < 100 ? 2 : 1)

    //############## DISTANCE LINKS (FORCE GRAPH ONLY)) ##############
    const distanceLinks = distanceWrapper.selectAll(".distance-link-group").data(distanceArray);
    const distanceLinksEntering = distanceLinks.enter().append("g")
        .attr("class", "distance-link-group")

    distanceLinksEntering.append("line")
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

    svg.style("background-size", `${attrs.transform.k * 20}%`);
    svg.style("background-position", `${attrs.transform.x}px ${attrs.transform.y}px`)

    svgWrapper.attr("transform", attrs.transform);
    hullWrapper.attr("transform", attrs.transform);

    let new_xScale = event.transform.rescaleX(xScale);
    let new_yScale = event.transform.rescaleY(yScale);
    d3.select(".x-axis").call(xAxis.scale(new_xScale));
    d3.select(".y-axis").call(yAxis.scale(new_yScale));
}

function onZoomReset() {
    svg.transition().duration(500)
        .call(behaviours.zoom.transform, d3.zoomIdentity.translate(attrs.margin.left, attrs.margin.bottom).scale(0.9));
}

// ############# UI FUNCTIONS ##################
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

// ################ HELPER FUNCTIONS #####################
function rescaleNodeSize(length) {
    length = 50/(length/10)
    if (length < .25) return .5
    if (length > 7) return 7
    return length
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

function convexHull(points) {
    // Step 1: Sort the points lexicographically
    points.sort(function(a, b) {
        return a.x - b.x || a.y - b.y;
    });

    // Step 2: Compute the lower hull
    var lower = [];
    for (var i = 0; i < points.length; i++) {
        while (lower.length >= 2 &&
        cross(lower[lower.length - 2], lower[lower.length - 1], points[i]) <= 0) {
            lower.pop();
        }
        lower.push(points[i]);
    }

    // Step 3: Compute the upper hull
    var upper = [];
    for (var i = points.length - 1; i >= 0; i--) {
        while (upper.length >= 2 &&
        cross(upper[upper.length - 2], upper[upper.length - 1], points[i]) <= 0) {
            upper.pop();
        }
        upper.push(points[i]);
    }

    // Step 4: Remove duplicate points
    upper.pop();
    lower.pop();

    // Step 5: Combine the lower and upper hulls
    return lower.concat(upper);
}

// Helper function to compute the cross product of three points
function cross(a, b, c) {
    return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
}

function polygonString(points) {
    let polygon = "";
    for (let i = 0; i < points.length; i++) {
        polygon += xScale(points[i].x) + "," + yScale(points[i].y);
        if (i < points.length - 1) {
            polygon += ",";
        }
    }
    return polygon
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
    }).fail(function (responseError) {
        alert(responseError.responseJSON.message)
    })

}