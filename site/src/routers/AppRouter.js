import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import Header from '../components/common/Header';
import Footer from '../components/common/Footer';

import HomePage from '../components/pages/HomePage';
import NotFoundPage from '../components/pages/NotFoundPage';
import ResolvePage from '../components/pages/ResolvePage';

import identifiersLogoImage from '../assets/identifiers_logo.png';
import ebinavBgImage from '../assets/ebinav_bg.svg';


const identifiersLogo = (
  <>
    <img src={identifiersLogoImage} className=""/>
    <div className="logo-text">
      <h1>Identifiers.org</h1>
      <p className="logo-subtitle">Resolution service</p>
    </div>
  </>
);


const AppRouter = () => (
  <BrowserRouter>
    <>
      <Header />
      <div className="container mt-5">
        <Switch>
          <Route exact path="/" component={HomePage} />
          <Route exact path="/resolve" component={ResolvePage} />
        </Switch>
      </div>
      <Footer />
    </>
  </BrowserRouter>
);


export default AppRouter;
