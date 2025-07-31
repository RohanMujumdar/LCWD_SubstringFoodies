package com.substring.foodies.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String addressLine;
    private String city;
    private String state;
    private String pincode;
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
