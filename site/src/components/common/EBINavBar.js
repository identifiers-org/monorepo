import React from 'react';

class EBINavBar extends React.Component {
  constructor(props) {
    super(props);

    this.state = {width: window.innerWidth, extraItemsVisible: false};
  }


  componentDidMount() {
    window.addEventListener('resize', () => this.setState({width: window.innerWidth}));
  }

  handleClickExtraItems = () => this.setState({extraItemsVisible: !this.state.extraItemsVisible});


  render() {
    const {
      handleClickExtraItems,
      props: { children, modifiers },
      state: { extraItemsVisible }
    } = this;

    const modifierClasses = modifiers.length ? modifiers.reduce((result, modifier) => {
      result = `${result} ${EBINavBar.baseClass}--${modifier}`;
      return result;
    }, '') : '';


    const visibleElements = Math.floor(window.innerWidth / 170) - 1;
    const childrenList = React.Children.map(children, child => child);

    console.log(`visibleElements ${visibleElements}, window.innerWidth`);

    return (
      <div className={`${EBINavBar.baseClass} ${modifierClasses}`}>
        <nav>
          <ul id="local-nav" className="dropdown menu float-left" data-description="navigational">
            {childrenList.slice(0, visibleElements)}
            {childrenList.length > visibleElements && (
              <>
                <li>
                  <a href="#!" onClick={handleClickExtraItems}>Also in this section <i className="icon icon-common icon-angle-down" /></a>
                  <ul>
                    {extraItemsVisible && (
                      <div className="EBINavBar--extra-items-menu">
                        {childrenList.slice(visibleElements)}
                      </div>
                    )}
                  </ul>
                </li>
              </>
            )}
          </ul>
        </nav>
      </div>
    );
  }
}

EBINavBar.baseClass = 'EBINavBar';

export default EBINavBar;
