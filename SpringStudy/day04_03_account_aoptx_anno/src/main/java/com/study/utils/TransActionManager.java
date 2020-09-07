package com.study.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/*
* 和事务管理相关的工具类，它包含了，开启事务，提交事务，回滚事务和释放连接
* */
@Aspect
@Component("txManager")
public class TransActionManager {

    @Autowired
    private ConnectionUtils connectionUtils;

    @Pointcut("execution(* com.study.service.impl.*.*(..))")
    private void pt1(){}

    /*
    * 开启事务*/
    public void begin(){
        try {
            connectionUtils.getThreadConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * 提交事务*/
    public void commit(){
        try {
            connectionUtils.getThreadConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * 回滚事务*/
    public void rollback(){
        try {
            connectionUtils.getThreadConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * 释放连接*/
    public void release(){
        try {
            connectionUtils.getThreadConnection().close();//换回连接池中
            connectionUtils.removeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Around("pt1()")
    public Object aroundAdvice(ProceedingJoinPoint pjp){
        Object rtValue=null;
        try{
            //1.获取参数
            Object[] args=pjp.getArgs();
            //2.开启事务
            this.begin();
            //3.执行方法
            rtValue=pjp.proceed(args);
            //4.提交事务
            this.commit();

            //返回结果
            return rtValue;
        }catch (Throwable e){
            //5.回滚事务
            this.rollback();
            throw new RuntimeException(e);
        }finally {
            //6.释放资源
            this.release();
        }

    }
}
