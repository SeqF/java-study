package com.study.utils;

import java.sql.SQLException;

/*
* 和事务管理相关的工具类，它包含了，开启事务，提交事务，回滚事务和释放连接
* */
public class TransActionManager {

    private ConnectionUtils connectionUtils;

    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

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
}
