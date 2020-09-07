package com.study.service.impl;

import com.study.dao.IAccountDao;
import com.study.domain.Account;
import com.study.service.IAccountService;
import com.study.utils.TransActionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * 账户的业务层实现类
 *
 * 事务控制应该都是在业务层*/
@Service("accountService")
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private IAccountDao accountDao;

    public List<Account> findAllAccount() {
        return accountDao.findAllAccount();
    }

    public Account findAccountById(Integer accountId) {
        return accountDao.findAccountById(accountId);
    }

    public void saveAccount(Account account) {
        accountDao.saveAccount(account);
    }

    public void updateAccount(Account account) {
        accountDao.updateAccount(account);
    }

    public void deleteAccount(Integer accountid) {
        accountDao.deleteAccount(accountid);
    }

    public void transfer(String sourceName, String targetName, Float money) {
        System.out.println("已启动");
        //1.根据名称查询转出账户
        Account source = accountDao.findAccountByName(sourceName);
        //2.根据名称查询转入账户
        Account target = accountDao.findAccountByName(targetName);
        //3.转出账户减钱
        source.setMoney(source.getMoney() - money);
        //4.转入账户加钱
        target.setMoney(target.getMoney() + money);
//        int i=1/0;
        //5.更新转出账户
        accountDao.updateAccount(source);
        //6.更新转入账户
        accountDao.updateAccount(target);
    }
}

