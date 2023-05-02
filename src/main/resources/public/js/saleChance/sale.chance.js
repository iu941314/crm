layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    //用户列表展示
    var  tableIns = table.render({
        id : "cnm",
        elem: '#saleChanceList',
        url : ctx+'/sale_chance/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"center"},
            {field: 'chanceSource', title: '机会来源',align:"center"},
            {field: 'customerName', title: '客户名称',  align:'center'},
            {field: 'cgjl', title: '成功几率', align:'center'},
            {field: 'overview', title: '概要', align:'center'},
            {field: 'linkMan', title: '联系人',  align:'center'},
            {field: 'linkPhone', title: '联系电话', align:'center'},
            {field: 'description', title: '描述', align:'center'},
            {field: 'createMan', title: '创建人', align:'center'},
            {field: 'assignMan', title: '分配人', align:'center'},
            {field: 'assignTime', title: '分配时间', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'updateDate', title: '修改时间', align:'center'},
            {field: 'state', title: '分配状态', align:'center',templet:function(d){
                    return formatterState(d.state);
                }},
            {field: 'devResult', title: '开发状态', align:'center',templet:function (d) {
                    return formatterDevResult(d.devResult);
                }},
            {title: '操作', templet:'#saleChanceListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    });

    function formatterState(state){
        if(state==0){
            return "<div style='color:yellow '>未分配</div>";
        }else if(state==1){
            return "<div style='color: green'>已分配</div>";
        }else{
            return "<div style='color: red'>未知</div>";
        }
    }

    function formatterDevResult(value){
        /**
         * 0-未开发
         * 1-开发中
         * 2-开发成功
         * 3-开发失败
         */
        if(value==0){
            return "<div style='color: yellow'>未开发</div>";
        }else if(value==1){
            return "<div style='color: #00FF00;'>开发中</div>";
        }else if(value==2){
            return "<div style='color: #00B83F'>开发成功</div>";
        }else if(value==3){
            return "<div style='color: red'>开发失败</div>";
        }else {
            return "<div style='color: #af0000'>未知</div>"
        }
    }

    // 多条件搜索
    $(".search_btn").on("click",function(){
        table.reload("saleChanceListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                customerName: $("input[name='customerName']").val(),  //客户名
                createMan: $("input[name='createMan']").val(),  //创建人
                state: $("#state").val()  //状态
            }
        })
    });

    //头工具栏事件
    table.on('toolbar(saleChances)', function(reqData){
        // var checkStatus = table.checkStatus(obj.config.id);
        //
        // console.log(reqData);
        // var cs = table.checkStatus(reqData.config.id);
        // console.log("ids："+cs.data);

        if(reqData.event == "add"){
            openAddOrUpdateSaleChanceDialog();
        }else if(reqData.event == "del"){
            var checkStatus = table.checkStatus('cnm');
            var datas = checkStatus.data;
            // saleChanceTable
            console.log("批量删除执行")
            delSaleChance(datas);
        }
    });


    /**
     * 行监听
     */
    table.on('tool(saleChances)',function (data) {
        if(data.event == "edit"){
            console.log("编辑……")
            console.log(data.data.id)
            openAddOrUpdateSaleChanceDialog(data.data.id)
        }else if(data.event == 'del'){
            console.log("删除……")
            //弹出关闭确认框
            layer.confirm("是否要删除该记录？",{icon:3,title:"营销机会-删除管理"},function (index){
                layer.close(index);

                //发送请求后端删除
                $.ajax({
                    type:"post",
                    url:ctx + "/sale_chance/delete",
                    data:{
                        ids:data.data.id
                    },
                    success:function (res){
                        if(res.code == 200){
                            layer.msg("删除成功",{icon:6})
                            //刷新表格
                            tableIns.reload();
                        }else {
                            layer.msg(res.msg,{icon:5})
                        }
                    }
                })
            })

        }
    })


    function openAddOrUpdateSaleChanceDialog(saleChanceId) {
        var title="营销机会管理-机会添加";
        var url=ctx+"/sale_chance/addOrUpdateSaleChancePage";

        if(saleChanceId != null && saleChanceId != ""){
            title="营销机会管理-机会编辑";
            url=url+"?saleChanceId="+saleChanceId;
        }

        layui.layer.open({
            title:title,
            type:2,
            area:["700px","500px"],
            maxmin:true,
            content:url
        })
    }


    /**
     * 批量删除
     * @param datas
     */
    function delSaleChance(datas) {

        console.log(datas)
        if(datas.length==0){
            layer.msg("请选择待删除记录!");
            return;
        }

        layer.confirm("确定删除选中的记录",{
            btn:['确定','取消']
        },function (index) {
            layer.close(index);
            // ids=10&ids=20&ids=30
            var ids="ids=";
            for(var i=0;i<datas.length;i++){
                if(i<datas.length-1){
                    ids=ids+datas[i].id+"&ids=";
                }else{
                    ids=ids+datas[i].id;
                }
            }

            $.ajax({
                type:"post",
                url:ctx+"/sale_chance/delete",
                data:ids,
                dataType:"json",
                success:function (data) {
                    if(data.code==200){
                        tableIns.reload();
                    }else{
                        layer.msg(data.msg);
                    }
                }
            })



        })

    }





});
