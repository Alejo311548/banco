package com.ejemplo.banco.service;

import com.ejemplo.banco.dto.TransactionDTO;
import com.ejemplo.banco.dto.TransferRequestDTO; // ← Agregado para usar TransferRequestDTO
import com.ejemplo.banco.model.Customer;
import com.ejemplo.banco.model.Transaction;
import com.ejemplo.banco.repository.CustomerRepository;
import com.ejemplo.banco.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;


import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // ← NUEVO: Método compatible con el controlador y TransferRequestDTO
    public void transfer(TransferRequestDTO dto) {
        Customer sender = customerRepository.findByAccountNumber(dto.getSenderAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta origen no encontrada"));

        Customer receiver = customerRepository.findByAccountNumber(dto.getReceiverAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta destino no encontrada"));

        if (sender.getBalance() < dto.getAmount()) {
            throw new IllegalArgumentException("Saldo insuficiente en cuenta origen");
        }

        sender.setBalance(sender.getBalance() - dto.getAmount());
        receiver.setBalance(receiver.getBalance() + dto.getAmount());

        customerRepository.save(sender);
        customerRepository.save(receiver);

        // ← Transacción de débito
        Transaction debitTransaction = new Transaction();
        debitTransaction.setSenderAccountNumber(sender.getAccountNumber());
        debitTransaction.setReceiverAccountNumber(receiver.getAccountNumber());
        debitTransaction.setAmount(-dto.getAmount()); // ← Monto negativo para el emisor
        debitTransaction.setDescription("TRANSFERENCIA ENVÍO"); // ← Agregado

        // ← Transacción de crédito
        Transaction creditTransaction = new Transaction();
        creditTransaction.setSenderAccountNumber(sender.getAccountNumber());
        creditTransaction.setReceiverAccountNumber(receiver.getAccountNumber());
        creditTransaction.setAmount(dto.getAmount()); // ← Monto positivo para el receptor
        creditTransaction.setDescription("TRANSFERENCIA RECEPCIÓN"); // ← Agregado

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
    }

    // ← Mantenido para compatibilidad (puede usarse en pruebas o front)
    public TransactionDTO transferMoney(TransactionDTO transactionDTO) {
        if (transactionDTO.getSenderAccountNumber() == null ||
                transactionDTO.getReceiverAccountNumber() == null) {
            throw new IllegalArgumentException("Sender Account Number or Receiver Account Number cannot be null");
        }

        Customer sender = customerRepository.findByAccountNumber(transactionDTO.getSenderAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Sender Account Number not found"));

        Customer receiver = customerRepository.findByAccountNumber(transactionDTO.getReceiverAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Receiver Account Number not found"));

        if (sender.getBalance() < transactionDTO.getAmount()) {
            throw new IllegalArgumentException("Sender Balance not enough");
        }

        sender.setBalance(sender.getBalance() - transactionDTO.getAmount());
        receiver.setBalance(receiver.getBalance() + transactionDTO.getAmount());

        customerRepository.save(sender);
        customerRepository.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setSenderAccountNumber(sender.getAccountNumber());
        transaction.setReceiverAccountNumber(receiver.getAccountNumber());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription("TRANSFERENCIA"); // ← Agregado para uniformidad

        transaction = transactionRepository.save(transaction);

        TransactionDTO savedTransaction = new TransactionDTO();
        savedTransaction.setId(transaction.getId());
        savedTransaction.setSenderAccountNumber(transaction.getSenderAccountNumber());
        savedTransaction.setReceiverAccountNumber(transaction.getReceiverAccountNumber());
        savedTransaction.setAmount(transaction.getAmount());

        return savedTransaction;
    }

    // ← NUEVO: Devuelve las transacciones como modelo, no DTO (para uso con Mapper)
    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        return transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber(accountNumber, accountNumber);
    }

    // ← Mantenido por compatibilidad si quieres usar DTO directamente
    public List<TransactionDTO> getTransactionsForAccount(String accountNumber) {
        List<Transaction> transactions = transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber(
                accountNumber, accountNumber);

        return transactions.stream().map(transaction -> {
            TransactionDTO dto = new TransactionDTO();
            dto.setId(transaction.getId());
            dto.setSenderAccountNumber(transaction.getSenderAccountNumber());
            dto.setReceiverAccountNumber(transaction.getReceiverAccountNumber());
            dto.setAmount(transaction.getAmount());
            return dto;
        }).collect(Collectors.toList());
    }
}
