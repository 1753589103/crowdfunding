package com.nyf.service.impl;

import com.nyf.entity.Auth;
import com.nyf.entity.AuthExample;
import com.nyf.mapper.AuthMapper;
import com.nyf.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    AuthMapper authMapper;

    @Override
    public List<Auth> getAll() {
        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public List<Integer> getAuthIdList(Integer roleId) {
        return authMapper.selectAuthIdList(roleId);
    }

    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {
        // 从map获取到roleId、authIdList
        List<Integer> roleIdList = map.get("roleId");
        Integer roleId = roleIdList.get(0);

        List<Integer> authIdList = map.get("authIdList");
        //删除原有关系
        authMapper.deleteOldRelationshipByRoleId(roleId);

        // 2 当authIdList有效时，添加前端获取的新的关系信息
        if (authIdList != null && authIdList.size() > 0){
            authMapper.insertNewRelationship(roleId,authIdList);
        }
    }
}
