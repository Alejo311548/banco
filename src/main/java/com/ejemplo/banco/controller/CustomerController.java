package com.ejemplo.banco.controller;

import com.ejemplo.banco.dto.CustomerDTO;
import com.ejemplo.banco.dto.TransactionDTO;
import com.ejemplo.banco.dto.TransferRequestDTO;
import com.ejemplo.banco.mapper.TransactionMapper;
import com.ejemplo.banco.model.Transaction;
import com.ejemplo.banco.service.CustomerService;
import com.ejemplo.banco.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerFacade;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    public CustomerController(CustomerService customerFacade, TransactionService transactionService, TransactionMapper transactionMapper) {
        this.customerFacade = customerFacade;
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerFacade.getAllCustomer());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerFacade.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        if (customerDTO.getBalance() == null) {
            throw new IllegalArgumentException("Balance cannot be null");
        }
        return ResponseEntity.ok(customerFacade.createCustomer(customerDTO));
    }

    // NUEVO: transferencia entre cuentas
    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferRequestDTO transferRequestDTO) {
        try {
            transactionService.transfer(transferRequestDTO); // ← Usa el nuevo método que vamos a crear
            return ResponseEntity.ok("Transferencia exitosa");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al transferir: " + e.getMessage());
        }
    }

    // NUEVO: historial de transacciones
    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions(@PathVariable String accountNumber) {
        List<Transaction> transactions = transactionService.getTransactionsByAccount(accountNumber);
        List<TransactionDTO> dtos = transactions.stream()
                .map(transactionMapper::toDTO) // ← Corrige a toDTO
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
