import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';

import moment from 'moment';

// Store.
import store from './store/store';

// Actions.
import { authInit, saveAuthRenewalIntervalHandler } from './actions/Auth';
import { getInstitutionsListFromRegistry } from './actions/InstitutionList';
import { getLocationListFromRegistry } from './actions/LocationList';

// Routers.
import AppRouter from './routers/AppRouter';

// CSS.
import './styles/styles.scss';
import { renewToken } from './utils/auth';


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
  // Get institutions.
  await store.dispatch(getInstitutionsListFromRegistry());

  // Init auth.
  store.getState().config.enableAuthFeatures && await store.dispatch(authInit());

  // Auth token periodic renewal.
  if (store.getState().auth.authenticated) {
    const keycloak = store.getState().auth.keycloak;
    const tokenDuration = (moment.unix(keycloak.tokenParsed.exp) - moment.unix(keycloak.tokenParsed.iat));

    console.debug(`Auth -> Setting auth token renewal interval to ${(tokenDuration - tokenDuration * .1) / 1000} seconds.`);

    store.dispatch(saveAuthRenewalIntervalHandler(renewToken, tokenDuration - (tokenDuration * .1)));
  }

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
