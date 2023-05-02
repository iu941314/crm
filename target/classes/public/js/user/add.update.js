layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var formSelects = layui.formSelects;


    //取消按钮-关闭弹窗
    $("#closeBtn").click(function () {
        //关闭iframe页面
        var frameIndex = parent.layer.getFrameIndex(window.name);//获取当前窗口索引
        //通过索引关闭窗口
        parent.layer.close(frameIndex);
    })

    //确认按钮  表单提交
    //表单事件监听
    form.on("submit(addOrUpdateUser)",function (formData) {

        console.log("表单数据："+formData.field);
        var index = top.layer.msg(" 用户注册中……请稍候",{icon:16,time:false,shade:0.8});
        var url = ctx +"/user/add";
        // 隐藏域userId
        var userId = $("input[name='id']").val();

        console.log("用户id："+userId);


        if(userId !=null && userId != ""){
            url = ctx +"/user/update"
        }
        $.ajax({
            type:"post",
            url:url,
            data:formData.field,
            success:function (res) {
                if(res.code == 200){
                    top.layer.msg("操作成功");
                    //关闭当前iframe
                    top.layer.close(index);
                    //关闭所有窗口
                    layer.closeAll("iframe");
                    //刷新页面
                    parent.location.reload();
                }else {
                    layer.msg(res.msg);
                }
            }
        })
        return false;

    })
    var userId = $("[name='id']").val();
    //加载下拉框
    formSelects.config("selectId",{
        type:"post",
        searchUrl: ctx + "/role/queryAllRoles?userId="+userId,
        keyName: "roleName",
        keyVal:"id"
    },true);



});