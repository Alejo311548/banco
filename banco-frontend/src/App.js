import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import CustomerList from './components/CustomerList';
import TransferForm from './components/TransferForm';
import TransactionHistory from './components/TransactionHistory';

import './App.css';

function App() {
    return (
        <Router>
            <header className="navbar">
                <div className="navbar-title">Banco UdeA</div>
                <nav className="navbar-links">
                    <Link to="/">Consultar Clientes</Link>
                    <Link to="/transfer">Realizar Transferencia</Link>
                    <Link to="/historial">Histórico</Link>

                </nav>
            </header>
            <Routes>
                <Route path="/" element={<CustomerList />} />
                <Route path="/transfer" element={<TransferForm />} />
                <Route path="/historial" element={<TransactionHistory />} />

            </Routes>
        </Router>
    );
}

export default App;
