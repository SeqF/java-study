package com.study.service.impl;

import com.study.dao.IAccountDao;
import com.study.dao.impl.AccountDaoImpl;
import com.study.service.IAccountService;

/*
 * 账户的业务层实现类*/
public class AccountServiceImpl implements IAccountService {

    public AccountServiceImpl(){

        System.out.println("对象创建了");
    }

    public void saveAccount() {

        System.out.println("service中的saveAccount方法执行了");
    }

    public void init() {

        System.out.println("对象初始化了");
    }

    public void destory() {

        System.out.println("对象销毁了");
    }

}
