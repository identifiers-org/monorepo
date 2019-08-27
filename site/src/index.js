import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';

import moment from 'moment';

// Store.
import store from './store/store';

// Actions.
import { authInit, saveAuthRenewalIntervalHandler } from './actions/Auth';
import { getInstitutionListFromRegistry } from './actions/InstitutionList';
import { getLocationListFromRegistry } from './actions/LocationList';
import { getSchemaOrgMetadataFromRegistry, getSchemaOrgMetadataByPrefixFromRegistry } from './actions/SchemaOrgMetadata';

// Routers.
import AppRouter from './routers/AppRouter';

// Utils.
import { appendSchemaOrg } from './utils/schemaOrg';

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
  store.dispatch(getLocationListFromRegistry());
  // Get institutions.
  store.dispatch(getInstitutionListFromRegistry());
  // Get Schema.org Metadata depending on current path and append it to the document's head.
  const prefix = window.location.pathname.split('/').pop();

  if (window.location.pathname.includes('registry') && (prefix !== "registry" && prefix !== "")) {
    await store.dispatch(getSchemaOrgMetadataByPrefixFromRegistry(prefix));
  } else {
    await store.dispatch(getSchemaOrgMetadataFromRegistry());
  }

  appendSchemaOrg(store.getState().schemaOrgMetadata);

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
