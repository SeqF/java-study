package com.study.service.impl;

import com.study.dao.IAccountDao;
import com.study.domain.Account;
import com.study.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
* 账户的业务层实现类*/
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
}
