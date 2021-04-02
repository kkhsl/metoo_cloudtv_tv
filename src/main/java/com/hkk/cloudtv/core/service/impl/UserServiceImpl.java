package com.hkk.cloudtv.core.service.impl;

import com.hkk.cloudtv.core.mapper.BuyerMapper;
import com.hkk.cloudtv.core.service.IUserService;
import com.hkk.cloudtv.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private BuyerMapper buyerMapper;

    @Override
    public User findByUserName(String username) {
        return this.buyerMapper.findByUserName(username);
    }

    @Override
    public User findRolesByUserName(String username) {
        return null;
    }
}
