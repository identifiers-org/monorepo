import React from 'react';
import { BrowserRouter, Route, Routes, Navigate } from 'react-router-dom';
import { connect } from 'react-redux';

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

const condRoute = (auth, element, path) => {
  const hasRole = auth.keycloak.hasResourceRole('curationDashboard'),
        redirectElem = <Navigate replace to={"/"} />;
  return <Route path={path} element={hasRole ? element : redirectElem} />
}

const AppRouter = (props) => (
  <BrowserRouter>
    <>
      <Header />
      <div className="container">
        <div className="w-100 pt-5">
          <Routes>
            <Route path="/" element={<HomePage/>} />
            <Route path="/registry" element={<BrowseRegistryPage/>} />
            <Route path="/registry/:prefix" element={<NamespaceDetailsPage/>} />
            <Route path="/prefixregistrationrequest" element={<PrefixRegistrationRequestPage/>} />
            <Route path="/resourceregistrationrequest" element={<ResourceRegistrationRequestPage/>} />
            { config.enableAuthFeatures && condRoute(props.auth, <CurationDashboardPage/>, "/curation") }
            { config.enableAuthFeatures && condRoute(props.auth, <ManagePrefixRegistrationRequestPage/>,
                "/curation/prefixRegistration/:id") }
            { config.enableAuthFeatures && condRoute(props.auth, <ManageResourceRegistrationRequestPage/>,
                "/curation/resourceRegistration/:id") }
            { config.enableAuthFeatures && condRoute(props.auth, <AccountPage/>, "/account") }
            <Route component={NotFoundPage} />
          </Routes>
        </div>
      </div>
      <Footer />
    </>
  </BrowserRouter>
);

const mapStateToProps = (state) => ({ auth: state.auth });
export default connect(mapStateToProps)(AppRouter);
