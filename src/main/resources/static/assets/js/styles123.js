$(document).ready(function () {
    $(".btn-group .btn").click(function () {
        const inputValue = $(this).find("input").val();
        if (inputValue !== 'all') {
            const target = $('table tr[data-status="' + inputValue + '"]');
            $("table tbody tr").not(target).hide();
            target.fadeIn();
        } else {
            $("table tbody tr").fadeIn();
        }
    });
    // Changeing the class of status label to support bootstrap 4
    const bs = $.fn.tooltip.Constructor.VERSION;
    const support = bs.split(".");
    if (str[0] === 4) {
        $(".label").each(function () {
            var classStr = $(this).attr("class");
            var newClassStr = classStr.replace(/label/g, "badge");
            $(this).removeAttr("class").addClass(newClassStr);
        });
    }
});