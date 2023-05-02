var zTreeObj;
$(function () {
    loadModuleInfo();
});


//树形权限图显示
function loadModuleInfo() {
    $.ajax({
        type: "post",
        url: ctx + "/module/queryAllModules",
        data:{roleId:$("#roleId").val()},
        dataType: "json",
        success: function (data) {
            // zTree 的参数配置，深入使用请参考 API文档（setting 配置详解）
            var setting = {
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                view: {
                    showLine: false
                    // showIcon: false
                },
                check: {
                    enable: true,
                    chkboxType: {
                        "Y": "ps",
                        "N": "ps"
                    }
                },
                // 绑定函数
                callback:{
                    //onCheck当checkbox/radio(单选框)被选中或取消时触发函数
                    onCheck:zTreeOnCheck
                }
            };
            var zNodes = data;

            zTreeObj = $.fn.zTree.init($("#test1"), setting, zNodes);
        }
    })
}

function zTreeOnCheck(event,treeId,treeNode) {
    // alert(treeNode.tId,+".---"+treeNode.name+".---"+treeNode.checked);
    var nodes = zTreeObj.getCheckedNodes(true);
//     // var nodef = zTreeObj.getCheckedNodes(false);
//     console.log("选中的为："+nodes);
//     console.log("未选中的为："+nodef);
// }
    if(nodes.length > 0){
        var mIds ="mIds=";
        for(var i= 0; i<nodes.length;i++){
            if (i < nodes.length-1){
                mIds += nodes[i].id + "&mIds=";
            }else {
                mIds += nodes[i].id;
            }
        }

        console.log(mIds);
    }


    // 获取隐藏域中的角色Id
    var roleId = $("#roleId").val();
    console.log(roleId);
    //发送请求进行授权操作
    $.ajax({
        type:"post",
        url:ctx +"/role/addGrant",
        data:mIds+"&roleId="+roleId,
        dataType: "json",
        success:function (res) {
            console.log(res);
        }
    })
}