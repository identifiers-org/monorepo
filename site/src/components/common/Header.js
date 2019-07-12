import React from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';

import identifiersLogo from '../../assets/identifiers_logo.png';

import Sticky from './Sticky';
import EBINavBar from './EBINavBar';
import EBINavItem from './EBINavItem';


class Header extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { config } = this.props;

    return (
      // This is EMBL-EBI Enforced boilerplate header.
      <>
        <header id="masthead-black-bar" className="clearfix masthead-black-bar expanded">
          <nav>
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
              <div className="masthead-inner row expanded">
                <div className="columns medium-12" id="local-title">
                  <div className="d-flex ml-3 mb-2">
                    <img src={identifiersLogo} />
                    <div className="logo-text">
                      <h1>Identifiers.org</h1>
                      <p className="logo-subtitle">Resolution service</p>
                    </div>
                  </div>
                </div>

                <div className="sticky-placeholder">
                  <Sticky>
                    <EBINavBar>
                      <EBINavItem className="nav-item">
                        <NavLink exact to="/" className="nav-link nav-link-dark" activeClassName="active">
                          <i className="icon icon-common icon-external-link-alt" /> Resolution
                        </NavLink>
                      </EBINavItem>

                      <EBINavItem className="nav-item">
                        <a href={config.registryUrl} className="nav-link nav-link-dark">
                          <i className="icon icon-common icon-list" /> Registry
                        </a>
                      </EBINavItem>

                      <EBINavItem className="nav-item">
                        <a href={`${config.registryUrl}/registry`} className="nav-link nav-link-dark">
                          <i className="icon icon-common icon-search" /> Browse the registry
                        </a>
                      </EBINavItem>

                      <EBINavItem className="nav-item">
                        <a href={`${config.registryUrl}/prefixregistrationrequest`} className="nav-link nav-link-dark">
                          <i className="icon icon-common icon-hand-point-up" /> Request prefix
                        </a>
                      </EBINavItem>

                      <EBINavItem className="nav-item">
                        <a href={config.documentationUrl} className="nav-link nav-link-dark">
                          <i className="icon icon-common icon-documentation" /> Documentation
                        </a>
                      </EBINavItem>

                      <EBINavItem className="nav-item">
                        <a href={config.oldIdentifiersUrl} target="_blank" className="nav-link nav-link-dark">
                          <i className="icon icon-common icon-home" /> Legacy platform
                        </a>
                      </EBINavItem>

                      <EBINavItem className="nav-item float-right">
                        <a href={config.feedbackUrl} target="_blank" className="nav-link nav-link-dark">
                          <i className="icon icon-common icon-comments" /> Feedback
                        </a>
                      </EBINavItem>
                    </EBINavBar>
                  </Sticky>
                </div>
              </div>
            </div>
          </div>
        </div>
      </>
    );
  }
}


// Redux mappings.
const mapStateToProps = (state) => ({
  config: state.config
});

export default connect(mapStateToProps)(Header);
