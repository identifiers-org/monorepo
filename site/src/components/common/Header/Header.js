import React from 'react';
import { NavLink } from 'react-router-dom';

import identifiersLogo from '../../../assets/identifiers_logo.png';


class Header extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      navCollapsed: true
    };
  }

  handleToggleNav = () => {
    this.setState({navCollapsed: !this.state.navCollapsed});
  }

  render() {
    return (
      // This is EMBL-EBI Enforced boilerplate code.
      <>
        <header id="masthead-black-bar" className="clearfix masthead-black-bar">
          <nav className="fixblackbar">
            <ul id="global-nav" className="menu">
              <li className="home-mobile"><a href="//www.ebi.ac.uk"></a></li>
              <li className="home"><a href="//www.ebi.ac.uk">EMBL-EBI</a></li>
              <li className="services"><a href="//www.ebi.ac.uk/services">Services</a></li>
              <li className="research"><a href="//www.ebi.ac.uk/research">Research</a></li>
              <li className="training"><a href="//www.ebi.ac.uk/training">Training</a></li>
              <li className="about"><a href="//www.ebi.ac.uk/about">About us</a></li>
              <li className="search">
                <a href="#" data-toggle="search-global-dropdown"><span className="show-for-small-only">Search</span></a>
                <div id="search-global-dropdown" className="dropdown-pane" data-dropdown data-options="closeOnClick:true;"></div>
              </li>
              <li className="float-right show-for-medium embl-selector">
                <button className="button float-right" type="button" data-toggle="embl-dropdown">Hinxton</button>
              </li>
            </ul>
          </nav>
        </header>

        <div id="content">
        <div data-sticky-container>
          <div id="masthead" className="masthead" data-sticky data-sticky-on="large" data-top-anchor="main-content-area:top" data-btm-anchor="main-content-area:bottom">
            <div className="masthead-inner row">
              <div className="columns medium-12" id="local-title">
                <div className="d-flex ml-2">
                  <img src={identifiersLogo} />
                  <div className="logo-text">
                    <h1>Identifiers.org</h1>
                    <p className="logo-subtitle">Resolution service</p>
                  </div>
                </div>
              </div>

              <nav>
                <ul id="local-nav" className="dropdown menu float-left" data-description="navigational">
                  <li><a href="../">Overview</a></li>
                  <li><a data-open="modal-download">Download</a></li>
                  <li><a href="#">Support <i className="icon icon-generic" data-icon="x"></i></a></li>
                </ul>
              </nav>
            </div>
          </div>
        </div>
        </div>
      </>
    );
  }
}


export default Header;
