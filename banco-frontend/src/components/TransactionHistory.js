import React, { useState } from 'react';
import axios from 'axios';

const TransactionHistory = () => {
    const [accountNumber, setAccountNumber] = useState('');
    const [transactions, setTransactions] = useState([]);
    const [error, setError] = useState('');

    const fetchTransactions = async () => {
        if (!accountNumber) {
            setError('Por favor ingrese un número de cuenta');
            return;
        }

        try {
            const response = await axios.get(`http://localhost:8080/api/customers/${accountNumber}/transactions`);
            setTransactions(response.data);
            setError('');
        } catch (err) {
            setError('Error al obtener las transacciones');
        }
    };

    return (
        <div className="container">
            <h2>Histórico de Transacciones</h2>
            <div className="form-group">
                <label htmlFor="accountNumber">Número de Cuenta:</label>
                <input
                    type="text"
                    id="accountNumber"
                    className="form-control"
                    value={accountNumber}
                    onChange={(e) => setAccountNumber(e.target.value)}
                    placeholder="Ingrese el número de cuenta"
                />
            </div>
            <button onClick={fetchTransactions} className="btn btn-primary">Buscar</button>

            {error && <p className="error-text">{error}</p>}

            <div className="transactions-table">
                {transactions.length > 0 && (
                    <table className="table table-striped mt-4">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Cuenta Origen</th>
                            <th>Cuenta Destino</th>
                            <th>Monto</th>

                            <th>Descripción</th>
                        </tr>
                        </thead>
                        <tbody>
                        {transactions.map((transaction) => (
                            <tr key={transaction.id}>
                                <td>{transaction.id}</td>
                                <td>{transaction.senderAccountNumber}</td>
                                <td>{transaction.receiverAccountNumber}</td>
                                <td>{transaction.amount}</td>

                                <td>{transaction.description}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                )}

                {transactions.length === 0 && <p>No se encontraron transacciones</p>}
            </div>
        </div>
    );
};

export default TransactionHistory;
