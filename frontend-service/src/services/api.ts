import axios, { AxiosInstance } from 'axios';

const AUTH_SERVICE_URL = process.env.REACT_APP_AUTH_SERVICE_URL || 'http://localhost:8081';
const DATA_SERVICE_URL = process.env.REACT_APP_DATA_SERVICE_URL || 'http://localhost:8080';

const authApi: AxiosInstance = axios.create({
    baseURL: AUTH_SERVICE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

const dataApi: AxiosInstance = axios.create({
    baseURL: DATA_SERVICE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Auth Service API calls
export const loginUser = async (email: string, password: string): Promise<{ token: string }> => {
    const response = await authApi.post('/account/token', { email, password });
    return response.data;
};

export const registerUser = async (name: string, email: string, password: string): Promise<{ message: string }> => {
    const response = await authApi.post('/account/register', { name, email, password });
    return response.data;
};

export const validateToken = async (token: string): Promise<{ valid: boolean, email?: string }> => {
    const response = await authApi.post('/account/validate', null, {
        headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
};

// Data Service API calls
export const getCustomers = async (token: string): Promise<any[]> => {
    const response = await dataApi.get('/api/customers', {
        headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
};

export const createCustomer = async (token: string, customerData: { name: string, email: string }): Promise<any> => {
    const response = await dataApi.post('/api/customers', customerData, {
        headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
};

export const updateCustomer = async (token: string, id: number, customerData: { name: string, email: string }): Promise<any> => {
    const response = await dataApi.put(`/api/customers/${id}`, customerData, {
        headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
};

export const deleteCustomer = async (token: string, id: number): Promise<void> => {
    await dataApi.delete(`/api/customers/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
    });
};

// Helper function to set the token for authenticated requests
export const setAuthToken = (token: string) => {
    dataApi.defaults.headers.common['Authorization'] = `Bearer ${token}`;
};

export { authApi, dataApi };