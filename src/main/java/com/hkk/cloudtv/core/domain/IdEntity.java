package com.hkk.cloudtv.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class IdEntity implements Serializable {

    private Long id;//

    private Date addTime;// 添加时间

    private int deleteStatus;// 删除状态

    
}
