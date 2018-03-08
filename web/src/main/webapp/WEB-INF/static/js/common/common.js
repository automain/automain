/**
 * 弹窗最大化
 * @param layer
 * @param title
 * @param url
 */
function alertByFull(layer, title, url) {
    layer.open({
        title: title,
        type: 2,
        shift: 0,
        area: ['100%', '100%'],
        content: url,
        success: function (layero, index) {
            layer.full(index);
        }
    });
}

/**
 * 判断是否为空
 * @param data
 * @returns {boolean}
 */
function checkIsNotNull(data) {
    return data != "" && data != undefined && data != null;
}

/**
 * 全选关联
 * @param form
 * @param all_delete_label
 * @param delete_label
 * @param allDeleteLabel
 * @param deleteLabel
 */
function checkIsAllCheck(form, all_delete_label, delete_label, allDeleteLabel, deleteLabel) {
    form.on('checkbox(' + all_delete_label + ')', function (data) {
        var child = $(data.elem).parents('table').find('tbody input[name=' + deleteLabel + ']');
        child.each(function (index, item) {
            item.checked = data.elem.checked;
        });
        form.render('checkbox');
    });
    form.on('checkbox(' + delete_label + ')', function (data) {
        var child = $(data.elem).parents('table').find('tbody input[name=' + deleteLabel + ']');
        var allChecked = true;
        child.each(function (index, item) {
            if (!item.checked) {
                allChecked = false;
            }
        });
        $('thead input[name=' + allDeleteLabel + ']')[0].checked = allChecked;
        form.render('checkbox');
    });
}

/**
 * 删除
 * @param deleteLabel
 * @param url
 * @param fun
 */
function doDelete(layer, deleteLabel, url, fun) {
    var deleteCheck = new Array();
    $("input[name=" + deleteLabel + "]:checked").each(function () {
        deleteCheck.push($(this).val());
    });
    if (deleteCheck.length == 0) {
        layer.msg("请选择删除项");
    } else {
        $.ajax({
            url: url,
            type: "post",
            traditional: true,
            data: {
                deleteCheck: deleteCheck
            },
            dataType: "json",
            success: function (data) {
                layer.msg(data.msg);
                if (data.code == code_success) {
                    fun;
                }
            }
        });
    }
}

/**
 * 分页
 * @param laypage
 * @param page_label
 * @param count
 * @param curr
 * @param fun
 */
function renderPage(laypage, page_label, count, curr, fun) {
    laypage.render({
        elem: page_label
        , count: count
        , curr: curr
        , layout: ['prev', 'page', 'next', 'count', 'skip']
        , jump: function (obj, first) {
            if (!first) {
                fun(obj.curr);
            }
        }
    });
}

/**
 * 公告
 * @param layer
 * @param noticeStartTime
 * @param noticeEndTime
 * @param noticeTitle
 * @param noticeContent
 */
function alertNotice(layer, noticeStartTime, noticeEndTime, noticeTitle, noticeContent) {
    var content = '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;">网站将在以下时间段进行升级维护:<br/>'
        + noticeStartTime + ' -- ' + noticeEndTime + '<br/><br/>本次升级内容:<br/>' + noticeContent + '</div>';
    if (checkIsNotNull(noticeStartTime)) {
        layer.open({
            type: 1
            ,title: noticeTitle
            ,closeBtn: false
            ,area: '400px;'
            ,shade: 0.8
            ,id: 'notice-id'
            ,btn: ['知道了']
            ,btnAlign: 'c'
            ,moveType: 1
            ,content: content
            ,success: function(layero){
            }
        });
    };
}