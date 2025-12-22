package com.substring.foodies.service;

import com.substring.foodies.dto.AddressDto;
import com.substring.foodies.entity.Address;

import java.util.List;

public interface AddressService {

    AddressDto createAddress(AddressDto address);

    AddressDto getAddressById(String id);

    List<AddressDto> getAllAddresses();

    AddressDto updateAddress(String id, AddressDto address);

    void deleteAddress(String id);

    AddressDto patchAddress(String id, AddressDto patch);

}
