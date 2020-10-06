package com.vaccinesoap.main.service;

import com.vaccinesoap.main.model.VaccineEntity;
import com.vaccinesoap.main.repository.VaccineRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VaccineServiceImpl implements VaccineService{

    @Autowired
    VaccineRepository vaccineRepository;

    public VaccineServiceImpl(VaccineRepository vaccineRepository){
        vaccineRepository = this.vaccineRepository;
    }

    @Override
    public List<VaccineEntity> getAllVaccines() {
        List<VaccineEntity> vaccineEntities = new ArrayList<>(vaccineRepository.findAll());
        return vaccineEntities;
    }

    @Override
    public VaccineEntity getVaccineById(long id) {
        VaccineEntity vaccineEntity = vaccineRepository.findById(id);
        return vaccineEntity;
    }

    @Override
    public synchronized boolean addVaccine(VaccineEntity vaccineEntity) {
        List<VaccineEntity> vaccineEntities = vaccineRepository.findByModelAndSerialNumber(vaccineEntity.getModel(), vaccineEntity.getSerialNumber());
        if (vaccineEntities.size() > 0) {
            return false;
        } else {
            vaccineRepository.save(vaccineEntity);
            return true;
        }
    }

    @Override
    public void updateVaccine(VaccineEntity vaccineEntity) {
        vaccineRepository.save(vaccineEntity);
    }

    @Override
    public void deleteVaccine(VaccineEntity vaccineEntity) {
        vaccineRepository.delete(vaccineEntity);
    }
}
