// layui.use(['form','jquery','jquery_cookie'], function () {
//     var form = layui.form,
//         layer = layui.layer,
//         $ = layui.jquery,
//         $ = layui.jquery_cookie($);
//     // 进行登录操作
//     form.on('submit(login)', function (data) {
//         data = data.field;
//         if ( data.username =="undefined" || data.username =="" || data.username.trim()=="") {
//             layer.msg('用户名不能为空');
//             return false;
//         }
//         if ( data.password =="undefined" || data.password =="" || data.password.trim()=="")  {
//             layer.msg('密码不能为空');
//             return false;
//         }
//         $.ajax({
//             type:"post",
//             url:ctx+"/user/login",
//             data:{
//                 userName:data.username,
//                 userPwd:data.password
//             },
//             dataType:"json",
//             success:function (data) {
//                 if(data.code==200){
//                     layer.msg('登录成功', function () {
//                         var result =data.result;
//                         $.cookie("userIdStr",result.userIdStr);
//                         $.cookie("userName",result.userName);
//                         $.cookie("trueName",result.trueName);
//                         // 如果点击记住我 设置cookie 过期时间7天
//                         if($("input[type='checkbox']").is(':checked')){
//                             // 写入cookie 7天
//                             $.cookie("userIdStr",result.userIdStr, { expires: 7 });
//                             $.cookie("userName",result.userName, { expires: 7 });
//                             $.cookie("trueName",result.trueName, { expires: 7 });
//                         }
//                         window.location.href=ctx+"/main";
//                     });
//                 }else{
//                     layer.msg(data.msg);
//                 }
//             }
//         });
//         return false;
//     });
// });

//bySelf

layui.use(['form', 'jquery', 'jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    form.on('submit(login)', function (data) {

        console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
        // console.log(data.ctx)
        $.ajax({
            type: "post",
            url: ctx + "/user/login",
            data: {
                userName: data.field.username,
                userPwd: data.field.password
            },
            success: function (result) {
                console.log(data);
                console.log(result);

                if (result.code == 200) {
                    //登录成功，设置cookie信息
                    layer.msg("用户登录成功",function () {
                        var res = result.result;
                        $.cookie("userIdStr",res.userIdStr);
                        $.cookie("userName",res.userName);
                        $.cookie("trueName",res.trueName);
                        if($("input[type='checkbox']").is(":checked")){
                            $.cookie("userIdStr",res.userIdStr,{expires:7});
                            $.cookie("userName",res.userName,{expires:7});
                            $.cookie("trueName",res.trueName,{expires:7});
                        }
                        window.location.href=ctx+"/main";
                    })


                } else {
                    //登录失败
                    layer.msg(result.msg, {icon: 5})
                }
            }

        })


        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });


})