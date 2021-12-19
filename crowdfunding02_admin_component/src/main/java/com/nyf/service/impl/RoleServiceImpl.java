package com.nyf.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyf.entity.Role;
import com.nyf.entity.RoleExample;
import com.nyf.mapper.RoleMapper;
import com.nyf.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleMapper mapper;
    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum,pageSize);

        //执行查询
        List<Role> list = mapper.selectByKeyword(keyword);

        return new PageInfo<>(list);
    }
    //新增保存
    @Override
    public void saveRole(Role role) {
        mapper.insertSelective(role);
    }
    //更新
    @Override
    public void updateRole(Role role) {
        mapper.updateByPrimaryKey(role);
    }
    //删除
    @Override
    public void removeRole(List<Integer> arryList) {
        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();
        criteria.andIdIn(arryList);
        mapper.deleteByExample(roleExample);
    }
}
