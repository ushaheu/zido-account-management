package com.logistics.zido.account.management.repository;

import com.logistics.zido.account.management.entities.redis.TokenManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenManagerRepository extends CrudRepository<TokenManager, String> {
}
