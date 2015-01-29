package com.cxy.model;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;
import com.jfinal.plugin.activerecord.Db;

import java.util.List;

/**
 * Created by wangrenhui on 14-4-22.
 */
@TableBind(tableName = "sec_user_menu")
public class UserMenu extends Model<UserMenu> {
  public static UserMenu dao = new UserMenu();

  public List<String> findMenuIds(String where, Object... paras) {
    return Db.query("SELECT DISTINCT `userMenu`.menu_id " + getFromSql() + getWhere(where), paras);
  }
}
