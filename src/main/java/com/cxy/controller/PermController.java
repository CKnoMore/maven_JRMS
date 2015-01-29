package com.cxy.controller;

import com.cxy.model.Permission;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

import java.util.HashMap;
import java.util.Map;

//import cn.dreampie.tree.TreeNodeKit;
//import org.apache.shiro.authz.annotation.Logical;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.apache.shiro.authz.annotation.RequiresRoles;


public class PermController extends Controller {

	//@RequiresRoles(value = { "user", "admin" }, logical = Logical.OR)
	@ActionKey("/manage/permission/index")
	public void index() {
		render("/manage/permission.html");
	}

	@ActionKey("/manage/permission/new")
	public void showEdit(){
		render("/manage/permEdt.html");
	}

	//@RequiresPermissions("showUser")
	@ActionKey("/manage/permission/list")
	public void list() {
		Page<Permission> page = Permission.dao.paginate(getParaToInt("page", 1),
				getParaToInt("size", 20),
				"select id,name,value,created_at,url ",
				"from sec_permission order by created_at desc");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", page.getTotalRow());
		map.put("rows", page.getList());
		renderJson(map);
	}

	//@RequiresPermissions("addUser")
	@ActionKey("/manage/permission/add")
	public void add() {
		//renderJson(getModel(Permission.class).save());
		Permission perm = getModel(Permission.class);
		perm.save();
	}

	//@RequiresPermissions("editUser")
	@ActionKey("/manage/permission/save")
	public void save() {
		renderJson(getModel(Permission.class).update());
	}

	@ActionKey("/manage/permission/edit")
	public void edit(){
		setAttr("permission",Permission.dao.findById(getParaToInt("id")));
		render("/manage/userEdt.html");
	}

	//@RequiresPermissions("deleteUser")
	@ActionKey("/manage/permission/delete")
	public void delete() {
		renderJson(getModel(Permission.class).delete());
	}

}
