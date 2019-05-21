import React from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';

import identifiersLogo from '../../assets/identifiers_logo.png';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faHome,
  faSearch,
  faHandPointUp
} from '@fortawesome/free-solid-svg-icons';


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
        <nav className="navbar navbar-expand-md navbar-light bg-light">

          <div className="navbar-brand">
            <div className="mb-0 header-logo">
              <img src={identifiersLogo} className="brand-img"/>
              <h2>Identifiers.org</h2>
            </div>
          </div>

          <button className="navbar-toggler" type="button" onClick={handleToggleNav}>
            <span className="navbar-toggler-icon"></span>
          </button>

          <div className={`collapse navbar-collapse ${navCollapsed ? '' : 'show'}`}>
            <ul className="navbar-nav ml-auto">
              <li className="nav-item">
                <NavLink exact to="/" className="nav-link" activeClassName="active">
                    <FontAwesomeIcon icon={faHome}/> Home
                </NavLink>
              </li>
              <li className="nav-item">
                <a href={config.registryUrl} className="nav-link">
                  <FontAwesomeIcon icon={faSearch}/> Registry
                </a>
              </li>
              <li className="nav-item">
                <a href={config.registryPrefixRegistrationRequestFormUrl} className="nav-link">
                  <FontAwesomeIcon icon={faHandPointUp}/> Request prefix
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
