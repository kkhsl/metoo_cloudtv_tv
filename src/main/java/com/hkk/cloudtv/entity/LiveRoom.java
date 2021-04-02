package com.hkk.cloudtv.entity;

import com.hkk.cloudtv.core.domain.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 *     Title: LiveRoom.java
 * </p>
 *
 * <p>
 *     Description: 直播间管理类；
 * </p>
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LiveRoom extends IdEntity{
   /* private Long id;// 直播间ID

    private Date addTime;// 直播间创建时间

    private int deleteStatus;
*/
    private String title;// 直播间名称

    private String manager;// 直播间管理人

    private String managerPassword;// 直播间密码

    private String info;// 直播间简介

    private String telphone;// 直播间联系电话

    private Integer isEnable;// 是否禁用直播间 默认0：否 1：是

    private String logoImg;// 直播间封面图片

    private Integer onLine;// 在线人数

    private Integer maxOnline;// 直播间最大在线人数

    private Integer istalk;// 直播间是否禁言  默认0：否 1：是

    private Integer isVip;// 是否游客观看 默认0：否 1：是

    private String pushAddress;// 推流地址

    private String bindCode;// 推流码

    private String rtmp;// rtmp 地址

    private String flv;// flv 地址

    private String websocketFlv;// websocket 地址

    private Long ItemId;// 栏目管理：java、php、python、语文、数学

    private Long deptId;// 部门ID 直播间所属部门

    private Integer isPlayback;// 是否开启回放

}
