package com.example.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.backend.dto.MaintenanceRequestDTO;
import com.example.backend.enums.MaintenanceRequestStatus;
import com.example.backend.exception.CustomException;
import com.example.backend.model.Apartment;
import com.example.backend.model.MaintenanceRequest;
import com.example.backend.model.Tenant;
import com.example.backend.repository.ApartmentRepository;
import com.example.backend.repository.MaintenanceRequestRepository;
import com.example.backend.repository.TenantRepository;

@Service
public class MaintenanceRequestService {

    @Autowired
    MaintenanceRequestRepository repo;

    @Autowired
    ApartmentRepository apartmentRepo;

    @Autowired
    TenantRepository tenantRepo;

    public MaintenanceRequestDTO save(MaintenanceRequestDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Apartment apartment = apartmentRepo.findById(dto.getApartmentId())
                .orElseThrow(() -> new CustomException("Apartment not found"));

        Tenant tenant = tenantRepo.findById(dto.getTenantId())
                .orElseThrow(() -> new CustomException("Tenant not found"));
        
        MaintenanceRequest lastRequest = repo.findTopByOrderByIdDesc();

        long nextId = (lastRequest == null) ? 1 : lastRequest.getId() + 1;

        String requestCode = String.format("MT%06d", nextId);

        MaintenanceRequest request = new MaintenanceRequest();
        request.setRequestCode(requestCode);
        request.setRequestDate(dto.getRequestDate());
        request.setAssignDate(dto.getAssignDate());
        request.setDescription(dto.getDescription());
        request.setStatus(dto.getStatus());
        request.setApartment(apartment);
        request.setTenant(tenant);
        request.setCreatedBy(username);

        request = repo.save(request);

        return new MaintenanceRequestDTO(request);
    }

    public MaintenanceRequestDTO update(MaintenanceRequestDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        MaintenanceRequest request = repo.findById(dto.getId())
                .orElseThrow(() -> new CustomException("Maintenance request not found"));

        Apartment apartment = apartmentRepo.findById(dto.getApartmentId())
                .orElseThrow(() -> new CustomException("Apartment not found"));

        Tenant tenant = tenantRepo.findById(dto.getTenantId())
                .orElseThrow(() -> new CustomException("Tenant not found"));

        request.setRequestDate(dto.getRequestDate());
        request.setAssignDate(dto.getAssignDate());
        request.setDescription(dto.getDescription());
        request.setStatus(dto.getStatus());
        request.setApartment(apartment);
        request.setTenant(tenant);
        request.setUpdatedBy(username);

        request = repo.save(request);

        return new MaintenanceRequestDTO(request);
    }

    public List<MaintenanceRequestDTO> getAll(LocalDate requestDate, MaintenanceRequestStatus status, String tenantCode) {

        List<MaintenanceRequestDTO> list = new ArrayList<>();
        List<MaintenanceRequest> entities = repo.searchByRequestDateAndStatusAndTenantId(requestDate, status, tenantCode);

        for (MaintenanceRequest r : entities) {
            list.add(new MaintenanceRequestDTO(r));
        }

        return list;
    }

    public MaintenanceRequestDTO getById(long id) {

        Optional<MaintenanceRequest> opt = repo.findById(id);

        if (!opt.isPresent()) {
            throw new CustomException("Maintenance request not found");
        }

        return new MaintenanceRequestDTO(opt.get());
    }
}
