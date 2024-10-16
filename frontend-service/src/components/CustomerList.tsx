import React, { useEffect, useState } from 'react';
import { getCustomers, deleteCustomer, createCustomer } from '../services/api';
import { useAuth } from '../utils/AuthContext';
import LoadingSpinner from './LoadingSpinner';

interface Customer {
    id: number;
    name: string;
    email: string;
}

const CustomerList: React.FC = () => {
    const [customers, setCustomers] = useState<Customer[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [newCustomer, setNewCustomer] = useState({ name: '', email: '' });
    const { token } = useAuth();

    const fetchCustomers = async () => {
        if (token) {
            try {
                setLoading(true);
                setError(null);
                const data = await getCustomers(token);
                setCustomers(data);
            } catch (error) {
                console.error('Failed to fetch customers', error);
                setError('Failed to fetch customers. Please try again.');
            } finally {
                setLoading(false);
            }
        }
    };

    useEffect(() => {
        fetchCustomers();
    }, [token]);

    const handleDelete = async (id: number) => {
        if (token) {
            try {
                await deleteCustomer(token, id);
                fetchCustomers();
            } catch (error) {
                console.error('Failed to delete customer', error);
                setError('Failed to delete customer. Please try again.');
            }
        }
    };

    const handleAddCustomer = async (e: React.FormEvent) => {
        e.preventDefault();
        if (token) {
            try {
                await createCustomer(token, newCustomer);
                setNewCustomer({ name: '', email: '' });
                fetchCustomers();
            } catch (error) {
                console.error('Failed to add customer', error);
                setError('Failed to add customer. Please try again.');
            }
        }
    };

    if (loading) return <LoadingSpinner />;
    if (error) return <div className="text-red-500 text-center">{error}</div>;

    return (
        <div className="space-y-6">
            <h2 className="text-2xl font-bold text-center">Customer List</h2>

            <form onSubmit={handleAddCustomer} className="space-y-4 bg-white shadow rounded-lg p-6">
                <h3 className="text-lg font-semibold">Add New Customer</h3>
                <div>
                    <label htmlFor="name" className="block text-sm font-medium text-gray-700">Name</label>
                    <input
                        type="text"
                        id="name"
                        value={newCustomer.name}
                        onChange={(e) => setNewCustomer({...newCustomer, name: e.target.value})}
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                        required
                    />
                </div>
                <div>
                    <label htmlFor="email" className="block text-sm font-medium text-gray-700">Email</label>
                    <input
                        type="email"
                        id="email"
                        value={newCustomer.email}
                        onChange={(e) => setNewCustomer({...newCustomer, email: e.target.value})}
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                        required
                    />
                </div>
                <button
                    type="submit"
                    className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                >
                    Add Customer
                </button>
            </form>

            <ul className="space-y-4">
                {customers.map((customer) => (
                    <li key={customer.id} className="bg-white shadow rounded-lg p-6 flex justify-between items-center">
                        <div>
                            <h3 className="text-lg font-semibold">{customer.name}</h3>
                            <p className="text-gray-600">{customer.email}</p>
                        </div>
                        <button
                            onClick={() => handleDelete(customer.id)}
                            className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                        >
                            Delete
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default CustomerList;