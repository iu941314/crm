package com.itpanda.crm.services;

import com.itpanda.crm.base.BaseService;
import com.itpanda.crm.dao.ModuleMapper;
import com.itpanda.crm.dao.PermissionMapper;
import com.itpanda.crm.pojo.Module;
import com.itpanda.crm.pojo.modle.TreeModel;
import com.itpanda.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module,Integer> {
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;

//    查询所有的资源列表
    public List<TreeModel> queryAllModules(Integer roleId){
        //查询所有的资源列表项
        List<TreeModel> treeModelList = moduleMapper.queryAllModules();
        //查询角色授权过的资源项id
        List<Integer> permissionIds = permissionMapper.queryRoleHasModuleIdsByRoleId(roleId);

        //判断是否拥有资源mid
        if(permissionIds != null && permissionIds.size() >0){
            treeModelList.forEach(treeModel -> {
                //判断角色id中是有资源
                if(permissionIds.contains(treeModel.getId())){
                    treeModel.setChecked(true);
                }
            });
        }
        return treeModelList;
    }


    //查询资源列表
    public Map<String,Object> queryModuleList(){
        Map<String,Object> result=new HashMap<String,Object>();
        List<Module> modules = moduleMapper.queryModuleList();
        result.put("msg","");
        result.put("code",200);
        result.put("count",modules.size());
        result.put("data",modules);

        return  result;
    }


    //资源module添加
    @Transactional(propagation = Propagation.REQUIRED)
    public void addModule(Module module){
        //参数校验
        //模块名称
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"菜单名称不能为空");
        //模块层级
        AssertUtil.isTrue(!(module.getGrade() ==0 ||module.getGrade() == 1 || module.getGrade() ==2),"模块层级请检查……");
        //模块同级下唯一
        AssertUtil.isTrue(null != moduleMapper.queryModuleByGradeAndModuleName(module.getGrade(),module.getModuleName()),"菜单栏名称已存在");

            //地址url-二级菜单
        if(module.getGrade() ==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"请输入url");
            AssertUtil.isTrue( null != moduleMapper.queryModuleByGradeAndUrl(module.getGrade(),module.getUrl()),"url已存在，请检查");
        }
            //父级菜单
        if(module.getGrade() != 0){
            AssertUtil.isTrue(null == module.getParentId()|| null == selectByPrimaryKey(module.getParentId()),"请指定父菜单");
        }
        //授权码
        AssertUtil.isTrue(null == module.getOptValue(),"请输入授权码");
        Module temp = moduleMapper.queryModuleByOptVal(module.getOptValue());
        System.out.println(temp);
        if(temp != null){

            AssertUtil.isTrue(temp.getOptValue().equals(module.getOptValue()),"权限码已存在");
        }

        //设置默认值
        module.setIsValid(1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        //入库
        AssertUtil.isTrue(insertSelective(module)!=1,"添加菜单失败");

    }

    //更新菜单栏
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module) {
        //参数校验
        //id校验
        AssertUtil.isTrue(null == module.getId() || null == selectByPrimaryKey(module.getId()) ,"待更新记录不存在");
       //菜单名校验
        AssertUtil.isTrue(StringUtils.isBlank( module.getModuleName()),"请输入菜单名" );
        //层级校验-层级是否合法
        AssertUtil.isTrue(null == module.getGrade()|| !(module.getGrade() ==0 ||module.getGrade() ==1 ||module.getGrade() ==2),"菜单层级非法");
        //层级校验-层级是否存在
        Module temp = moduleMapper.queryModuleByGradeAndModuleName(module.getGrade(),module.getModuleName());
        if(temp != null){

            AssertUtil.isTrue(!temp.getId().equals(module.getId()),"该层级菜单已存在");

        }
        //地址url-二级菜单
        if(module.getGrade() ==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"请输入url");
            AssertUtil.isTrue( null != moduleMapper.queryModuleByGradeAndUrl(module.getGrade(),module.getUrl()),"url已存在，请检查");
        }
        //父级菜单
        if(module.getGrade() != 0){

            AssertUtil.isTrue(null == module.getParentId()|| null == selectByPrimaryKey(module.getParentId()),"请指定父菜单");
        }

        //授权码
        AssertUtil.isTrue(null == module.getOptValue(),"请输入授权码");


        AssertUtil.isTrue(null != moduleMapper.queryModuleByOptVal(module.getOptValue() ),"权限码已存在");


        //设置默认值
        module.setIsValid(1);
        module.setUpdateDate(new Date());
        //入库
        AssertUtil.isTrue(updateByPrimaryKeySelective(module) < 1,"添加菜单失败");



    }

    //删除资源module
    public void deleteModuleById(Integer mId){
        //通过主键id查询资源mod
        Module tem = selectByPrimaryKey(mId);
        //校验
        AssertUtil.isTrue((null == tem || null == mId),"待删除记录不存在");

        //是否存在子菜单
        int count = moduleMapper.coutSubModByParentId(mId);
        AssertUtil.isTrue(count>0,"该菜单存在子菜单，不支持删除");
        //查询菜单下的权限
        count = permissionMapper.countPermissionsByModuleId(mId);
        if(count >0){
            //删除资源id对应的角色权限
            AssertUtil.isTrue(permissionMapper.deletePermissonByModuled(mId) != count,"菜单栏删除失败");
        }
        tem.setIsValid(0);

        AssertUtil.isTrue(updateByPrimaryKeySelective(tem) <1,"菜单栏删除失败");
    }



}
