package com.shopsphere.service;

import com.shopsphere.model.Address;
import com.shopsphere.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Transactional
    public Address addAddress(Address address) {
        // 1. Fetch all existing addresses for this user
        List<Address> existingAddresses = addressRepository.findByUserId(address.getUserId());

        // 2. If this is the user's very first address, make it default automatically
        if (existingAddresses.isEmpty()) {
            address.setIsDefault(true);
        }
        // 3. If the user is manually setting this new address as default
        else if (Boolean.TRUE.equals(address.getIsDefault())) {
            handleDefaultReset(address.getUserId());
        }

        return addressRepository.save(address);
    }

    private void handleDefaultReset(Long userId) {
        List<Address> addresses = addressRepository.findByUserId(userId);
        for (Address addr : addresses) {
            if (Boolean.TRUE.equals(addr.getIsDefault())) {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            }
        }
    }

    public List<Address> getAddressesByUserId(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    @Transactional
    public Address updateAddress(Long addressId, Address updatedAddress) {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // Update fields
        existingAddress.setStreet(updatedAddress.getStreet());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setState(updatedAddress.getState());
        existingAddress.setPincode(updatedAddress.getPincode());

        // Logic: If user is setting THIS address as default now
        if (Boolean.TRUE.equals(updatedAddress.getIsDefault()) && !Boolean.TRUE.equals(existingAddress.getIsDefault())) {
            handleDefaultReset(existingAddress.getUserId());
            existingAddress.setIsDefault(true);
        } else if (updatedAddress.getIsDefault() != null) {
            existingAddress.setIsDefault(updatedAddress.getIsDefault());
        }

        return addressRepository.save(existingAddress);
    }

    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // Pro-Tip: If deleting a default address, you might want to assign a new default,
        // but for now, a simple delete is fine.
        addressRepository.delete(address);
    }
}