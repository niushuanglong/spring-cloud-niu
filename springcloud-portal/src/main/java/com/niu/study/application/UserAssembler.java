package com.niu.study.application;

import com.niu.study.domain.Dept;
import com.niu.study.domain.Job;
import com.niu.study.domain.Role;
import com.niu.study.domain.User;
import com.niu.study.service.dto.DeptSmallDto;
import com.niu.study.service.dto.JobSmallDto;
import com.niu.study.service.dto.RoleSmallDto;
import com.niu.study.service.dto.UserDto;

import java.util.Set;

//dto 转实体用from  实体转dto用TO
public class UserAssembler {

    public UserDto toUser(User user) {

        /*Long id, Set<RoleSmallDto> roles, Set<JobSmallDto> jobs, DeptSmallDto dept, Long deptId,
                   String username, String nickName, String email, String phone, String gender, String avatarName,
                   String avatarPath, String password, Boolean enabled, Boolean isAdmin, Date pwdResetTime*/

        return new UserDto(user.getId(), null, null, null,null,user.getUsername(),
                user.getNickName(), user.getEmail(), user.getPhone(), user.getGender(), user.getAvatarName(), user.getAvatarPath(),
                user.getPassword(), user.getEnabled(), user.getIsAdmin(), user.getPwdResetTime());
    }
    public User fromUser(UserDto dto) {

        return new User(null, null, null, dto.getUsername(),
                dto.getNickName(), dto.getEmail(), dto.getPhone(), dto.getGender(), dto.getAvatarName(), dto.getAvatarPath(),
                dto.getPassword(), dto.getEnabled(), dto.getIsAdmin(), dto.getPwdResetTime());
    }

//    public Set<Role> fromRoles(Set<RoleSmallDto> dtoSet) {
//        return new User(dto.getRoles(), dto.getJobs(), dto.getDept(), dto.getUsername(),
//                dto.getNickName(), dto.getEmail(), dto.getPhone(), dto.getGender(), dto.getAvatarName(), dto.getAvatarPath(),
//                dto.getPassword(), dto.getEnabled(), dto.getIsAdmin(), dto.getPwdResetTime());
//    }
//    public Set<Job> fromJob(Set<JobSmallDto> dtoSet) {
//
//        return new User(dto.getRoles(), dto.getJobs(), dto.getDept(), dto.getUsername(),
//                dto.getNickName(), dto.getEmail(), dto.getPhone(), dto.getGender(), dto.getAvatarName(), dto.getAvatarPath(),
//                dto.getPassword(), dto.getEnabled(), dto.getIs Admin(), dto.getPwdResetTime());
//    }
//    public Set<Dept> fromDept(Set<DeptSmallDto> dtoSet) {
//
//        return new User(dto.getRoles(), dto.getJobs(), dto.getDept(), dto.getUsername(),
//                dto.getNickName(), dto.getEmail(), dto.getPhone(), dto.getGender(), dto.getAvatarName(), dto.getAvatarPath(),
//                dto.getPassword(), dto.getEnabled(), dto.getIsAdmin(), dto.getPwdResetTime());
//    }

}