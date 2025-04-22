// src/components/CustomerList.js
import React, { useEffect, useState } from 'react';

const CustomerList = () => {
    const [clientes, setClientes] = useState([]);

    useEffect(() => {
        fetch('http://localhost:8080/api/customers')
            .then((res) => res.json())
            .then((data) => setClientes(data))
            .catch((err) => console.error('Error al obtener clientes:', err));
    }, []);

    return (
        <div style={{ padding: '2rem' }}>
            <h2 style={{ marginBottom: '1rem' }}>Listado de Clientes</h2>
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                <tr style={{ backgroundColor: '#f2f2f2' }}>
                    <th style={thStyle}>ID</th>
                    <th style={thStyle}>Nombre</th>
                    <th style={thStyle}>Número de Cuenta</th>
                    <th style={thStyle}>Saldo</th>
                </tr>
                </thead>
                <tbody>
                {clientes.map((cliente) => (
                    <tr key={cliente.id}>
                        <td style={tdStyle}>{cliente.id}</td>
                        <td style={tdStyle}>{cliente.firstName} {cliente.lastName}</td>
                        <td style={tdStyle}>{cliente.accountNumber}</td>
                        <td style={tdStyle}>${cliente.balance.toFixed(2)}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

const thStyle = {
    border: '1px solid #ddd',
    padding: '8px',
    textAlign: 'left',
    fontWeight: 'bold'
};

const tdStyle = {
    border: '1px solid #ddd',
    padding: '8px'
};

export default CustomerList;
