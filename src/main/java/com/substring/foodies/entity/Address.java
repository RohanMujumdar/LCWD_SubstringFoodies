package com.substring.foodies.entity;
import com.substring.foodies.dto.enums.AddressType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseAuditableEntity{

    @Id
    private String id;

    @Column(columnDefinition = "TEXT")
    private String addressLine;
    private String city;
    private String state;
    private String pincode;
    private String country;

    @OneToOne(mappedBy = "address")
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @ManyToMany(mappedBy = "addresses")
    private Set<Restaurant> restaurants = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address other = (Address) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
