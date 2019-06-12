function createIngredient(){

    var $form = $("#newing");
    var data = getFormData($form);
    var datajson = JSON.stringify(data);

    console.log(datajson)
    $.ajax({
        url: 'rest/ingredient',
        method: 'POST',

        contentType: "application/json", // det vi sender er json
        data: datajson,
        success: function (datajson) {
            $("#bodytest").load("ingredientlist.html");
        },
        error: function (jqXHR){
            alert(jqXHR.responseText);
        }
    });

}

function loadIngredients() {
    $.get('rest/ingredient', function (data, textStatus, req) {
        $("#ingtablebody").empty();
        $.each(data, function (i, elt) {
            $('#ingtablebody').append(generateIngHTML(elt));
        });
    });
}
function generateIngHTML(ing) {
    return '<tr><td>' + ing.id + '</td>' +
        '<td>' + ing.name + '</td>';
}