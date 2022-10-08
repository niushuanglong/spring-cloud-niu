package com.niu.study.application.assembler;

import com.niu.study.application.dto.RoleSmallDto;
import com.niu.study.domain.Role;
import com.niu.study.domain.User;
import com.niu.study.domain.base.BizBaseAssembler;
import com.niu.study.domain.base.IBeansFactoryService;
import com.niu.study.application.dto.UserDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//dto 转实体用from  实体转dto用TO
public class UserAssembler extends BizBaseAssembler {

    public UserAssembler(IBeansFactoryService beansFactoryService) {
        super(beansFactoryService);
    }

    public UserDto toUser(User user) {

        /*Long id, Set<RoleSmallDto> roles, Set<JobSmallDto> jobs, DeptSmallDto dept, Long deptId,
                   String username, String nickName, String email, String phone, String gender, String avatarName,
                   String avatarPath, String password, Boolean enabled, Boolean isAdmin, Date pwdResetTime*/

        return new UserDto(user.getId().toString(), null, null, null,null,user.getUsername(),
                user.getNickName(), user.getEmail(), user.getPhone(), user.getGender(), user.getAvatarName(), user.getAvatarPath(),
                user.getPassword(), user.isEnabled(), user.isAdmin(), user.getPwdResetTime());
    }
    public User formUser(UserDto dto) {
        Set<Role> roles = this.formRoles(dto.getRoles());
        new Role();
        return new User(roles, null, null, dto.getUsername(),
                dto.getNickName(), dto.getEmail(), dto.getPhone(), dto.getGender(), dto.getAvatarName(), dto.getAvatarPath(),
                dto.getPassword(), dto.getEnabled(), dto.getIsAdmin(), dto.getPwdResetTime());
    }

    public Set<Role> formRoles(List<RoleSmallDto> dtos) {
        return dtos.stream().map(dto->{

        }).collect(Collectors.toList());
    }
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