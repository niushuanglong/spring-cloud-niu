/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.niu.study.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.niu.study.domain.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

/**
 * 部门信息
 */
@Entity
@Table(name="sys_dept")
public class Dept extends BaseEntity implements Serializable {

    @JSONField(serialize = false)
    @ManyToMany(mappedBy = "depts")
    private Set<Role> roles;

    @ApiModelProperty(value = "排序")
    private Integer deptSort;

    @NotBlank
    @ApiModelProperty(value = "部门名称")
    private String name;

    public Set<Role> getRoles() {
        return roles;
    }

    public Integer getDeptSort() {
        return deptSort;
    }

    public String getName() {
        return name;
    }

    public Dept(){
    }

    public Dept(Set<Role> roles, Integer deptSort, String name) {
        this.roles = roles;
        this.deptSort = deptSort;
        this.name = name;
    }


}