WebFontConfig = {
    google: { families: [ 'Playfair+Display:900,700italic:latin', 'PT+Mono::latin', 'Cardo::latin' ] }
};
(function() {
    var wf = document.createElement('script');
    wf.src = ('https:' == document.location.protocol ? 'https' : 'http') +
        '://ajax.googleapis.com/ajax/libs/webfont/1/webfont.js';
    wf.type = 'text/javascript';
    wf.async = 'true';
    var s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(wf, s);
})();

(function($) {
    $('[name="paras"]').on('change', function(event) {
        $.getJSON("/" + $(event.target).val(), function(json) {
            var paragraphs = "";
            $.each(json, function(index, item) {
                paragraphs += "<p>" + item + "</p>";
            });
            $('#ipsum').html(paragraphs);
        });
    });
})(Zepto);