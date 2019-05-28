import React from 'react';
import { NavLink } from 'react-router-dom';

import { getColorTone } from '../../../utils/colors';


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
      props: { bgColor, bgImage, logo, side },
      state: { navCollapsed },
      handleToggleNav
    } = this;

    const textClass = getColorTone(bgColor) === 'dark' ? 'ebinav-dark' : 'ebinav-light';
    const ebiNavStyle = {
      background: bgColor,
      backgroundImage: `url(${bgImage})`,
      backgroundPosition: 'center',
      backgroundSize: 'cover'
    };


    return (
      <header className="ebinav" style={ebiNavStyle}>
        <div className="ebinav-row ebinav-align-center">
          <div className="ebinav-col">
            <div className={`ebinav-logo ${textClass}`}>
              {logo}
            </div>
          </div>
          <div className="ebinav-col">
            <div className="ebinav-searchbar">
              {side ? side : null}
            </div>
          </div>
        </div>


{/* TODO: CREATE THE CHILDREN LINKS! */}
        <div className="ebinav-row">
          <nav className="">
            <ul className="ebinav-links">
              <li className="">
                <NavLink exact to="/" className="" activeClassName="">
                <i className="icon icon-common icon-home" /> Home
                </NavLink>
              </li>
                {/* <li className="nav-item">
                  <a href={config.registryUrl} className="nav-link nav-link-dark">
                    <i className="icon icon-common icon-search" /> Registry
                  </a>
                </li>
                <div className="nav-item-spacer"></div>
                <li className="nav-item">
                  <a href={config.registryPrefixRegistrationRequestFormUrl} className="nav-link nav-link-dark">
                    <i className="icon icon-common icon-hand-point-up" /> Request prefix
                  </a>
                </li> */}
            </ul>
          </nav>
        </div>
      </header>
    );
  }
}


export default Header;
