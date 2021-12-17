package com.nyf.mvc.controller;


import com.github.pagehelper.PageInfo;
import com.nyf.constant.CrowdConstant;
import com.nyf.entity.Admin;
import com.nyf.service.api.AdminService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;
    //更新用户信息
    @RequestMapping("admin/page/update/{adminId}/{pageNum}/{keyword}.html")
    public String updateAdmin(
                    @PathVariable("adminId") Integer adminId,
                    @PathVariable("pageNum") Integer pageNum,
                    @PathVariable("keyword") String keyword,
                    ModelMap modelMap
                ){
        //获取需要修改的admin对象
        Admin admin = adminService.getAdminByAdminId(adminId);
        modelMap.addAttribute("admin",admin);
        return "admin-update";
    }
    //更新页面的数据回显
/*    @RequestMapping("")
    public String getAdminById(){
        return null;
    }*/
    //新增用户
    @RequestMapping("admin/page/doSave.html")
    public String doSave(Admin admin){
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }
    //单条记录删除
    @RequestMapping("admin/page/remove/{adminId}/{pageNum}/{keyword}.html")
    public String removeInfo(
                    @PathVariable("adminId") Integer adminId,
                    @PathVariable("pageNum") Integer pageNum,
                    @PathVariable("keyword") String keyword
                ){
        adminService.remove(adminId);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }
    //分页
    @RequestMapping("admin/get/page.html")
    public String getPageInfo(
            //请求中没有参数是默认为空
            @RequestParam(value = "keyword",defaultValue = "")  String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
            ModelMap modelMap
        ){
        //使用service获取pageinfo对象
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword,pageNum,pageSize);
        //
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);

        return "admin-page";
    }
    //登陆验证
    @RequestMapping("/security/do/login.html")
    public String doLogin(
                @RequestParam("login-user") String user,
                @RequestParam("login-pwd") String pwd,
                HttpSession session
            ){
        //根据login-user获取admin对象，在service里做登录判断，登录成功才返回一个admin对象
        Admin admin = adminService.getAdminByUsername(user,pwd);
        //把获取到的对象放进session中
        session.setAttribute(CrowdConstant.LOGIN_ADMIN_NAME,admin);
        return "redirect:/admin/main/page.html";
    }
    //退出登录
    @RequestMapping("security/do/logout.html")
    public String doLogOut(HttpSession session){
//        让sesion失效
        session.invalidate();
        return "redirect:/admin/login/page.html";
    }
}
