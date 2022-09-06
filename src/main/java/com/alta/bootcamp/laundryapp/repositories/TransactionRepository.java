package com.alta.bootcamp.laundryapp.repositories;

import com.alta.bootcamp.laundryapp.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
