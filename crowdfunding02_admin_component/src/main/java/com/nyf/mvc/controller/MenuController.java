package com.nyf.mvc.controller;

import com.nyf.entity.Menu;
import com.nyf.service.api.MenuService;
import com.nyf.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MenuController {
    @Autowired
    MenuService service;
    //删除节点
    @RequestMapping("menu/remove.json")
    public ResultEntity<String> removeMenu(@RequestParam("id") Integer id){
        service.removeMenu(id);
        return ResultEntity.successWithoutData();
    }
    //修改节点
    @RequestMapping("menu/edit.json")
    public ResultEntity<String> editMenu(Menu menu){
        service.editMenu(menu);
        return ResultEntity.successWithoutData();
    }
    //新增节点
    @RequestMapping("menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu){
        service.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }
    //获取树形结构
    @RequestMapping("/menu/do/get.json")
    public ResultEntity<Menu> getWholeTree(){
        // 通过service层方法得到全部Menu对象的List
        List<Menu> menuList = service.getAll();

        // 声明一个Menu对象root，用来存放找到的根节点
        Menu root = null;

        // 使用map表示每一个菜单与id的对应关系
        Map<Integer,Menu> menuMap = new HashMap<>();

        // 将菜单id与菜单对象以K-V对模式存入map
        for(Menu menu: menuList){
            menuMap.put(menu.getId(),menu);
        }

        for (Menu menu : menuList){
            Integer pid = menu.getPid();

            if (pid == null){
                // pid为null表示该menu是根节点
                root = menu;
                continue;
            }
            // 通过当前遍历的menu的pid得到其父节点
            Menu father = menuMap.get(pid);
            // 为父节点添加子节点为当前节点
            father.getChildren().add(menu);
        }

        // 将根节点作为data返回（返回根节点也就返回了整棵树）
        return ResultEntity.successWithData(root);
    }
}
