function sidemenu(){
    $.ajax({
        url: "sidemenu.html",
        cache: false,
        success: function(html){
            document.write(html);
        }
    });
}