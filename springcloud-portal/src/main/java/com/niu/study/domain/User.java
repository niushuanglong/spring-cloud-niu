package com.niu.study.domain;

import com.niu.study.domain.base.BaseEntity;
import com.niu.study.utils.Sm4Utils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="sys_user")
public class User extends BaseEntity implements Serializable {

    @ManyToMany(fetch = FetchType.EAGER)
    @ApiModelProperty(value = "用户角色")
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Job> jobs;

    @OneToOne
    @JoinColumn(name = "dept_id")
    @ApiModelProperty(value = "用户部门")
    private Dept dept;

    @Column(unique = true)
    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @Email
    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "用户性别")
    private String gender;

    @ApiModelProperty(value = "头像真实名称",hidden = true)
    private String avatarName;

    @ApiModelProperty(value = "头像存储的路径", hidden = true)
    private String avatarPath;

    @ApiModelProperty(value = "密码")
    private String password;

    @Type(type = "java.lang.Boolean")
    @ApiModelProperty(value = "是否启用")
    private boolean enabled=true;

    @ApiModelProperty(value = "是否为admin账号", hidden = true)
    @Type(type = "java.lang.Boolean")
    private boolean isAdmin = false;

    @Column(name = "pwd_reset_time")
    @ApiModelProperty(value = "最后修改密码的时间", hidden = true)
    private Date pwdResetTime;



    public User(Set<Role> roles, Set<Job> jobs, Dept dept, String username,
                String nickName, String email, String phone, String gender,
                String avatarName, String avatarPath, String password,
                boolean enabled, boolean isAdmin, Date pwdResetTime) {
        this.roles = roles;
        this.jobs = jobs;
        this.dept = dept;
        this.username = username;
        this.nickName = nickName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.avatarName = avatarName;
        this.avatarPath = avatarPath;
        this.password = Sm4Utils.encryptSm4(password);
        this.enabled = enabled;
        this.isAdmin = isAdmin;
        this.pwdResetTime = pwdResetTime;
    }

    //获得原始密码
    public String getDecPassword() {
        return Sm4Utils.decryptSm4(password);
    }


}