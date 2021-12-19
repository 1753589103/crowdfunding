package com.nyf.mvc.controller;

import com.github.pagehelper.PageInfo;
import com.nyf.entity.Role;
import com.nyf.service.api.RoleService;
import com.nyf.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RoleController {
    @Autowired
    RoleService service;
    //删除
    @ResponseBody
    @RequestMapping("role/do/remove.json")
    public ResultEntity<String> deleteRole(@RequestBody List<Integer> roleIdList){
        service.removeRole(roleIdList);
        return ResultEntity.successWithoutData();
    }
    //更新
    @ResponseBody
    @RequestMapping("role/do/update.json")
    public ResultEntity<String> doUpdate(Role role){
        service.updateRole(role);
        return ResultEntity.successWithoutData();
    }
    //新增
    @ResponseBody
    @RequestMapping("role/do/save.json")
    public ResultEntity<String> doSave(Role role){
        service.saveRole(role);
        return ResultEntity.successWithoutData();
    }
    //分页查询
    @ResponseBody
    @RequestMapping("role/get/page/info.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(
                @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
                @RequestParam(value = "keyword",defaultValue = "") String keyword

            ){

        PageInfo<Role> pageInfo = service.getPageInfo(pageNum,pageSize,keyword);

        return ResultEntity.successWithData(pageInfo);
    }
}
