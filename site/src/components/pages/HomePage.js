import React from 'react';

import identifiersLogo from '../../assets/identifiers_logo.png';
import Search from '../HomePage/Search';


class HomePage extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
        <>
          <div className="row justify-content-center">
            <div className="col col-5">
              <div className="logo">
                <img src={identifiersLogo} />
                <div className="logo-text">
                  <h1>Identifiers.org</h1>
                  <p className="logo-subtitle">Resolution service</p>
                </div>
              </div>
            </div>
          </div>
          <div className="row justify-content-center mt-2">
            <div className="col col-sm-12 col-md-11 col-lg-9 col-xl-8">
              <Search />
            </div>
          </div>
        </>
    );
  }
}


export default HomePage;
