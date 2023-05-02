layui.use(['table','layer',"form"],function(){
       var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    //用户列表展示
    var  tableIns = table.render({
        elem: '#userList',
        url : ctx+'/user/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "userListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'用户id',fixed:"true"},
            {field: "userName", title:'用户名',fixed:"true"},
            {field: "trueName", title:'真实姓名',fixed:"true"},
            {field: "email", title:'邮箱',fixed:"true"},
            {field: "phone", title:'电话',fixed:"true"},
            {field: "createDate", title:'创建时间',fixed:"true"},
            {field: "updateDate", title:'修改时间',fixed:"true"},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#userListBar"}
        ]]
    });

    // 多条件搜索
    $(".search_btn").on("click",function(){

        table.reload("userListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {

                //"input[name='customerName']"  标签选择器+name选择器
                userName: $("input[name='userName']").val(),  //用户名
                email: $("input[name='email']").val(),  // 邮箱
                phone: $("input[name='phone']").val(),  // 手机号
            }
        })

    });

    //监听头部工具栏
    table.on('toolbar(users)',function (tableData) {
        console.log("头部栏监听……")
        if(tableData.event == "add"){
            //打开添加页面
            openAddOrUpdateUserDialog();

        }else if(tableData.event =="del"){
            // 获取表格选择项
            var chekStatus = table.checkStatus('userListTable');
            delUser(chekStatus.data);
        }
    })

    //行监听 编辑按钮、删除按钮

    table.on('tool(users)',function (tableData) {
        console.log("行监听")
        if (tableData.event == "edit"){//编辑
            // console.log(tableData);
            // console.log(tableData.data);
            openAddOrUpdateUserDialog(tableData.data.id);
        }else if(tableData.event == "del"){//删除
            layer.confirm("是否要删除用户数据",{icon:3},function (index){
                $.ajax({
                    type:"post",
                    url: ctx + "/user/delete",
                    data:{ userIds:tableData.data.id},
                    success:function (res){
                        if (res.code == 200){
                            layer.msg("删除成功",{icon:6});
                            tableIns.reload();
                        }else {
                            layer.msg(res.msg,{icon:5});
                        }
                    }
                })
            });


        }
    })


    function openAddOrUpdateUserDialog(userId) {
        console.log(userId);
        console.log(userId != null && userId != '');

            //设置访问路径
        var url = ctx+ "/user/userAddOrUpdate";
        var title = "用户管理-用户添加"
        //判断是添加还是更新
        if(userId != null && userId != ''){
            title= "用户管理-用户更新"
            url += "?userId="+userId;
        }

        layui.layer.open({
            title : title,
            type : 2,
            area:["700px","400px"],
            maxmin:true,
            content : url
        });
        
    }

    //删除数据
    function delUser(data) {
        //删除数据判断
        if(data.length == 0){
            layer.msg("请选择删除记录！")
            return
        }
        layer.confirm("是否要删除选定用户数数据",{
            btn:['确定','取消']
        },function (index) {
            layer.close(index)
            var userIds = "userIds";

            for (var i = 0; i <data.length ; i++) {
                if(i < data.length-1){
                    userIds += data[i].id + "&userIds=";
                }else{
                    userIds += data[i].id;
                }
            }

            $.ajax({
                type:"post",
                url: ctx+"/user/delete",
                data:userIds,
                success:function (res){
                    if (res.code == 200){
                        layer.msg("删除成功",{icon:6});
                        tableIns.reload();
                    }else {
                        layer.msg(res.msg,{icon:5});
                    }
                }
            })
        })



    }





});
