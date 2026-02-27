package com.example.bankcards.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.example.bankcards.dto.response.Role;
import com.example.bankcards.dto.response.User;
import com.example.bankcards.entity.RoleEntity;
import com.example.bankcards.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = SPRING)
public interface UserMapper {

    User mapUserToDto(UserEntity source);

    @Mapping(target = "name", expression = "java(source.getName().substring(5))")
    Role mapRoleToDto(RoleEntity source);
}
