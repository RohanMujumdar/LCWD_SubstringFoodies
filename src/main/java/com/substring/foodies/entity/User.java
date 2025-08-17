package com.substring.foodies.entity;
import com.substring.foodies.dto.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
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
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Address> address;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isAvailable=true;

    @OneToMany(mappedBy = "owner", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Restaurant> restaurantList=new ArrayList<>();

    private LocalDate createdDate;


//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(name = "user_role",
//
//                // Parent's column will be Join Column
//                joinColumns = @JoinColumn(name = "user_id"),
//
//                // Child's column will be Inverse Join Column
//                inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private List<RoleEntity> roleEntityList=new ArrayList<>();


    private boolean isEnabled=true;

    private String gender;

    @OneToOne(mappedBy = "creator", cascade = CascadeType.ALL)
    private Cart cart;

    @PrePersist
    protected void onCreate()
    {
        createdDate = LocalDate.now();
    }
}
