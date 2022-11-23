




function loadStatesForCountry() {
    url = contextPath + "/api/tour";

    $.get(url, function (responseJson) {
        dataListState.empty();
        $.each(responseJson, function (index, state) {
            $("<option>").val(state.name).text(state.name).appendTo(dataListState);
        })
    }).fail(function () {
        alert("Failed to connect to the server")
    })
}