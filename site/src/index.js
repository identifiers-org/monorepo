import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';

import AppRouter from './routers/AppRouter';
import store from './store/store';

import { getLocationListFromRegistry } from './actions/LocationList';

import './styles/styles.scss';


// App container.
const jsx = (
  <Provider store={store}>
    <AppRouter />
  </Provider>
);


// Get initial data.
// For now, it is only locations.
store.dispatch(getLocationListFromRegistry());


// Render app.
ReactDOM.render(jsx, document.getElementById("app"));
