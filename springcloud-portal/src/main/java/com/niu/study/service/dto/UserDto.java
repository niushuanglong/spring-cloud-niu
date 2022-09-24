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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.niu.study.domain.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Data
@ApiModel(value = "用户DTO信息")
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserDto extends BaseDTO implements Serializable {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "角色信息")
    private List<RoleSmallDto> roles;
    @ApiModelProperty(name = "岗位信息")
    private List<JobSmallDto> jobs;
    @ApiModelProperty(name = "部门信息")
    private DeptSmallDto dept;
    @ApiModelProperty(name = "部门id")
    private Long deptId;
    @ApiModelProperty(name = "用户名")
    private String username;
    @ApiModelProperty(name = "昵称")
    private String nickName;
    @ApiModelProperty(name = "邮箱")
    private String email;
    @ApiModelProperty(name = "手机号")
    private String phone;
    @ApiModelProperty(name = "性别")
    private String gender;
    @ApiModelProperty(name = "头像名称")
    private String avatarName;
    @ApiModelProperty(name = "头像路径")
    private String avatarPath;
    @ApiModelProperty(name = "密码")
    private String password;
    @ApiModelProperty(name = "是否开启")
    private Boolean enabled;
    @ApiModelProperty(name = "是否为admin账号")
    private Boolean isAdmin = false;
    @ApiModelProperty(name = "最后修改密码的时间")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date pwdResetTime;

    public UserDto() {
    }

    public UserDto(Long id, List<RoleSmallDto> roles, List<JobSmallDto> jobs, DeptSmallDto dept, Long deptId,
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
