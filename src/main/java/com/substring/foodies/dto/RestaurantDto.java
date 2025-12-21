package com.substring.foodies.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RestaurantDto {

    private String id;
    private String name;
    private String description;
    private Set<AddressDto> addresses = new HashSet<>();
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean isOpen=true;
    private boolean isActive = true;
    private String ownerId;
    private String banner;
    private double rating;

    @JsonProperty
    public String imageUrl()
    {
        return "http://localhost:8080/images/" + banner;
    }
}
