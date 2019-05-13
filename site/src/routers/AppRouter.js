import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import Header from '../components/common/Header';

import { Config } from '../config/config';

import HomePage from '../components/pages/HomePage';
import BrowseRegistryPage from '../components/pages/BrowseRegistryPage';
import NamespaceDetailsPage from '../components/pages/NamespaceDetailsPage';
import PrefixRegistrationRequestPage from '../components/pages/PrefixRegistrationRequestPage';
import CuratorDashboardPage from '../components/pages/CuratorDashboardPage';
import ManagePrefixRegistrationRequestPage from '../components/pages/ManagePrefixRegistrationRequestPage';
import AccountPage from '../components/pages/AccountPage';
import NotFoundPage from '../components/pages/NotFoundPage';


const AppRouter = () => (
  <BrowserRouter>
    <>
      <Header />
      <div className="container mt-5">
        <Switch>
          <Route exact path="/" component={HomePage} />
          <Route exact path="/registry" component={BrowseRegistryPage} />
          <Route exact path="/registry/:prefix" component={NamespaceDetailsPage} />
          <Route exact path="/prefixregistrationrequest" component={PrefixRegistrationRequestPage} />
          {
            Config.enableAuthFeatures && (
              <>
                <Route exact path="/curator" component={CuratorDashboardPage} />
                <Route exact path="/curator/:id" component={ManagePrefixRegistrationRequestPage} />
                <Route path="/account" component={AccountPage} />
                <Route component={NotFoundPage} />
              </>
            )
          }
        </Switch>
      </div>
    </>
  </BrowserRouter>
);


export default AppRouter;
