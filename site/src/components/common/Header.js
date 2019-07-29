import React from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';

// Actions.
import { doSignIn, doSignOut } from '../../actions/Auth';

// Assets.
import identifiersLogo from '../../assets/identifiers_logo.png';

// Components.
import EBINavBar from './EBINavBar';
import EBINavItem from './EBINavItem';
import Sticky from './Sticky';


class Header extends React.Component {
  constructor(props) {
    super(props);
  }


  handleClickSignIn = () => {
    const { auth, doSignIn } = this.props;

    if (typeof auth.authenticated !== 'undefined' && !auth.authenticated) {
      doSignIn();
    }
  }

  handleClickSignOut = () => {
    const { auth, doSignOut } = this.props;

    if (typeof auth.authenticated !== 'undefined' && auth.authenticated) {
      doSignOut();
    }
  }


  render() {
    const {
      handleClickSignIn,
      handleClickSignOut,
      props: { auth, config }
    } = this;

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
                      <p className="logo-subtitle">Central registry</p>
                    </div>
                  </div>
                </div>

                <div className="sticky-placeholder">
                  <Sticky>
                    <EBINavBar>
                      <EBINavItem className="nav-item">
                        <a href={config.satelliteUrl} className="nav-link nav-link-dark">
                          <i className="icon icon-common icon-external-link-alt mr-1" />Resolution
                        </a>
                      </EBINavItem>

                      <EBINavItem className="nav-item">
                        <NavLink exact to="/" className="nav-link" activeClassName="active">
                          <i className="icon icon-common icon-list mr-1" />Registry
                        </NavLink>
                      </EBINavItem>

                      <EBINavItem className="nav-item">
                        <NavLink to="/registry" className="nav-link" activeClassName="active">
                          <i className="icon icon-common icon-search mr-1" />Browse the registry
                        </NavLink>
                      </EBINavItem>

                      <EBINavItem className="nav-item">
                        <NavLink to="/prefixregistrationrequest" className="nav-link" activeClassName="active">
                          <i className="icon icon-common icon-hand-point-up mr-1" />Request prefix
                        </NavLink>
                      </EBINavItem>

                      <EBINavItem>
                        <a href={config.documentationUrl} className="nav-link nav-link-dark">
                          <i className="icon icon-common icon-documentation mr-1" />Documentation
                        </a>
                      </EBINavItem>

                      <EBINavItem>
                        <a href={config.oldIdentifiersUrl} target="_blank" rel="noopener noreferrer" className="nav-link nav-link-dark">
                          <i className="icon icon-common icon-home mr-1" />Legacy platform
                        </a>
                      </EBINavItem>

                      {
                        config.enableAuthFeatures && (
                          // If not logged in.
                          !auth.authenticated ? (
                            <>
                              <EBINavItem className="nav-item">
                                <a href="#!" onClick={handleClickSignIn}>
                                  <i className="icon icon-common icon-icon-sign-in-alt mr-1" />Sign in
                                </a>
                              </EBINavItem>
                            </>
                          ) : ( // If logged in.
                            //TODO: RoleComponent <- wrapper
                            <>
                              <EBINavItem className="nav-item">
                                <NavLink to="/curator" className="nav-link" activeClassName="active">
                                  <i className="icon icon-common icon-tachometer-alt mr-1" />Curation dashboard
                                </NavLink>
                              </EBINavItem>
                              <EBINavItem className="nav-item">
                                <NavLink to="/account" className="nav-link" activeClassName="active">
                                  <i className="icon icon-common icon-user mr-1" />Account
                                </NavLink>
                              </EBINavItem>
                              <EBINavItem className="nav-item">
                                <a href="#!" onClick={handleClickSignOut}>
                                  <i className="icon icon-common icon-icon-sign-out-alt mr-1" />Sign out
                                </a>
                              </EBINavItem>
                            </>
                          )
                        )
                      }
                      <EBINavItem className="nav-item float-right">
                        <a href={config.feedbackUrl} target="_blank" rel="noopener noreferrer" className="nav-link nav-link-dark">
                          <i className="icon icon-common icon-comments mr-1" />Feedback
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
  auth: state.auth,
  config: state.config
});

const mapDispatchToProps = (dispatch) => ({
  doSignIn: () => dispatch(doSignIn()),
  doSignOut: () => dispatch(doSignOut())
});

export default connect(mapStateToProps, mapDispatchToProps, null, {pure: false})(Header);
