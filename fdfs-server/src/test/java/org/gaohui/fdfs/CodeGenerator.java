package org.gaohui.fdfs;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.Scanner;

//代码生成器
public class CodeGenerator {

    /**
     * 读取控制台内容
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /*public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        //生成路径
        gc.setOutputDir(scanner("请输入你的项目路径") + "/src/main/java");
        //作者
        gc.setAuthor("gaohui");
        //生成之后是否打开资源管理器
        gc.setOpen(false);
        //实体属性 Swagger2 注解
        gc.setSwagger2(true);
        //重新生成时候是否覆盖
        gc.setFileOverride(false);
        //文件命名规则
        gc.setServiceName("%sService");
        //设置主键生成策略
        gc.setIdType(IdType.ASSIGN_UUID);
        //设置Date类型
        gc.setDateType(DateType.ONLY_DATE);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://106.14.83.220:3306/fdfs?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("scistorGH2021");
        //数据库类型
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        //pc.setModuleName(scanner("请输入模块名"));
        pc.setParent("org.gaohui.fdfs");
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setMapper("mapper");
        pc.setEntity("entity");
        pc.setXml("mapper");
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //设置哪些表需要自动生成
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        //表前缀
        strategy.setTablePrefix("tb_");
        //strategy.setLogicDeleteFieldName("is_del");
        //驼峰命名策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //字段名
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //Lombok
        strategy.setEntityLombokModel(true);
        //@RestController
        strategy.setRestControllerStyle(true);
        //驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        //设置逻辑删除字段
        strategy.setLogicDeleteFieldName("flag");
        *//*strategy.setSuperEntityColumns("id","createby","createtime","updatetime","version","delete");
        //设置公共字段父类
        strategy.setSuperEntityClass(BaseEntity.class,NamingStrategy.no_change);
        InjectionConfig in = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                //自定义配置，在模版中cfg.superColums 获取
                // TODO 这里解决子类会生成父类属性的问题，在模版里会用到该配置
                map.put("superColums", this.getConfig().getStrategyConfig().getSuperEntityColumns());
                this.setMap(map);
            }
        };
        mpg.setCfg(in);*//*
        TemplateConfig templateConfig = new TemplateConfig();
        //关闭默认的mapper xml生成
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
        mpg.setStrategy(strategy);
        mpg.execute();
    }*/

}