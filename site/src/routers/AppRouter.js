import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import Header from '../components/common/Header';

import HomePage from '../components/pages/HomePage';
import NotFoundPage from '../components/pages/NotFoundPage';
import ResolvePage from '../components/pages/ResolvePage';


const AppRouter = () => (
  <BrowserRouter>
    <>
      <Header />
      <div className="container mt-5">
        <Switch>
          <Route exact path="/" component={HomePage} />
          <Route exact path="/resolve" component={ResolvePage} />
          <Route component={NotFoundPage} />
        </Switch>
      </div>
    </>
  </BrowserRouter>
);


export default AppRouter;
