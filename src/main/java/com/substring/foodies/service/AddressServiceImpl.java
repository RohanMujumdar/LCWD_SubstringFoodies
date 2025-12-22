package com.substring.foodies.service;

import com.substring.foodies.dto.AddressDto;
import com.substring.foodies.entity.Address;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.AddressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AddressDto createAddress(AddressDto addressDto) {
        Address address = modelMapper.map(addressDto, Address.class);
        Address saved = addressRepository.save(address);
        return modelMapper.map(saved, AddressDto.class);
    }

    @Override
    public AddressDto getAddressById(String id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFound("Address not found with id = " + id));

        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public List<AddressDto> getAllAddresses() {
        return addressRepository.findAll()
                .stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .toList();
    }

    @Override
    public AddressDto updateAddress(String id, AddressDto addressDto) {

        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFound("Address not found with id = " + id));

        existingAddress.setAddressLine(addressDto.getAddressLine());
        existingAddress.setCity(addressDto.getCity());
        existingAddress.setState(addressDto.getState());
        existingAddress.setPincode(addressDto.getPincode());
        existingAddress.setCountry(addressDto.getCountry());

        Address updated = addressRepository.save(existingAddress);
        return modelMapper.map(updated, AddressDto.class);
    }

    @Override
    public AddressDto patchAddress(String id, AddressDto patchDto) {

        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFound("Address not found with id = " + id));

        if (patchDto.getAddressLine() != null)
            existingAddress.setAddressLine(patchDto.getAddressLine());

        if (patchDto.getCity() != null)
            existingAddress.setCity(patchDto.getCity());

        if (patchDto.getState() != null)
            existingAddress.setState(patchDto.getState());

        if (patchDto.getPincode() != null)
            existingAddress.setPincode(patchDto.getPincode());

        if (patchDto.getCountry() != null)
            existingAddress.setCountry(patchDto.getCountry());

        Address updated = addressRepository.save(existingAddress);
        return modelMapper.map(updated, AddressDto.class);
    }

    @Override
    public void deleteAddress(String id) {
        if (!addressRepository.existsById(id)) {
            throw new ResourceNotFound("Address not found with id = " + id);
        }
        addressRepository.deleteById(id);
    }
}
