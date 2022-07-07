package com.niu.study.service.dto;

import com.niu.study.domain.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
@Getter
@Setter
public class DictDetailDto extends BaseDTO implements Serializable {

    private Long id;

    private DictSmallDto dict;

    private String label;

    private String value;

    private Integer dictSort;
}