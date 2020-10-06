package com.vaccinesoap.main.service;

import com.vaccinesoap.main.model.VaccineEntity;
import java.util.List;

public interface VaccineService {

    List<VaccineEntity> getAllVaccines();

    VaccineEntity getVaccineById(long id);

    boolean addVaccine(VaccineEntity vaccineEntity);

    void updateVaccine(VaccineEntity vaccineEntity);

    void deleteVaccine(VaccineEntity vaccineEntity);
}
