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
}