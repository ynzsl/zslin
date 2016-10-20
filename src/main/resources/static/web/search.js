$(function() {
    $(".model-search-div").each(function() {
        var thisObj = $(this);
        $.get("/public/articleCount", {}, function(res) {
            $(thisObj).find("input").attr("placeholder", "在 "+ res + " 篇文章中搜索");
        });
    });

    $(".model-search-div .search-btn").click(function() {
        var condition = $(this).parents(".input-group").find("input").val();
        if($.trim(condition)=='') {showDialog("可输入标题或内容搜索文章");}
        else {
            window.location.href = "/?condition="+condition;
        }
    });
});