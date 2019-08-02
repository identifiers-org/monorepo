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


  handleClickExpandLink = () => {
    const { expanded } = this.state;

    this.setState({expanded: !expanded});
  };


  render() {
    const {
      handleClickExpandLink,
      props: { children, caption },
      state: { expanded }
     } = this;

    const childrenList = React.Children.map(children, child => child);
    
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
