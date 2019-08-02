import React from 'react';

// Components.
import EBINavDropDown from './EBINavDropDown';


class EBINavBar extends React.Component {
  constructor(props) {
    super(props);

    this.state = {width: window.innerWidth};
  }


  componentDidMount() {
    window.addEventListener('resize', () => this.setState({width: window.innerWidth}));
  }


  render() {
    const {
      props: { children, modifiers }
    } = this;

    const modifierClasses = modifiers.length ? modifiers.reduce((result, modifier) => {
      result = `${result} ${EBINavBar.baseClass}--${modifier}`;
      return result;
    }, '') : '';


    const visibleElements = Math.floor(window.innerWidth / 170) - 1;
    const childrenList = React.Children.map(children, child => child);

    return (
      <div className={`${EBINavBar.baseClass} ${modifierClasses}`}>
        <nav>
          <ul id="local-nav" className="dropdown menu float-left" data-description="navigational">
            {childrenList.slice(0, visibleElements)}
            {childrenList.length > visibleElements && (
              <EBINavDropDown caption="Also in this section">
                {childrenList.slice(visibleElements)}
              </EBINavDropDown>
            )}
          </ul>
        </nav>
      </div>
    );
  }
}

EBINavBar.baseClass = 'EBINavBar';

export default EBINavBar;
