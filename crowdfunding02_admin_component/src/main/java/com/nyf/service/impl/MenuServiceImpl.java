package com.nyf.service.impl;

import com.nyf.entity.Menu;
import com.nyf.entity.MenuExample;
import com.nyf.mapper.MenuMapper;
import com.nyf.service.api.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    MenuMapper mapper;

    @Override
    public List<Menu> getAll() {
        return mapper.selectByExample(new MenuExample());
    }

    @Override
    public void saveMenu(Menu menu) {
        mapper.insert(menu);
    }
}
