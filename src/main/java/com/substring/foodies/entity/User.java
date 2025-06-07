package com.substring.foodies.entity;

import com.substring.foodies.dto.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name="foodie_users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    private String name;

    @Column(unique = true)
    private String email;
    private String password;
    private String address;
    private String phoneNumber;

//    @Enumerated(EnumType.STRING)
//    private Role role;

    private boolean isAvailable=true;

//    We comment this out, because it does not make any sense. This is a food delivery app and a user has how many restaurants just doesn't make any sense, a restaurant has how many users makes more of a sense.
//    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @ToString.Exclude
//    private List<Restaurant> restaurantList=new ArrayList<>();

    private LocalDateTime createdDateTime;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",

                // Parent's column will be Join Column
                joinColumns = @JoinColumn(name = "user_id"),

                // Child's column will be Inverse Join Column
                inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleEntity> roleEntityList=new ArrayList<>();

    private boolean isEnabled=true;

    private String gender;

}
