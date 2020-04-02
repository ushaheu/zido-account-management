package com.logistics.zido.account.management.repository;

import com.logistics.zido.account.management.entities.DriverInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverInformationRepository extends JpaRepository<DriverInformation, Long> {

    boolean existsByDriverLicenceNumber(String driverLicenseNumber);
}
