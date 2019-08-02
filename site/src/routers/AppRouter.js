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
import ResourceRegistrationRequestPage from '../components/pages/ResourceRegistrationRequestPage';

// Router.
import PrivateRoute from './privateRoute';


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
          <Route exact path="/resourceregistrationrequest" component={ResourceRegistrationRequestPage} />
          {config.enableAuthFeatures && <PrivateRoute exact path="/curator" component={CuratorDashboardPage} requiredRoles={['curationDashboard']} />}
          {config.enableAuthFeatures && <PrivateRoute exact path="/curator/:id" component={ManagePrefixRegistrationRequestPage} requiredRoles={['curationDashboard']} />}
          {config.enableAuthFeatures && <PrivateRoute path="/account" component={AccountPage} requiredRoles={['curationDashboard']} />}
          <Route component={NotFoundPage} />
        </Switch>
      </div>
      <Footer />
    </>
  </BrowserRouter>
);


export default AppRouter;
