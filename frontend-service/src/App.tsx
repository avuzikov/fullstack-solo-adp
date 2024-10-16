import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './utils/AuthContext';
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import CustomerList from './components/CustomerList';
import ProtectedRoute from './components/ProtectedRoute';
import Navigation from './components/Navigation';

function App() {
    return (
        <AuthProvider>
            <Router>
                <div className="min-h-screen bg-gray-100">
                    <Navigation />
                    <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
                        <Routes>
                            <Route path="/login" element={<LoginForm />} />
                            <Route path="/register" element={<RegisterForm />} />
                            <Route element={<ProtectedRoute />}>
                                <Route path="/customers" element={<CustomerList />} />
                            </Route>
                            <Route
                                path="/"
                                element={
                                    <h1 className="text-3xl font-bold text-center">
                                        Welcome to the Customer Management App
                                    </h1>
                                }
                            />
                        </Routes>
                    </div>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;
