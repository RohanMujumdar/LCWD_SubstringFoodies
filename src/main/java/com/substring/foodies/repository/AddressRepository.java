package com.substring.foodies.repository;

import com.substring.foodies.entity.Address;
import com.substring.foodies.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);
}
