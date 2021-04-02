package com.hkk.cloudtv.entity;

import com.hkk.cloudtv.core.domain.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *     Title: Role.java
 * </p>
 *
 * <p>
 *     Description: 角色管理；
 * </p>
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Role extends IdEntity {

/*    private Long id;// 角色id
    private Date addTime;// 添加时间*/

    private String name;// 角色名称
    private String roleCode;// 角色编码，根据改编码识别角色
    private String type;// 角色类型
    private String info;// 角色说明
    private List<Res> reses;// 权限集合

   /* public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<Res> getReses() {
        return reses;
    }

    public void setReses(List<Res> reses) {
        this.reses = reses;
    }*/
}
