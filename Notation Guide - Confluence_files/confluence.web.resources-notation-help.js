AJS.toInit(function ($) {

    var sectionDivs = $(".help-section-div");
    var sectionLinks = $(".help-section-link");

    // Iterates over all help section divs, showing/hiding those relevant
    var showHideDivs = function (currentSection, hideAll) {
        sectionDivs.each(function () {
            var isVisible = !hideAll && (currentSection == "help-section-div-all" || currentSection == this.id);
            AJS.setVisible(this, isVisible);
        });
    };

    // Add click handlers to section menu items to show/hide divs and update bookmark.
    sectionLinks.each( function () {
        var thisSectionLink = $(this);
        var thisSectionKey = thisSectionLink[0].id.replace("help-section-link-", "");
        var thisSectionDiv = "help-section-div-" + thisSectionKey;

        thisSectionLink.click(function (e) {
            var hideAll = thisSectionLink.hasClass("selected"); // User clicked on already-selected section - hide all.
            var bookmarkSection = hideAll ? "" : thisSectionKey;

            // Store current section at back end.
            jQuery.post(
                AJS.params.contextPath + "/notationhelp-bookmark.action",
                {section: bookmarkSection}
            );

            sectionLinks.removeClass("selected");
            if (!hideAll) {
                thisSectionLink.addClass("selected");
            }
            showHideDivs(thisSectionDiv, hideAll);

            return AJS.stopEvent(e);
        });
    });

    // Show/hide lefthand-nav for printing
    $("#print-friendly-help-toggle").click(function () {
        $("#lefthand-nav").toggle();
    });
});