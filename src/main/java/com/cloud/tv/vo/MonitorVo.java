package com.cloud.tv.vo;

import com.cloud.tv.core.domain.IdEntity;
import com.cloud.tv.entity.LiveRoom;
import com.cloud.tv.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@ApiModel("学校实体VO类")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MonitorVo extends IdEntity {

    @ApiModelProperty("学校编号：唯一标识、由学校第一次调用接口生成，之后不在改变")
    private String appId;
    @ApiModelProperty("学校标题")
    private String title;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    @ApiModelProperty("直播开启时间")
    private Date beginTime;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    @ApiModelProperty("直播结束时间")
    private Date endTime;
    private Long userId;
    private Long liveRoomId;
    private String username;
    private String liveRoomTitle;
    @ApiModelProperty("直播名称")
    private String roomProgramTitle;
    @ApiModelProperty("直播状态 0: 关闭 1：开启 ")
    private int status;
    @ApiModelProperty("直播 次数")
    private Integer number;
    @ApiModelProperty("播放时长")
    private Long duration;
    @ApiModelProperty("播放总时长")
    private Long durationTotal;

}
