package com.nyf.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyf.constant.CrowdConstant;
import com.nyf.entity.Admin;
import com.nyf.entity.AdminExample;
import com.nyf.exception.DeleteFailedException;
import com.nyf.exception.LoginAcctAlreadyInUseException;
import com.nyf.mapper.AdminMapper;
import com.nyf.service.api.AdminService;
import com.nyf.util.CrowdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper mapper;
    @Override
    public Admin getAdminByUsername(String adminLoginAcct, String adminPassword) {
        //1、根据登录账户查找admin对象
        AdminExample example = new AdminExample();
        AdminExample.Criteria criteria = example.createCriteria();
        criteria.andLoginAcctEqualTo(adminLoginAcct);
        List<Admin> adminList= mapper.selectByExample(example);
        //2、判断admin是否为空
        if (adminList==null || adminList.size() == 0){
            //3、admin为空抛出异常
            throw new RuntimeException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        if (adminList.size()>1){
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }

        //4、admin不为空，则取出数据库密码
        Admin admin = adminList.get(0);
        String pasWordDB = admin.getUserPswd();
        //5、把表单提交的密码进行加密
        //6、比较密码
        if (Objects.equals(CrowdUtil.md5(adminPassword),pasWordDB)) {
            return admin;
        }
        //7、不一致抛出异常
        throw new RuntimeException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        //8、一致返回admin对象
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        //开启分页功能
        PageHelper.startPage(pageNum,pageSize);

        //执行查询
        List<Admin> adminList = mapper.selectAdminByKeword(keyword);

        return new PageInfo<>(adminList);
    }

    @Override
    public void remove(Integer adminId) {
        Integer n = mapper.deleteByPrimaryKey(adminId);
        if (n==0){
            throw new DeleteFailedException(CrowdConstant.DELETE_FAILED);
        }
    }

    @Override
    public void saveAdmin(Admin admin) {
        //密码加密
        String userPswd = CrowdUtil.md5(admin.getUserPswd());
        //把加密好的密码放回admin中
        admin.setUserPswd(userPswd);
        //获取当前时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        String createTime = format.format(date);

        admin.setCreateTime(createTime);
        try {
            mapper.insert(admin);
        }catch (DuplicateKeyException e){
            throw new LoginAcctAlreadyInUseException(CrowdConstant.ATTR_NAME_ALREADY_USE);
        }
    }

    @Override
    public void updateAdmin(Admin admin) {
        mapper.updateByPrimaryKeySelective(admin);
    }

    @Override
    public Admin getAdminByAdminId(Integer adminId) {
        return mapper.selectByPrimaryKey(adminId);
    }
}
