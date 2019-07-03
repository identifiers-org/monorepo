import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';

// Store.
import store from './store/store';

// Actions.
import { getLocationListFromRegistry } from './actions/LocationList';
import { authInit } from './actions/Auth';

// Routers.
import AppRouter from './routers/AppRouter';

// CSS.
import './styles/styles.scss';


// App container.
const jsx = (
  <Provider store={store}>
    <AppRouter />
  </Provider>
);


// ==================== APP Initialization ====================
(async () => {
  // Get locations.
  await store.dispatch(getLocationListFromRegistry());

  // Init auth.
  await store.dispatch(authInit());


  // Render app.
  ReactDOM.render(jsx, document.getElementById("app"));

  // Run ebi framework stuff.
  ebiFrameworkPopulateBlackBar();
  ebiFrameworkActivateBlackBar();
  ebiFrameworkExternalLinks();
  ebiFrameworkManageGlobalSearch();
  ebiFrameworkSearchNullError();
  ebiFrameworkHideGlobalNav();
  ebiFrameworkAssignImageByMetaTags();
  ebiFrameworkInsertEMBLdropdown();
  ebiFrameworkUpdateFoot();
  ebiFrameworkUpdateFooterMeta();
  ebiFrameworkIncludeAnnouncements();
  ebiFrameworkRunDataProtectionBanner('1.3');
})()
