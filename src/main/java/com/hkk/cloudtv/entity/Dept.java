package com.hkk.cloudtv.entity;

import com.hkk.cloudtv.core.domain.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Dept extends IdEntity {

    private String title;// 部门名称

    private int sequence;// 索引

    private int level;// 级别

    private int type;// 部门类型 平台 其他部门

    private String contact;// 部门联系人

    private String telphone;// 部门联系电话

    private String info;// 部门简介



}
