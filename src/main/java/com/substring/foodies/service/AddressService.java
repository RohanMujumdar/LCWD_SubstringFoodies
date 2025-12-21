package com.substring.foodies.service;

import com.substring.foodies.entity.Address;

import java.util.List;

public interface AddressService {

    Address createAddress(Address address);

    Address getAddressById(String id);

    List<Address> getAllAddresses();

    Address updateAddress(String id, Address address);

    void deleteAddress(String id);

    Address patchAddress(String id, Address patch);

}
