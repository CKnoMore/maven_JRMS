package com.cxy.controller;

import com.cxy.model.Role;
import com.cxy.model.UserRole;
import com.jfinal.core.Controller;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014/12/30.
 */
public class RoleController extends Controller {
    @ActionKey("/manage/role/index")
    public void index(){
        render("/manage/role.html");
    }

    @ActionKey("/manage/role/list")
    public void listRole(){
        Page<Role> page = Role.dao.paginate(getParaToInt("page", 1),
                getParaToInt("size", 10),
                "select id,name,value,created_at ",
                "from sec_role order by created_at desc");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", page.getTotalRow());
        map.put("rows", page.getList());
        renderJson(map);
    }

    @ActionKey("/manage/role/edit")
    public void edt(){
        setAttr("role",Role.dao.findById(getParaToInt("id")));
        render("/manage/roleEdt.html");
    }

    @ActionKey("/manage/role/disable")
    public void disableRole(){
        boolean flag = false;
        int id = getParaToInt("id");
        UserRole ur = UserRole.dao.findFirstBy("`userrole`.role_id = ?", id );
        if( ur == null){
            Role r = Role.dao.findById(id);
            r.set("deleted_at",new Date());
            r.update();
            flag = true;
        }
        renderJson(flag);
    }

    @ActionKey("/manage/role/editPermission")
    public void edtRolePerm(){

    }

    @ActionKey("/manage/role/editMenu")
    public void edtRoleMenu(){

    }

    @ActionKey("/manage/role/save")
    public void save(){
        renderJson(getModel(Role.class).save());
    }

    @ActionKey("/manage/role/update")
    public void update(){
        renderJson(getModel(Role.class).update());
    }
}
