$(function () {
    makeEditable({
            entity: "meal",
            ajaxUrl: "ajax/meals/",
            formColumns: ["dateTime","description","calories"],
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "desc"
                    ]
                ]
            })
        }
    );
});

function clearFilter() {
    $("#filter").find(":input").val("");
    updateTableGet();
}
function filter() {
    let filterForm = $("#filter");
    $.ajax({
        type: "GET",
        url: context.ajaxUrl + "filter",
        data: filterForm.serialize()
    }).done(function (data) {
        updateTable(data);
    });
}