package com.cloud.tv.dto;

import com.cloud.tv.entity.Res;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@ApiModel("权限管理")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto  extends PageDto<Res>{

    @ApiModelProperty("资源ID")
    private Long id;

    @ApiModelProperty("资源名称")
    private String name;

    private String value;

    @ApiModelProperty("角色ID")
    private Long role_id;
}
