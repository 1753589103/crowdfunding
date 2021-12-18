package com.nyf.service.api;

import com.github.pagehelper.PageInfo;
import com.nyf.entity.Role;

public interface RoleService {
    PageInfo<Role> getPageInfo(Integer pageNum , Integer pageSize,String keyword);

    void saveRole(Role role);
}
