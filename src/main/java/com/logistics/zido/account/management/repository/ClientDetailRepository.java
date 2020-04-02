package com.logistics.zido.account.management.repository;

import com.logistics.zido.account.management.entities.ClientDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientDetailRepository extends JpaRepository<ClientDetail, Long> {

    List<ClientDetail> findByContactPhoneNumber(String contactPhoneNumber);
}
