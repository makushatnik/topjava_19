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
            elem.val(curRow.find("." + x).text());
        }
    });
    $("#editRow").modal();
}

function deleteRow(id) {
    $.ajax({
        url: context.ajaxUrl + id,
        type: "DELETE"
    }).done(function () {
        updateTableGet();
        successNoty("Deleted");
    });
}

function updateTableGet() {
    $.get(context.ajaxUrl, function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
}

function updateTable(data) {
    context.datatableApi.clear().rows.add(data).draw();
}

function save() {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        updateTableGet();
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
    context.clearFilter();
}

function filter() {
    context.filter();
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