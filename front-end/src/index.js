import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import AppWrapper from './App';
import reportWebVitals from './reportWebVitals';
import { Provider } from "react-redux";
import store from './store';
import { NavermapsProvider } from 'react-naver-maps';

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
    
    // <React.StrictMode>
    <Provider store={store}>
    <NavermapsProvider >
      <AppWrapper />
    </NavermapsProvider>
    </Provider>
    // </React.StrictMode>

);

reportWebVitals();
