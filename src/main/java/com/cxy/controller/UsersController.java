package com.cxy.controller;

import java.util.HashMap;
import java.util.Map;

import cn.dreampie.shiro.core.SubjectKit;
import cn.dreampie.shiro.hasher.Hasher;
import cn.dreampie.shiro.hasher.HasherInfo;
import cn.dreampie.shiro.hasher.HasherKit;
//import cn.dreampie.tree.TreeNodeKit;
import com.cxy.model.User;
//import org.apache.shiro.authz.annotation.Logical;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;


public class UsersController extends Controller {

	//@RequiresRoles(value = { "user", "admin" }, logical = Logical.OR)
	@ActionKey("/manage/user/index")
	public void index() {
		render("/manage/user.html");
	}
	@ActionKey("/manage/user/new")
	public void showEdit(){
		render("/manage/userEdt.html");
	}

	//@RequiresPermissions("showUser")
	@ActionKey("/manage/user/list")
	public void list() {
		Page<User> page = User.dao.paginate(getParaToInt("page", 1),
				getParaToInt("size", 10),
				"select id,username,email,created_at ",
				"from sec_user order by created_at desc");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", page.getTotalRow());
		map.put("rows", page.getList());
		renderJson(map);
	}

	//@RequiresPermissions("addUser")
	@ActionKey("/manage/user/add")
	public void add() {
		renderJson(getModel(User.class).save());
	}

	//@RequiresPermissions("editUser")
	@ActionKey("/manage/user/save")
	public void save() {
		renderJson(getModel(User.class).update());
	}

	@ActionKey("/manage/user/edit")
	public void edit(){
		Integer id = getParaToInt("id");
		User user = User.dao.findById(id);
		setAttr("user",user);
		render("/manage/userEdt.html");
	}

	//@RequiresPermissions("deleteUser")
	@ActionKey("/manage/user/delete")
	public void delete() {
		renderJson(getModel(User.class).delete());
	}

	public void updatePwd() {
		keepModel(User.class);
		User upUser = getModel(User.class);

		HasherInfo passwordInfo = HasherKit.hash(upUser.getStr("password"), Hasher.DEFAULT);
		upUser.set("password", passwordInfo.getHashResult());
		upUser.set("hasher", passwordInfo.getHasher().value());
		upUser.set("salt", passwordInfo.getSalt());

		if (upUser.update()) {
			SubjectKit.getSubject().logout();
			setAttr("username", upUser.get("username"));
			setAttr("state", "success");
		} else
			setAttr("state", "failure");
	}
}
