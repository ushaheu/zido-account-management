package com.logistics.zido.account.management.repository;

import com.logistics.zido.account.management.entities.VehicleMake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleMakeRepository extends JpaRepository<VehicleMake, String> {
}
