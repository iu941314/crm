layui.use(['table','layer',"form"],function(){
       var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    //用户列表展示
    var  tableIns = table.render({
        elem: '#roleList',
        url : ctx+'/role/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "roleListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'角色id',fixed:"true"},
            {field: "roleName", title:'角色名',fixed:"true"},
            {field: "roleRemark", title:'备注',fixed:"true"},
            {field: "createDate", title:'创建时间',sort:"true",fixed:"true"},
            {field: "updateDate", title:'更新时间',sort:"true",fixed:"true"},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#roleListBar"}
        ]]
    });

    // 多条件搜索
    $(".search_btn").on("click",function(){

        table.reload("roleListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {

                //"input[name='customerName']"  标签选择器+name选择器
                roleName: $("input[name='roleName']").val(),  //角色名
                roleRemark: $("input[name='roleRemark']").val(),  // 备注
            }
        })

    });

    //监听头部工具栏
    table.on('toolbar(roles)',function (tableData) {
        console.log("头部栏监听……")
        console.log(tableData.config)
        if(tableData.event == "add"){
            //打开添加页面
            openAddOrUpdateDialog();

        }else if(tableData.event =="del"){
            // 获取表格选择项
            var chekStatus = table.checkStatus('roleListTable');
            deleRole(chekStatus.data);
        }else if(tableData.event == "grant"){
            //获取表格选中的记录数据
            var chekStatus = table.checkStatus('roleListTable');
            //打开授权对话框
            openAddGrantDialog(chekStatus.data);
        }
    })

    //行监听 编辑按钮、删除按钮

    table.on('tool(roles)',function (tableData) {
        console.log("行监听")
        if (tableData.event == "edit"){//编辑
            openAddOrUpdateDialog(tableData.data.id);
        }else if(tableData.event == "del"){//删除
            layer.confirm("是否要删除用户数据",{icon:3},function (index){
                $.ajax({
                    type:"post",
                    url: ctx + "/role/delete",
                    data:{ roleIds:tableData.data.id},
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


    function openAddOrUpdateDialog(roleId) {
        console.log(roleId);
        console.log(roleId != null && roleId != '');

            //设置访问路径
        var url = ctx+ "/role/addOrUpdateRolePage";
        var title = "用户角色管理-用户角色添加"
        //判断是添加还是更新
        if(roleId != null && roleId != ''){
            title= "用户管理-用户更新";
            url = ctx+ "/role/addOrUpdateRolePage?roleId="+roleId;
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
    function deleRole(data) {
        //删除数据判断
        if(data.length == 0){
            layer.msg("请选择删除记录！")
            return
        }
        layer.confirm("是否要删除选定校色",{
            btn:['确定','取消']
        },function (index) {
            layer.close(index)
            var roleIds = "roleIds";

            for (var i = 0; i <data.length ; i++) {
                if(i < data.length-1){
                    roleIds += data[i].id + "&roleIds=";
                }else{
                    roleIds += data[i].id;
                }
            }

            $.ajax({
                type:"post",
                url: ctx+"/role/delete",
                data:roleIds,
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


    //打开授权对话框
    function openAddGrantDialog(listRoleId){
        console.log(listRoleId);
        //选择数量判断
        if(listRoleId.length == 0){
            layer.msg("请选择授权的角色",{icon:5});
        }
        if(listRoleId.length>1){
            layer.msg("暂不支持批量收取",{icon5})
        }

        var url = ctx + "/module/toAddGrantPage?roleId="+listRoleId[0].id;
        var title = "<h3>角色管理-角色授权</h3>";

        layui.layer.open({
            title:title,
            content: url,
            type:2,
            area:["600px","200px"],
            maxmin:true
        })

    }





});
