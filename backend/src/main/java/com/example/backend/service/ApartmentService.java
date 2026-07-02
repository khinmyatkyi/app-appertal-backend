package com.example.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.backend.dto.ApartmentDTO;
import com.example.backend.enums.ApartmentStatus;
import com.example.backend.exception.CustomException;
import com.example.backend.model.Apartment;
import com.example.backend.model.Invoice;
import com.example.backend.repository.ApartmentRepository;

@Service
public class ApartmentService {

    @Autowired
    ApartmentRepository repo;

    public ApartmentDTO save(ApartmentDTO apartmentDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Apartment lastApartment = repo.findTopByOrderByIdDesc();

        long nextId = (lastApartment == null) ? 1 : lastApartment.getId() + 1;

        String apartmentCode = String.format("APM%06d", nextId);

        Apartment apartment = new Apartment();
        apartment.setApartmentCode(apartmentCode);
        apartment.setApartmentNumber(apartmentDTO.getApartmentNumber());
        apartment.setBuilding(apartmentDTO.getBuilding());
        apartment.setFloor(apartmentDTO.getFloor());
        apartment.setType(apartmentDTO.getType());
        apartment.setMonthlyRent(apartmentDTO.getMonthlyRent());
        apartment.setStatus(apartmentDTO.getStatus());
        apartment.setCreatedBy(username);

        apartment = repo.save(apartment);

        return new ApartmentDTO(apartment);
    }

    public ApartmentDTO update(ApartmentDTO apartmentDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Apartment apartment = repo.findById(apartmentDTO.getId())
                .orElseThrow(() -> new CustomException("Apartment not found"));

        apartment.setApartmentNumber(apartmentDTO.getApartmentNumber());
        apartment.setBuilding(apartmentDTO.getBuilding());
        apartment.setFloor(apartmentDTO.getFloor());
        apartment.setType(apartmentDTO.getType());
        apartment.setMonthlyRent(apartmentDTO.getMonthlyRent());
        apartment.setStatus(apartmentDTO.getStatus());
        apartment.setUpdatedBy(username);

        apartment = repo.save(apartment);

        return new ApartmentDTO(apartment);
    }

    public List<ApartmentDTO> getAll(String apartmentNumber, String building, List<ApartmentStatus> statuses) {

        List<ApartmentDTO> dtoList = new ArrayList<>();
        
        List<Apartment> list = new ArrayList<>();
        if (statuses == null || statuses.isEmpty()) {
        	list = repo.searchByApartmentNumberAndBuilding(apartmentNumber, building);
        }else {
        	list = repo.searchByApartmentNumberAndBuildingAndStatuses(apartmentNumber, building, statuses);
        }  

        for (Apartment a : list) {
            dtoList.add(new ApartmentDTO(a));
        }

        return dtoList;
    }

    public ApartmentDTO getById(long id) {

        Optional<Apartment> opt = repo.findById(id);

        if (!opt.isPresent()) {
            throw new CustomException("Apartment not found");
        }

        return new ApartmentDTO(opt.get());
    }
}
