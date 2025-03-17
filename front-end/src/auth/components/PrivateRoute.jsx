import React from 'react';
import { Navigate } from 'react-router-dom';
import Cookies from 'js-cookie';

const PrivateRoute = ({ children }) => {
    const token = Cookies.get('accessToken');
    const isAuthenticated = !!token;

    console.log(token);
    console.log(isAuthenticated);

    return isAuthenticated ? (
        children
    ) : (
        <Navigate to="/login" replace />
    );
};

export default PrivateRoute;