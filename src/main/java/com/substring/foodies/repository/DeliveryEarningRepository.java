package com.substring.foodies.repository;

import com.substring.foodies.entity.DeliveryEarning;
import com.substring.foodies.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryEarningRepository extends JpaRepository<DeliveryEarning, Long> {

    List<DeliveryEarning> findByDeliveryBoy(User deliveryBoy);
}
