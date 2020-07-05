package com.ihrm.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
    分页
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    private Long total;     //总条数
    private List<T> rows;   //数据列表
}
