import React from 'react';
import { NavLink } from 'react-router-dom';
import { Collapse, Nav, Navbar, NavbarBrand, NavbarToggler, NavItem } from 'reactstrap';

import identifiersLogo from '../../assets/identifiers_logo.png';

import { Config } from '../../config/config';


class Header extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      isOpen: false
    };
  }


  handleToggle = () => {
    this.setState({isOpen: !this.state.isOpen});
  }

  render() {
    return (
      <header>
        <Navbar color="light" light expand="lg">
          <NavbarBrand>
            <div className="mb-0 header-logo">
              <img src={identifiersLogo} className="brand-img"/>
              <h2>Identifiers.org</h2>
            </div>
          </NavbarBrand>
          <NavbarToggler onClick={this.handleToggle} className="mr-2" />

          <Collapse isOpen={this.state.isOpen} navbar>
            <Nav className="ml-auto" navbar>
              <NavItem>
                <NavLink exact to="/" className="nav-link" activeClassName="active">
                  <i className="icon icon-common icon-home" /> Registry
                </NavLink>
              </NavItem>
              <NavItem>
                <NavLink to="/registry" className="nav-link" activeClassName="active">
                  <i className="icon icon-common icon-search" /> Browse the registry
                </NavLink>
              </NavItem>
              <NavItem>
                <NavLink to="/prefixregistrationrequest" className="nav-link" activeClassName="active">
                  <i className="icon icon-common icon-hand-point-up" /> Request prefix
                </NavLink>
              </NavItem>
              {
                Config.enableAuthFeatures && (
                  <>
                    <NavItem>
                      <NavLink to="/curator" className="nav-link" activeClassName="active">
                        <i className="icon icon-common icon-tachometer-alt" /> Curator dashboard
                      </NavLink>
                    </NavItem>
                    <NavItem>
                      <NavLink to="/account" className="nav-link" activeClassName="active">
                        <i className="icon icon-common icon-sign-in-alt" /> Account
                      </NavLink>
                    </NavItem>
                  </>
                )
              }
            </Nav>
          </Collapse>
        </Navbar>
      </header>
    );
  }
}


export default Header;
