package com.vaccinesoap.main.repository;

import com.vaccinesoap.main.model.VaccineEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineRepository extends JpaRepository<VaccineEntity, Long> {
    VaccineEntity findById(long id);

    List<VaccineEntity> findByModelAndSerialNumber(String model, String serialNumber);
}
