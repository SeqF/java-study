package com.study.dao;

import com.study.domain.Account;

/*
* 账户的持久层接口
* */
public interface IAccountDao {
    /*
    * 根据ID查询账户*/
    Account findAccountById(Integer accountId);

    /*
    * 根据名称查询账户
    * */
    Account findAccountByName(String accountName);

    /*
    * 更新账户*/
    void updateAccount(Account account);


}
