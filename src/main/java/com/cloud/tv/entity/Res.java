package com.cloud.tv.entity;

import com.cloud.tv.core.domain.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *      Title: Res.java
 * </p>
 *
 * <p>
 *     Description: 系统权限资源管理类；用于记录系统权限信息，使用shiro进行对系统资源的访问控制
 * </p>
 *
 *  <author>
 *      HKK
 *  </author>
 */

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("权限资源实体类")
public class Res extends IdEntity {

    @ApiModelProperty("资源名称")
    private String name;

    @ApiModelProperty("资源值")
    private String value;

    @ApiModelProperty("资源类型")
    private String type;

    @ApiModelProperty("角色")
    private Role role;

    /*@ApiModelProperty("角色集合")
    private List<Role> roles = new ArrayList<Role>();*/
}
