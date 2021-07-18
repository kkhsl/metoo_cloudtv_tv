package com.cloud.tv.core.service.impl;

import com.cloud.tv.core.mapper.ResMapper;
import com.cloud.tv.dto.PermissionDto;
import com.cloud.tv.entity.Res;
import com.cloud.tv.entity.Role;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.cloud.tv.core.service.IResService;
import com.cloud.tv.core.service.IRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("resService")
@Transactional
public class ResServiceImpl implements IResService {

    @Autowired
    private ResMapper resMapper;
    @Autowired
    private IRoleService roleService;

    @Override
    public List<Res> findResByRoleId(Long id) {
        return this.resMapper.findResByRoleId(id);
    }

    @Override
    public Res findObjById(Long id) {
        return this.resMapper.selectPrimaryById(id);
    }

    @Override
    public Res findResUnitRoleByResId(Long id) {
        return this.resMapper.findResUnitRoleByResId(id);
    }

    @Override
    public Page<Res> query(Map<String, Integer> params) {
             Page<Res> page= PageHelper.startPage(params.get("currentPage"), params.get("pageSize"));
            this.resMapper.findResByAll();
            return page;
    }

    @Override
    public List<Res> findResByResIds(List<Integer> ids) {
        return this.resMapper.findResByResIds(ids);
    }

    @Override
    public boolean save(PermissionDto instance) {
        Res obj = null;
        if(instance.getId() == null){
            obj = new Res();
            obj.setAddTime(new Date());
        }else{
            obj = this.resMapper.selectPrimaryById(instance.getId());
        }
        Role role =  this.roleService.findRoleById(instance.getRole_id());
        obj.setRole(role);
        if(obj != null){
            BeanUtils.copyProperties(instance, obj);
            obj.setType("URL");
            if(obj.getId() == null ){
                try {
                    this.resMapper.save(obj);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }else{
                try {
                    this.resMapper.update(obj);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }


    @Override
    public boolean delete(Long id) {
        try {
            this.resMapper.delete(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
