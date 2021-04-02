package com.hkk.cloudtv.entity;

import com.hkk.cloudtv.core.domain.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *     Title: Item.java
 * </p>
 *
 * <p>
 *     Description: 栏目管理类；语文、数学、java;
 * </p>
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Item extends IdEntity {

    private String title;// 栏目标题

    private int sequence;// 索引

}
