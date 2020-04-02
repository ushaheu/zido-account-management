package com.logistics.zido.account.management.repository;

import com.logistics.zido.account.management.entities.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, String> {
}
