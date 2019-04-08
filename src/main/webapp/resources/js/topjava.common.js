let context, form;

function makeEditable(ctx) {
    context = ctx;
    form = $('#detailsForm');
    $(".delete").click(function () {
        if (confirm('Are you sure?')) {
            let parent = $(this).parents("." + ctx.entity);
            if (parent.length) {
                deleteRow(parent.attr("id"));
            }
        }
    });
    $(".edit").click(function () {
        let parent = $(this).parents("." + ctx.entity);
        if (parent.length) {
            editRow(parent.attr("id"));
        }
    });

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});
}

function add() {
    form.find(":input").val("");
    $("#editRow").modal();
}

function editRow(id) {
    let curRow = $("#datatable").find("#" + id);
    if (!curRow.length) return;
    let cols = context.formColumns;

    form.find("#id").val(id);
    cols.forEach(function (x) {
        let elem = form.find("#" + x);
        if (elem.length) {
            if (x === "enabled") {
                elem.val(curRow.find("." + x).val());
            } else {
                elem.val(curRow.find("." + x).text());
            }
        }
    });
    $("#editRow").modal();
}

function deleteRow(id) {
    $.ajax({
        url: context.ajaxUrl + id,
        type: "DELETE"
    }).done(function () {
        updateTable();
        successNoty("Deleted");
    });
}

function updateTable() {
    let params = {}, url = context.ajaxUrl;
    if (context.entity === "meal") {
        let filterForm = $("#filter");
        url += "filter";
        params = filterForm.serialize();
    }
    $.get(url, params, function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
}

function save() {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        updateTable();
        successNoty("Saved");
    });
}

function switchEnabled(id, enabled) {
    if (context.entity === "user") {
        $.ajax({
            type: "POST",
            url: context.ajaxUrl + "switch",
            data: {id: id, enabled: enabled}
        }).done(function () {
            successNoty("Switched");
        });
    }
}

function clearFilter() {
    $("#filter").find(":input").val("");
}

function filter() {
    /*let filterForm = $("#filter");
    $.ajax({
        type: "GET",
        url: context.ajaxUrl + "filter",
        data: filterForm.serialize()
    }).done(function () {
        updateTable();
    });*/
    updateTable();
}

let failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(text) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + text,
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;Error status: " + jqXHR.status,
        type: "error",
        layout: "bottomRight"
    }).show();
}