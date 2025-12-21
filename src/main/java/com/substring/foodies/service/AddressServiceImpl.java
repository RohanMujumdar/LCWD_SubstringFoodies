package com.substring.foodies.service;

import com.substring.foodies.entity.Address;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address getAddressById(String id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Address not found with id = " + id));
    }

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Address updateAddress(String id, Address address) {

        Address existingAddress = getAddressById(id);

        existingAddress.setAddressLine(address.getAddressLine());
        existingAddress.setCity(address.getCity());
        existingAddress.setState(address.getState());
        existingAddress.setPincode(address.getPincode());
        existingAddress.setCountry(address.getCountry());

        return addressRepository.save(existingAddress);
    }

    @Override
    public Address patchAddress(String id, Address patch) {
        Address existingAddress = getAddressById(id); // fetch existing

        if (patch.getAddressLine() != null) existingAddress.setAddressLine(patch.getAddressLine());
        if (patch.getCity() != null) existingAddress.setCity(patch.getCity());
        if (patch.getState() != null) existingAddress.setState(patch.getState());
        if (patch.getPincode() != null) existingAddress.setPincode(patch.getPincode());
        if (patch.getCountry() != null) existingAddress.setCountry(patch.getCountry());

        Address updateAddress = addressRepository.save(existingAddress);
        return updateAddress;
    }

    @Override
    public void deleteAddress(String id) {
        Address address = getAddressById(id);
        addressRepository.delete(address);
    }
}
