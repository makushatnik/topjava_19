// $(document).ready(function () {
$(function () {
    makeEditable({
            entity: "user",
            ajaxUrl: "ajax/admin/users/",
            formColumns: ["name","email","enabled"],
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
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
                        "asc"
                    ]
                ]
            })
        }
    );
});

function switchEnabled(id) {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl + "switch",
        data: {id: id}
    }).done(function (enabled) {
        let user = $("#" + id);
        user.toggleClass("enabled", enabled);
        user.toggleClass("disabled", !enabled);
        user.find(".enabled").prop('checked', enabled);
        successNoty(enabled ? "Enabled" : "Disabled");
    });
}