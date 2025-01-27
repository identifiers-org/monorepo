import React from 'react';
import { connect } from 'react-redux';
import { NavLink as BaseNavLink } from 'react-router-dom';

// Actions.
import { doSignIn, doSignOut } from '../../actions/Auth';

// Assets.
const identifiersLogo = new URL('../../assets/identifiers_logo.png', import.meta.url);

// Components.
import EBINavBar from './EBINavBar';
import EBINavItem from './EBINavItem';
import Sticky from './Sticky';
import EBINavDropDown from './EBINavDropDown';

// This code was written for v5 and code adjusts v6 to have the same behavious as v5
// https://reactrouter.com/en/main/components/nav-link
const NavLink = React.forwardRef(
  ({ activeClassName, activeStyle, ...props }, ref) => {
    return (
      <BaseNavLink
        ref={ref}
        {...props}
        className={({ isActive }) =>
          [
            props.className,
            isActive ? activeClassName : null,
          ]
            .filter(Boolean)
            .join(" ")
        }
        style={({ isActive }) => ({
          ...props.style,
          ...(isActive ? activeStyle : null),
        })}
      />
    );
  }
);


class Header extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    ebiFrameworkPopulateBlackBar();
    ebiFrameworkActivateBlackBar();
    ebiFrameworkExternalLinks();
    ebiFrameworkManageGlobalSearch();
    ebiFrameworkSearchNullError();
    ebiFrameworkHideGlobalNav();
    ebiFrameworkAssignImageByMetaTags();
    ebiFrameworkInsertEMBLdropdown();
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
          <nav className="position-relative">
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
                        <NavLink to="/" className="nav-link" activeClassName="active">
                          <i className="icon icon-common icon-list mr-1" />Registry
                        </NavLink>
                      </EBINavItem>

                      <EBINavItem className="nav-item">
                        <NavLink to="/registry" className="nav-link" activeClassName="active">
                          <i className="icon icon-common icon-search mr-1" />Browse the registry
                        </NavLink>
                      </EBINavItem>

                      <EBINavDropDown
                        caption={<span><i className="icon icon-common icon-hand-point-up mr-1" />Make a request</span>}
                      >
                        <EBINavItem className="nav-item">
                          <NavLink to="/prefixregistrationrequest" className="nav-link" activeClassName="active">
                            <i className="icon icon-common icon-leaf mr-1" />Request prefix
                          </NavLink>
                        </EBINavItem>
                        <EBINavItem className="nav-item">
                          <NavLink to="/resourceregistrationrequest" className="nav-link" activeClassName="active">
                            <i className="icon icon-common icon-cube mr-1" />Request resource
                          </NavLink>
                        </EBINavItem>
                      </EBINavDropDown>

                      <EBINavItem className="nav-item">
                        <a href={config.documentationUrl} className="nav-link nav-link-dark">
                          <i className="icon icon-common icon-documentation mr-1" />Documentation
                        </a>
                      </EBINavItem>

                      {
                        // If not logged in.
                        config.enableAuthFeatures && !auth.authenticated && (
                          <EBINavItem className="nav-item">
                            <a href="#!" onClick={handleClickSignIn}>
                              <i className="icon icon-common icon-sign-in-alt mr-1" />Sign in
                            </a>
                          </EBINavItem>
                        )
                      }

                      {/* Do not nest children, so responsive UI submenu does its work. */}
                      {
                        config.enableAuthFeatures && auth.authenticated && (
                          // If logged in.
                          <EBINavItem className="nav-item">
                            <NavLink to="/curation" className="nav-link" activeClassName="active">
                              <i className="icon icon-common icon-tachometer-alt mr-1" />Curation dashboard
                            </NavLink>
                          </EBINavItem>
                        )
                      }

                      {
                        config.enableAuthFeatures && auth.authenticated && (
                          // If logged in.
                          <EBINavItem className="nav-item">
                            <a href={`${config.authApi}/realms/idorg/account`} target="_blank" rel="noopener noreferrer" className="nav-link nav-link-dark">
                              <i className="icon icon-common icon-user mr-1" />Account
                            </a>
                          </EBINavItem>
                        )
                      }

                      {
                        config.enableAuthFeatures && auth.authenticated && (
                          // If logged in.
                          <EBINavItem className="nav-item">
                            <a href="#!" onClick={handleClickSignOut}>
                              <i className="icon icon-common icon-sign-out-alt mr-1" />Sign out
                            </a>
                          </EBINavItem>
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
