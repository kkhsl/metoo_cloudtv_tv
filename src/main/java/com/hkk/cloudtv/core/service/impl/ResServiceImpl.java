package com.hkk.cloudtv.core.service.impl;

import com.hkk.cloudtv.core.mapper.ResMapper;
import com.hkk.cloudtv.core.service.IResService;
import com.hkk.cloudtv.entity.Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("resService")
@Transactional
public class ResServiceImpl implements IResService {

    @Autowired
    private ResMapper resMapper;

    @Override
    public List<Res> findResByRoleId(Long id) {
        return this.resMapper.findResByRoleId(id);
    }
}
