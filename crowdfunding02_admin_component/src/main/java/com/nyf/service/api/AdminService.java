package com.nyf.service.api;

import com.github.pagehelper.PageInfo;
import com.nyf.entity.Admin;


import java.util.List;

public interface AdminService {
    Admin getAdminByUsername(String adminLoginAcct, String adminPassword);

    PageInfo<Admin> getPageInfo(String keyword,Integer pageNum,Integer pageSize);

    void remove(Integer adminId);

    void saveAdmin(Admin admin);

    void updateAdmin(Admin admin);

    Admin getAdminByAdminId(Integer adminId);

    void saveAdminRelationship(Integer adminId, List<Integer> roleIdList);

    Admin getAdminByLoginAcct(String loginAcct);
}
