import React from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';

import identifiersLogo from '../../assets/identifiers_logo.png';


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
    const {
      props: { config },
      state: { navCollapsed },
      handleToggleNav
    } = this;

    return (
      <header>
        <nav className="navbar navbar-dark bg-primary pt-4">

          <div className="navbar-brand">
            <div className="mb-0 header-logo">
              <img src={identifiersLogo} className="brand-img header-logo"/>
                <div className="logo-text">
                  <h1>Identifiers.org</h1>
                  <p className="logo-subtitle">Resolution service</p>
                </div>
            </div>
          </div>
        </nav>
        <nav className="navbar navbar-expand navbar-dark bg-primary pb-0">
          <button className="navbar-toggler mb-1" type="button" onClick={handleToggleNav}>
            <span className="navbar-toggler-icon"></span>
          </button>

          <div className={`collapse navbar-collapse ${navCollapsed ? '' : 'show'}`}>
            <ul className="navbar-nav">
              <li className="nav-item">
                <NavLink exact to="/" className="nav-link nav-link-dark" activeClassName="nav-link-active">
                <i className="icon icon-common icon-home" /> Home
                </NavLink>
              </li>
              <div className="nav-item-spacer"></div>
              <li className="nav-item">
                <a href={config.registryUrl} className="nav-link nav-link-dark">
                  <i className="icon icon-common icon-search" /> Registry
                </a>
              </li>
              <div className="nav-item-spacer"></div>
              <li className="nav-item">
                <a href={config.registryPrefixRegistrationRequestFormUrl} className="nav-link nav-link-dark">
                  <i className="icon icon-common icon-hand-point-up" /> Request prefix
                </a>
              </li>
            </ul>
          </div>
        </nav>
      </header>
    );
  }
}


// Redux mappings.
const mapStateToProps = (state) => ({
  config: state.config
});


export default connect(mapStateToProps)(Header);
