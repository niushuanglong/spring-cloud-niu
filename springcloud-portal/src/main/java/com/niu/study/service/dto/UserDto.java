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
package com.niu.study.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.niu.study.domain.base.BaseDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Data
public class UserDto extends BaseDTO implements Serializable {

    private Long id;

    private Set<RoleSmallDto> roles;

    private Set<JobSmallDto> jobs;

    private DeptSmallDto dept;

    private Long deptId;

    private String username;

    private String nickName;

    private String email;

    private String phone;

    private String gender;

    private String avatarName;

    private String avatarPath;

    @JSONField(serialize = false)
    private String password;

    private Boolean enabled;

    @JSONField(serialize = false)
    private Boolean isAdmin = false;

    private Date pwdResetTime;


    public UserDto(Long id, Set<RoleSmallDto> roles, Set<JobSmallDto> jobs, DeptSmallDto dept, Long deptId,
                   String username, String nickName, String email, String phone, String gender, String avatarName,
                   String avatarPath, String password, Boolean enabled, Boolean isAdmin, Date pwdResetTime) {
        this.id = id;
        this.roles = roles;
        this.jobs = jobs;
        this.dept = dept;
        this.deptId = deptId;
        this.username = username;
        this.nickName = nickName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.avatarName = avatarName;
        this.avatarPath = avatarPath;
        this.password = password;
        this.enabled = enabled;
        this.isAdmin = isAdmin;
        this.pwdResetTime = pwdResetTime;
    }

}
