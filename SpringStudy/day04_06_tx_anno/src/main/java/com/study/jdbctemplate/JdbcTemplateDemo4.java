package com.study.jdbctemplate;

import com.study.dao.IAccountDao;
import com.study.domain.Account;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/*
 * JdbcTemplate的最基本用法*/
public class JdbcTemplateDemo4 {

    public static void main(String[] args) {
        //1.获取容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        //2.获取对象
        IAccountDao accountDao=ac.getBean("accountDao",IAccountDao.class);

        Account account=accountDao.findAccountById(2);
        System.out.println(account);


//        //准备数据：spring的内置数据源
//        DriverManagerDataSource ds= new DriverManagerDataSource();
//        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        ds.setUrl("jdbc:mysql://localhost:3306/study?serverTimezone=GMT&useUnicode=true&characterEncoding=utf8");
//        ds.setUsername("root");
//        ds.setPassword("root");
//
//        //1.创建JdbcTemplate对象
//        JdbcTemplate jt=new JdbcTemplate();
//        //给jt设置数据源
//        jt.setDataSource(ds);
//        //2.执行操作
//        jt.execute("insert into account(name,money)values ('ddd',1000)");

    }
}
