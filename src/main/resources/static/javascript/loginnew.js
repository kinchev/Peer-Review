$(document).ready(function() {
    return $('.login').click(function(event) {
        return $('.state').html('<br><i class="fa fa-ban"></i><br><h2>Error</h2>The email or password you entered is incorrect, please try again.').css({
            color: '#eb5638'
        });
    });
});