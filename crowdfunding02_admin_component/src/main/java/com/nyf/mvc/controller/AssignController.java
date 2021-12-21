package com.nyf.mvc.controller;

import com.nyf.entity.Auth;
import com.nyf.entity.Role;
import com.nyf.service.api.AdminService;
import com.nyf.service.api.AuthService;
import com.nyf.service.api.RoleService;
import com.nyf.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class AssignController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthService authService;

    //保存更改后的Role与Auth关系
    @ResponseBody
    @RequestMapping("/assign/do/save/role/auth/relationship.json")
    public ResultEntity<String> saveRoleAuthRelationship(
            @RequestBody Map<String,List<Integer>> map
        ){
        authService.saveRoleAuthRelationship(map);

        return ResultEntity.successWithoutData();
    }
    //根据勾选的roleId查询auth
    @ResponseBody
    @RequestMapping("assign/get/checked/auth/id.json")
    public ResultEntity<List<Integer>> getAssignedAuthIdByRoleId(
                @RequestParam("roleId") Integer roleId
            ){
        List<Integer> authList = authService.getAuthIdList(roleId);
        return ResultEntity.successWithData(authList);
    }
    //获取树形结构数据
    @ResponseBody
    @RequestMapping("assign/get/tree.json")
    public ResultEntity<List<Auth>> getTree(){
        List<Auth> list = authService.getAll();
        return ResultEntity.successWithData(list);
    }
    //保存已分配的角色
    @RequestMapping("assign/do/assign.html")
    public String saveRelationship(
                 @RequestParam("adminId") Integer adminId,
                 @RequestParam("pageNum") Integer pageNum,
                 @RequestParam("keyword") String keyword,
                 //允许用户在页面上取消所有的分配角色提交，所以可以不提供roleIdList
                 @RequestParam(value = "roleIdList",required = false) List<Integer> roleIdList
            ){
        adminService.saveAdminRelationship(adminId,roleIdList);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }
    //获取分配和未分配角色
    @RequestMapping("assign/to/page.html")
    public String toAssignRolePage(
            @RequestParam("adminId") Integer adminId,
                ModelMap modelMap
            ){
        //查询已分配的角色
        List<Role> assignedRole = roleService.getAssignedRole(adminId);
        //查询未分配的角色
        List<Role> unAssignedRole = roleService.getUnAssignedRole(adminId);
        //存入模型
        modelMap.addAttribute("assignedRole",assignedRole);
        modelMap.addAttribute("unAssignedRole",unAssignedRole);

        return "assign-role";
    }
}
