$(function () {
    makeEditable({
            entity: "meal",
            ajaxUrl: "ajax/meals/",
            formColumns: ["dateTime","description","calories"],
            clearFilter: function () {
                $("#filter").find(":input").val("");
            },
            filter: function () {
                let filterForm = $("#filter");
                $.ajax({
                    type: "GET",
                    url: context.ajaxUrl + "filter",
                    data: filterForm.serialize()
                }).done(function (data) {
                    updateTable(data);
                });
            },
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