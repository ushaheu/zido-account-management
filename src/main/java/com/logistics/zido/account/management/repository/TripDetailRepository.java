package com.logistics.zido.account.management.repository;

import com.logistics.zido.account.management.entities.TripDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripDetailRepository extends JpaRepository<TripDetail, Long> {
}
