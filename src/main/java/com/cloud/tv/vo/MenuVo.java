package com.cloud.tv.vo;

import com.cloud.tv.req.RoleReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@ApiModel("菜单管理")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MenuVo {

    @ApiModelProperty("角色组ID")
    private Long roleGroupId;

    @ApiModelProperty("角色组名称")
    private String roleGroupName;

    @ApiModelProperty("ICON")
    private String roleGroupIcon;

    @ApiModelProperty("角色组路由：前端")
    private String roleGroupUrl;

    @ApiModelProperty("角色信息")
    private List<RoleReq> role;

}
