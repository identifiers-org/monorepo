import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

// Components.
import Header from '../components/common/Header';
import Footer from '../components/common/Footer';

// Config.
import { config } from '../config/Config';

// Pages.
import AccountPage from '../components/pages/AccountPage';
import BrowseRegistryPage from '../components/pages/BrowseRegistryPage';
import CuratorDashboardPage from '../components/pages/CuratorDashboardPage';
import HomePage from '../components/pages/HomePage';
import ManagePrefixRegistrationRequestPage from '../components/pages/ManagePrefixRegistrationRequestPage';
import NamespaceDetailsPage from '../components/pages/NamespaceDetailsPage';
import NotFoundPage from '../components/pages/NotFoundPage';
import PrefixRegistrationRequestPage from '../components/pages/PrefixRegistrationRequestPage';


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
          {config.enableAuthFeatures && <Route exact path="/curator" component={CuratorDashboardPage} />}
          {config.enableAuthFeatures && <Route exact path="/curator/:id" component={ManagePrefixRegistrationRequestPage} />}
          {config.enableAuthFeatures && <Route path="/account" component={AccountPage} />}
          <Route component={NotFoundPage} />
        </Switch>
      </div>
      <Footer />
    </>
  </BrowserRouter>
);


export default AppRouter;
