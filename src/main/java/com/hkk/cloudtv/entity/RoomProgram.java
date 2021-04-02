package com.hkk.cloudtv.entity;

import com.hkk.cloudtv.core.domain.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *     Title: RoomProgram.java
 * </p>
 *
 * <p>
 *     Description: 直播管理类；两种模式：一个直播对应一个直播间、多个直播对应一个直播间；
 * </p>
 *
 * <p>
 *     author: person: hkk
 * </p>
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RoomProgram extends IdEntity {

    private String title;// 直播节目名称

    private String usernName;// 直播节目创建人

    private Date beginTime;// 直播节目开始时间

    private Date endTime;// 直播节目结束时间

    private int status;// 直播节目状态 0: 关闭 1：开启

    private int type;// 直播节目类型

    private String info;// 直播节目简介

    private String poster;// 节目海报

    private int remark;//

    private int commmentNumber;// 评论数

    private int seeNumber;// 观看人数

    private int goodsNumber;// 点赞人数

    private Long RoomId;// 所属直播间

    private String lecturer;// 讲师

    private int loginNumber;// 签到人数

    private String logoImg;// 直播图片

    private String pushAddress;// 推流地址

    private String bindCode;// 推流码

    private String rtmp;//

}
