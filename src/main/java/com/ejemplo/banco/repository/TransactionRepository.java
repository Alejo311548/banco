package com.ejemplo.banco.repository;



import com.ejemplo.banco.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Método para buscar transacciones donde la cuenta sea remitente o destinataria
    List<Transaction> findBySenderAccountNumberOrReceiverAccountNumber(
            String senderAccountNumber, String receiverAccountNumber
    );
}

