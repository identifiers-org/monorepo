import React from 'react';

class EBINavBar extends React.Component {
  render() {
    const { children, modifiers } = this.props;

    const modifierClasses = modifiers.length ? modifiers.reduce((result, modifier) => {
      result = `${result} ${EBINavBar.baseClass}--${modifier}`;
      return result;
    }, '') : '';

    return (
      <div className={`${EBINavBar.baseClass} ${modifierClasses}`}>
        {children}
      </div>
    );
  }
}

EBINavBar.baseClass = 'EBINavBar';

export default EBINavBar;
