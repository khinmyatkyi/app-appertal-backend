package com.example.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.backend.dto.TenantDTO;
import com.example.backend.exception.CustomException;
import com.example.backend.model.Tenant;
import com.example.backend.repository.TenantRepository;

@Service
public class TenantService {

    @Autowired
    TenantRepository repo;

    public TenantDTO save(TenantDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Tenant lastTenant = repo.findTopByOrderByIdDesc();

        long nextId = (lastTenant == null) ? 1 : lastTenant.getId() + 1;

        String tenantCode = String.format("TN%06d", nextId);

        Tenant tenant = new Tenant();
        tenant.setTenantCode(tenantCode);
        tenant.setFirstName(dto.getFirstName());
        tenant.setLastName(dto.getLastName());
        tenant.setPassportNumber(dto.getPassportNumber());
        tenant.setPhone(dto.getPhone());
        tenant.setEmail(dto.getEmail());
        tenant.setAddress(dto.getAddress());
        tenant.setDateOfBirth(dto.getDateOfBirth());
        tenant.setCreatedBy(username);

        tenant = repo.save(tenant);

        return new TenantDTO(tenant);
    }

    public TenantDTO update(TenantDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Tenant tenant = repo.findById(dto.getId())
                .orElseThrow(() -> new CustomException("Tenant not found"));

        tenant.setFirstName(dto.getFirstName());
        tenant.setLastName(dto.getLastName());
        tenant.setPassportNumber(dto.getPassportNumber());
        tenant.setPhone(dto.getPhone());
        tenant.setEmail(dto.getEmail());
        tenant.setAddress(dto.getAddress());
        tenant.setDateOfBirth(dto.getDateOfBirth());
        tenant.setUpdatedBy(username);

        tenant = repo.save(tenant);

        return new TenantDTO(tenant);
    }

    public List<TenantDTO> getAll(String firstName, String lastName, String phone) {

        List<TenantDTO> list = new ArrayList<>();
        List<Tenant> entities = repo.searchByFirstNameAndLastNameAndPhone(firstName, lastName, phone);

        for (Tenant t : entities) {
            list.add(new TenantDTO(t));
        }

        return list;
    }

    public TenantDTO getById(long id) {

        Optional<Tenant> opt = repo.findById(id);

        if (!opt.isPresent()) {
            throw new CustomException("Tenant not found");
        }

        return new TenantDTO(opt.get());
    }
}
