package com.cxy.config;


import cn.dreampie.log.Slf4jLogFactory;
//import cn.dreampie.mail.MailerPlugin;
//import cn.dreampie.quartz.QuartzPlugin;
import cn.dreampie.routebind.RouteBind;
import cn.dreampie.shiro.core.ShiroInterceptor;
import cn.dreampie.shiro.core.ShiroPlugin;
import cn.dreampie.sqlinxml.SqlInXmlPlugin;
import cn.dreampie.tablebind.SimpleNameStyles;
import cn.dreampie.tablebind.TableBindPlugin;
import cn.dreampie.web.Config;
import cn.dreampie.web.handler.FakeStaticHandler;
import cn.dreampie.web.handler.ResourceHandler;
import cn.dreampie.web.handler.SkipHandler;
import cn.dreampie.web.handler.xss.AttackHandler;
import cn.dreampie.web.render.JsonErrorRenderFactory;
//import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.cxy.ext.shiro.MyJdbcAuthzService;
import com.cxy.ext.shiro.ShiroBeetlTag;
import com.jfinal.config.*;
//import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.core.JFinal;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
//import com.jfinal.plugin.activerecord.dialect.AnsiSqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import org.beetl.core.GroupTemplate;
import org.beetl.ext.jfinal.BeetlRenderFactory;

/**
 * Created with IntelliJ IDEA.
 * User: wangrenhui
 * Date: 13-12-18
 * Time: 下午6:28
 * API引导式配置
 */
public class AppConfig extends Config {
  /**
   * 供Shiro插件使用。
   */
  Routes routes;

  /**
   * 配置常量
   */
  public void configConstant(Constants constants) {
    loadPropertyFile("application.properties");
    constants.setDevMode(getPropertyToBoolean("devMode", false));
  //for beetl
    constants.setMainRenderFactory(new BeetlRenderFactory());
    GroupTemplate gt = BeetlRenderFactory.groupTemplate;
    gt.registerFunctionPackage("so",new ShiroBeetlTag());

    //set log to slf4j
    Logger.setLoggerFactory(new Slf4jLogFactory());
    constants.setErrorRenderFactory(new JsonErrorRenderFactory());
  }

  /**
   * 配置路由
   */
  public void configRoute(Routes routes) {
    this.routes = routes;
    RouteBind routeBind = new RouteBind();
    routes.add(routeBind);
  }

  /**
   * 配置插件
   */
  public void configPlugin(Plugins plugins) {

    //数据库版本控制插件
//    plugins.add(new FlywayPlugin());
    //配置druid连接池
    String jdbcUrl = getProperty("jdbcUrl");
    String driver = getProperty("driverClass");
    String username = getProperty("username");
    String password = getProperty("password");
    DruidPlugin druidPlugin = new DruidPlugin(jdbcUrl, username, password,driver);

    // WallFilter的功能是防御SQL注入攻击
    WallFilter wallFilter = new WallFilter();
    wallFilter.setDbType("mysql");
    druidPlugin.addFilter(wallFilter);
    plugins.add(druidPlugin);

    //Model自动绑定表插件
    TableBindPlugin tableBindDefault = new TableBindPlugin(druidPlugin, SimpleNameStyles.LOWER);
    tableBindDefault.setContainerFactory(new CaseInsensitiveContainerFactory(true)); //忽略字段大小写
//    tableBindDefault.addExcludePaths("cn.dreampie.function.shop");
//    tableBindDefault.addIncludePaths("cn.dreampie.function.default");
    tableBindDefault.setShowSql(getPropertyToBoolean("devMode", false));
    tableBindDefault.setShowSql(getPropertyToBoolean("showSql", false));
    //非mysql的数据库方言
    //tableBindDefault.setDialect(new AnsiSqlDialect());
    plugins.add(tableBindDefault);

    //sql语句plugin
    plugins.add(new SqlInXmlPlugin());
    //ehcache缓存
    plugins.add(new EhCachePlugin());
    //shiro权限框架
    //plugins.add(new ShiroPlugin(routes, new MyJdbcAuthzService()));
    //akka异步执行插件
//    plugins.add(new AkkaPlugin());
    //emailer插件
//    plugins.add(new MailerPlugin());
    //lesscsss编译插件
//    plugins.add(new LessCssPlugin("/lesscss/", "/style/"));
    //coffeescript编译插件
//    plugins.add(new CoffeeScriptPlugin("/coffeescript/", "/javascript/"));
    //quartz 任务
//    plugins.add(new QuartzPlugin());
  }

  /**
   * 配置全局拦截器
   */
  public void configInterceptor(Interceptors interceptors) {
//    interceptors.add(new ShiroInterceptor());
    //开发时不用开启  避免不能实时看到数据效果
//    interceptors.add(new CacheRemoveInterceptor());
//    interceptors.add(new CacheInterceptor());
    //interceptors.add(new SessionInViewInterceptor());
  }

  /**
   * 配置处理器
   */
  public void configHandler(Handlers handlers) {
//    handlers.add(new FakeStaticHandler());
//    handlers.add(new AccessDeniedHandler("/**/*.ftl"));
//    handlers.add(new ResourceHandler("/javascript/**", "/images/**", "/css/**", "/lib/**", "/**/*.html"));
//    handlers.add(new SkipHandler("/im/**"));
//    //防xss攻击
//    handlers.add(new AttackHandler());
  }

  @Override
  public void afterJFinalStart() {
    super.afterJFinalStart();
  }

  /**
   * 建议使用 JFinal 手册推荐的方式启动项目
   * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
   */
//    public static void main(String[] args) {
//        JFinal.start("src/main/webapp", 80, "/", 5);
//    }
}
