import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

// Components.
import Header from '../components/common/Header';
import Footer from '../components/common/Footer';

// Config.
import { config } from '../config/Config';

// Pages: Main.
import HomePage from '../components/pages/HomePage';
import BrowseRegistryPage from '../components/pages/BrowseRegistryPage';
import NamespaceDetailsPage from '../components/pages/NamespaceDetailsPage';

// Pages: Meta.
import AccountPage from '../components/pages/AccountPage';
import NotFoundPage from '../components/pages/NotFoundPage';

// Pages: Curation.
import CurationDashboardPage from '../components/pages/CurationDashboardPage';
import PrefixRegistrationRequestPage from '../components/pages/PrefixRegistrationRequestPage';
import ResourceRegistrationRequestPage from '../components/pages/ResourceRegistrationRequestPage';
import ManagePrefixRegistrationRequestPage from '../components/pages/ManagePrefixRegistrationRequestPage';
import ManageResourceRegistrationRequestPage from '../components/pages/ManageResourceRegistrationRequestPage';

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
          {config.enableAuthFeatures && <PrivateRoute exact path="/curation" component={CurationDashboardPage} requiredRoles={['curationDashboard']} />}
          {config.enableAuthFeatures && <PrivateRoute exact path="/curation/prefixRegistration/:id" component={ManagePrefixRegistrationRequestPage} requiredRoles={['curationDashboard']} />}
          {config.enableAuthFeatures && <PrivateRoute exact path="/curation/resourceRegistration/:id" component={ManageResourceRegistrationRequestPage} requiredRoles={['curationDashboard']} />}
          {config.enableAuthFeatures && <PrivateRoute path="/account" component={AccountPage} requiredRoles={['curationDashboard']} />}
          <Route component={NotFoundPage} />
        </Switch>
      </div>
      <Footer />
    </>
  </BrowserRouter>
);


export default AppRouter;
