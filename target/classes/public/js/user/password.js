

layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    form.on('submit(saveBtn)', function(data){

        // console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
        // console.log(data.ctx)
        $.ajax({
            type:"post",
            url:ctx + "/user/updatePwd",
            data:{
                old_password:data.field.old_password,
                new_password:data.field.new_password,
                again_password:data.field.again_password
            },
            success:function (result) {
                console.log(result);

                if(result.code == 200){
                    layer.msg("修改成功,3s后跳转至登录界面",function () {
                        //修改成功，清空cookie
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                        //跳转到首页
                        window.location.href = ctx + "/index";
                    })



                }else {
                    //登录失败
                    layer.msg(result.msg,{icon:5})
                }
            }

        })


        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });




})