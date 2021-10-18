package com.vainycos.mybatisgenerator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 * @ClassName CodeGenerator
 * @Description
 * @Version 1.0
 **/
public class CodeGenerator {

    public static void main(String[] args) {
        Properties jdbcProperties = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jdbc.properties");
        try {
            jdbcProperties.load(is);
        } catch (IOException e) {
            throw new MybatisPlusException("load jdbc.properties error.");
        }

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        System.out.println(projectPath);
        gc.setOutputDir(projectPath + "/src/main/java");
        // 设置作者
        gc.setAuthor("Vainycos");
        gc.setOpen(false);
        gc.setEntityName("%sDO");
        gc.setFileOverride(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(jdbcProperties.getProperty("mysql.url"));
        dsc.setDriverName(jdbcProperties.getProperty("mysql.driver.name"));
        dsc.setUsername(jdbcProperties.getProperty("mysql.username"));
        dsc.setPassword(jdbcProperties.getProperty("mysql.password"));
        if (!Boolean.valueOf(jdbcProperties.getProperty("mysql.tinyint.boolean"))) {
            dsc.setTypeConvert(new MySqlTypeConvert() {
                @Override
                public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
                    String t = fieldType.toLowerCase();
                    if (t.contains("tinyint(1)")) {
                        return DbColumnType.INTEGER;
                    }
                    return super.processTypeConvert(config, fieldType);
                }
            });
        }
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.vainycos.mybatisgenerator");
        // 实体包名称
        pc.setEntity("entity");
        // mapper包名称
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);
        templateConfig.setController(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (ipt != null) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

}
