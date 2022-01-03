package com.nyf.mvc.config;

import com.nyf.entity.Admin;
import com.nyf.entity.Auth;
import com.nyf.entity.Role;
import com.nyf.service.api.AdminService;
import com.nyf.service.api.AuthService;
import com.nyf.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class CrowdUserDetailService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //根据账号名查询admin对象
        Admin admin = adminService.getAdminByLoginAcct(username);

        //获取adminId
        Integer adminId = admin.getId();

        //根据adminId查询角色信息
        List<Role> assignedRole = roleService.getAssignedRole(adminId);

        //根据adminId查询权限信息
        List<String> assignedAuthNameList = authService.getAssignedAuthNameListByAdminId(adminId);

        //创建集合对象用来存储GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        //遍历assignedRole存入角色信息
        for (Role role:assignedRole){

            String roleName = "ROLE_"+role.getName();

            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);

            authorities.add(simpleGrantedAuthority);

        }

        //遍历集合，存入权限信息
        for (String auth:assignedAuthNameList){

            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(auth);

            authorities.add(simpleGrantedAuthority);

        }

        //封装SecurityAdmin对象
        SecurityAdmin securityAdmin = new SecurityAdmin(admin,authorities);
        return securityAdmin;
    }
}
