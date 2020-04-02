package com.logistics.zido.account.management.repository;

import com.logistics.zido.account.management.entities.VehicleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleDetailRepository extends JpaRepository<VehicleDetail, Long> {

    boolean existsByVehicleNumber(String vehicleNumber);

    List<VehicleDetail> findByDriverAssignedStatus(boolean driverAssignedStatus);
}
