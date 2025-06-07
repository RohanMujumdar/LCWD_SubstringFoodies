package com.substring.foodies.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class RoleEntity {

    @Id
    private int id;

    private String name;

    @ManyToMany(mappedBy = "roleEntityList")
    private List<User> userList=new ArrayList<>();
}
