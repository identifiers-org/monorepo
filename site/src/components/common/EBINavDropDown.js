import React from 'react';

// Components.
import EBINavItem from './EBINavItem';


class EBINavDropDown extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      expanded: false
    };
  }

  handleClickChild = () => {
    this.setState({expanded: false});
  };
  handleClickExpandLink = () => {this.setState({expanded: !this.state.expanded});};


  render() {
    const {
      handleClickChild,
      handleClickExpandLink,
      props: { handleClickChildFunction, children, caption },
      state: { expanded }
    } = this;

    const childrenList = React.Children.map(children, child => React.cloneElement(child, {handleClickChildFunction: () => {
        handleClickChild();
        handleClickChildFunction ? handleClickChildFunction() : null;
      }
    }));

    return (
      <EBINavItem>
        <a
          href="#!"
          onClick={handleClickExpandLink}
        >
          {caption} <i className="icon icon-common icon-angle-down" />
        </a>
        {expanded && (
          <div>
            <ul className="EBINav-dropdown">
              {childrenList}
            </ul>
          </div>
        )}
      </EBINavItem>
    );
  }
}


export default EBINavDropDown;
