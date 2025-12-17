package com.substring.foodies.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "foodie_restaurant")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Restaurant {

    @Id
    private String id;
    private String name;

    @Lob
    private String description;

    @ManyToMany
    @JoinTable(
            name = "restaurant_address", // join table name
            joinColumns = @JoinColumn(name = "restaurant_id"), // this entity's FK
            inverseJoinColumns = @JoinColumn(name = "address_id") // other entity's FK
    )
    private List<Address> addresses = new ArrayList<>();

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    private boolean isOpen=true;

    private boolean isActive=true;

    @ManyToMany
    @JoinTable(
            name = "restaurant_food",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<FoodItems> foodItemsList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> cartList = new ArrayList<>();

    private String banner;

    private LocalDateTime createdDateTime;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @PrePersist
    protected void onCreate()
    {
        createdDateTime = LocalDateTime.now();
    }
}
