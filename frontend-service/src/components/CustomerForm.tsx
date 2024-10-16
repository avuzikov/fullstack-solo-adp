import React, { useState } from 'react';
import { createCustomer } from '../services/api';
import { useAuth } from '../utils/AuthContext';

const CustomerForm: React.FC<{ onCustomerAdded: () => void }> = ({ onCustomerAdded }) => {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const { token } = useAuth();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (token) {
            try {
                await createCustomer(token, { name, email });
                setName('');
                setEmail('');
                onCustomerAdded();
            } catch (error) {
                console.error('Failed to create customer', error);
            }
        }
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            <div>
                <label htmlFor="name" className="block text-sm font-medium text-gray-700">Name</label>
                <input
                    type="text"
                    id="name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm"
                    required
                />
            </div>
            <div>
                <label htmlFor="email" className="block text-sm font-medium text-gray-700">Email</label>
                <input
                    type="email"
                    id="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm"
                    required
                />
            </div>
            <button type="submit" className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                Add Customer
            </button>
        </form>
    );
};

export default CustomerForm;