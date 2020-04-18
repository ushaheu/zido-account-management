package com.logistics.zido.account.management.repository;

import com.logistics.zido.account.management.entities.Transporter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransporterRepository extends JpaRepository<Transporter, Long> {

    boolean existsByTransporterEmailAddress(String transporterEmailAddress);

    List<Transporter> findByTransporterStatusFalseAndAccountStatusFalse();
}
