package com.hkk.cloudtv.entity;

import com.hkk.cloudtv.core.domain.IdEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Titlte: User.java
 * </p>
 *
 * <p>
 * Description: 用户类，所有用户都由该类管理，包括普通用户，商家，管理员
 * </p>
 *
 * <p>
 * author: hkk
 * </p>
 */

@Data//  注解在类上, 为类提供读写属性, 此外还提供了 equals()、hashCode()、toString() 方法
@Accessors(chain = true) // fluent、chain、prefix、注解用来配置lombok如何产生和显示getters和setters的方法
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户实体类")
public class User extends IdEntity {

    @ApiModelProperty("用户id")
    private Long id;// 用户id
    @ApiModelProperty("用户注册时间")
    private Date addTime;// 添加时间
    @ApiModelProperty("用户名")
    private String username;// 用户名
    @ApiModelProperty("用户密码")
    private String password;// 用户密码
    private int sex;// 性别 -1:无 0:女  1：男
    private int age;//年龄
    private String salt;// 加密盐
    private String userRole;// 用户角色
    private List<Role> roles = new ArrayList<Role>();// 角色集合
    private Long deptId;

  /*  public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public String getSalt() {
        return salt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }*/
}
