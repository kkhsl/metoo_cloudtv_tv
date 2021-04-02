package com.hkk.cloudtv.core.service.impl;

import com.hkk.cloudtv.core.mapper.BuyerMapper;
import com.hkk.cloudtv.core.mapper.RegisterMapper;
import com.hkk.cloudtv.core.service.IRegisterService;
import com.hkk.cloudtv.core.shiro.tools.SaltUtils;
import com.hkk.cloudtv.entity.User;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p>
 *     Title: RegisterServiceImpl.java
 *</p>
 *
 * <p>
 *     Description: 用户注册服务类；注意：唯一值验证
 * </p>
 */

@Service("registerService")
@Transactional
public class RegisterServiceImpl implements IRegisterService {

    @Autowired
    private RegisterMapper registerMapper;
    @Autowired
    private BuyerMapper buyerMapper;

    @Override
    public int register(User user) {
        // 处理业务逻辑;
        // 1，获取随机盐
        String sale = SaltUtils.getSalt(8);
        // 保存当前用户salt
        user.setSalt(sale);
        // 明文密码进行 md5 + salt + hash散列
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), sale, 1024);
        user.setPassword(md5Hash.toHex());
        this.registerMapper.save(user);
        return 1;
    }

    @Override
    public User findByUsername(String username) {
        return this.buyerMapper.findByUserName(username);
    }

    @Override
    public User findRolesByUserName(String username) {
        return this.buyerMapper.findRolesByUserName(username);
    }
}
