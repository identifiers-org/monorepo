import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';

import './styles/styles.scss';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';

import AppRouter from './routers/AppRouter';
import store from './store/store';

import { getConfigFromDevopsApi } from './actions/Config';


// App container.
const jsx = (
  <Provider store={store}>
    <AppRouter />
  </Provider>
);


// Get initial data.
// Configuration from devops endpoint, which will be residing in the same url as the app.
store.dispatch(getConfigFromDevopsApi(window.location.href));


// Render app.
ReactDOM.render(jsx, document.getElementById("app"));
