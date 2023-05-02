layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    $("#closeBtn").click(function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    })

    //
    // $.post(ctx + "/user/queryAllCustomerManager", function (res) {
    //     for (var i = 0; i < res.length; i++) {
    //         if ($("input[name='man']").val() == res[i].id) {
    //             $("#assignMan").append("<option value=\"" + res[i].id + "\" selected='selected' >" + res[i].name + "</option>");
    //         } else {
    //             $("#assignMan").append("<option value=\"" + res[i].id + "\">" + res[i].name + "</option>");
    //         }
    //     }
    //     //重新渲染
    //     layui.form.render("select");
    // });


    form.on('submit(addOrUpdateSaleChance)', function (data) {
        console.log(data.field);
        var index = top.layer.msg("数据提交中,请稍后...", {icon: 16, time: false, shade: 0.8});
        var url = ctx + "/sale_chance/save";
        if ($("input[name='id']").val()) {
            url = ctx + "/sale_chance/update";
        }

        $.post(url, data.field, function (res) {
            if (res.code == 200) {
                top.layer.msg("操作成功");
                top.layer.close(index);
                layer.closeAll("iframe");
                // 刷新父页面
                parent.location.reload();
            } else {
                layer.msg(res.msg);
            }
        });
        return false;
    });


    $.ajax({
        type:"get",
        url:ctx + "/user/queryAllSales",
        data:{},
        success:function (data){
            console.log(data);
            if(data != null){
                for (var i = 0; i < data.length; i++) {
                    var opt = "<option value="+data[i].id+">"+data[i].user_name+"</option>";
                    //将opt设置到select中
                    $("#assignMan").append(opt);

                }
            }
            //     重写渲染页面
            layui.form.render("select");
        }

    })


});