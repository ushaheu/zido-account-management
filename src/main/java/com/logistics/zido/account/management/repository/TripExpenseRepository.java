package com.logistics.zido.account.management.repository;

import com.logistics.zido.account.management.entities.TripExpenseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripExpenseRepository extends JpaRepository<TripExpenseDetails, Long> {
}
