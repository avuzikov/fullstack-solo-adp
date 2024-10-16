import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../utils/AuthContext';

const Navigation: React.FC = () => {
    const { token, setToken } = useAuth();

    const handleLogout = () => {
        setToken(null);
    };

    return (
        <nav className="bg-white shadow-sm">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16">
                    <div className="flex">
                        <Link to="/" className="flex-shrink-0 flex items-center">Home</Link>
                        {!token && (
                            <>
                                <Link to="/login" className="ml-6 inline-flex items-center px-1 pt-1 text-sm font-medium text-gray-500 hover:text-gray-700">Login</Link>
                                <Link to="/register" className="ml-6 inline-flex items-center px-1 pt-1 text-sm font-medium text-gray-500 hover:text-gray-700">Register</Link>
                            </>
                        )}
                        {token && (
                            <Link to="/customers" className="ml-6 inline-flex items-center px-1 pt-1 text-sm font-medium text-gray-500 hover:text-gray-700">Customers</Link>
                        )}
                    </div>
                    {token && (
                        <button onClick={handleLogout} className="ml-6 inline-flex items-center px-1 pt-1 text-sm font-medium text-gray-500 hover:text-gray-700">Logout</button>
                    )}
                </div>
            </div>
        </nav>
    );
};

export default Navigation;