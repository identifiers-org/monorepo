import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';

import AppRouter from './routers/AppRouter';
import store from './store/store';

import { getConfigFromDevopsApi } from './actions/Config';

import './styles/styles.scss';


// App container.
const jsx = (
  <Provider store={store}>
    <AppRouter />
  </Provider>
);


// Get initial data.
// Configuration from devops endpoint, which will be residing in the same url as the app.
const configUrlPort = process.env.NODE_ENV === 'development' ? 9090 : window.location.port;
const configUrl = `${window.location.protocol}//${window.location.hostname}:${configUrlPort}`;

store.dispatch(getConfigFromDevopsApi(configUrl));

// Render app.
ReactDOM.render(jsx, document.getElementById("app"));
