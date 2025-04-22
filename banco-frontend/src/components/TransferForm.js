// src/components/TransferForm.js
import React, { useState } from "react";
import axios from "axios";

const TransferForm = () => {
    const [senderAccountNumber, setSenderAccountNumber] = useState("");
    const [receiverAccountNumber, setReceiverAccountNumber] = useState("");
    const [amount, setAmount] = useState("");
    const [message, setMessage] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();

        const transferRequest = {
            senderAccountNumber,
            receiverAccountNumber,
            amount: parseFloat(amount),
        };

        axios.post("http://localhost:8080/api/customers/transfer", transferRequest)
            .then((response) => {
                setMessage("Transferencia exitosa");
            })
            .catch((error) => {
                setMessage(`Error: ${error.response ? error.response.data : error.message}`);
            });
    };

    return (
        <div className="form-container">
            <h2>Realizar Transferencia</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="senderAccountNumber">Cuenta Origen:</label>
                    <input
                        type="text"
                        id="senderAccountNumber"
                        value={senderAccountNumber}
                        onChange={(e) => setSenderAccountNumber(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="receiverAccountNumber">Cuenta Destino:</label>
                    <input
                        type="text"
                        id="receiverAccountNumber"
                        value={receiverAccountNumber}
                        onChange={(e) => setReceiverAccountNumber(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="amount">Monto:</label>
                    <input
                        type="number"
                        id="amount"
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Transferir</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default TransferForm;
