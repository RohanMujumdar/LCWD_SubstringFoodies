package com.substring.foodies.controller;

import com.substring.foodies.entity.Address;
import com.substring.foodies.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // CREATE
    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody Address address) {
        Address createdAddress = addressService.createAddress(address);
        return ResponseEntity.ok(createdAddress);
    }

    // READ single
    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable String id) {
        Address address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }

    // READ all
    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable String id, @RequestBody Address address) {
        Address updatedAddress = addressService.updateAddress(id, address);
        return ResponseEntity.ok(updatedAddress);
    }

    // PATCH
    @PatchMapping("/{id}")
    public ResponseEntity<Address> patchAddress(@PathVariable String id, @RequestBody Address patch) {
        Address updated = addressService.patchAddress(id, patch);
        return ResponseEntity.ok(updated);
    }


    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
