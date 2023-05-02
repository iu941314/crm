package com.itpanda.crm.dao;

import com.itpanda.crm.base.BaseMapper;
import com.itpanda.crm.pojo.Module;
import com.itpanda.crm.pojo.modle.TreeModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
    //查询Modole所有类型字段
    List<TreeModel> queryAllModules();
    //查询权限所有信息
    List<Module>queryModuleList();

    //通过层级+url查询资源
    Module queryModuleByGradeAndUrl(@Param("grade") Integer grade, @Param("url") String url);

    //通过moduleName+层级l查询资源
    Module queryModuleByGradeAndModuleName(@Param("grade") Integer grade,@Param("moduleName") String moduleName);
    //通过权限码查询资源
    Module queryModuleByOptVal(String optValue);

    int coutSubModByParentId(Integer mId);

}
