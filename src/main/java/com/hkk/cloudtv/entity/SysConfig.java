package com.hkk.cloudtv.entity;

import com.hkk.cloudtv.core.domain.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *     Title：系统配置
 * </p>
 *
 * <author>
 *     HKK
 * </author>
 */

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysConfig extends IdEntity {

    private String ip;

    private String rtmp;//  rtmp://lk.soarmall.com:1935/hls


}
