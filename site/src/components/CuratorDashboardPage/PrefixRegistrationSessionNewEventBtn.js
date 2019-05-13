import React from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faTimes } from '@fortawesome/free-solid-svg-icons';


class PrefixRegistrationSessionNewEventBtn extends React.Component {
  constructor(props) {
    super(props);

    // If isCancel, the button will look like a cancel button.
    this.state = {
      isCancel: this.props.isCancel
    };
  }


  handleClick = () => {
    const {
      props: { handleShow },
      state: { isCancel }
    } = this;

    this.setState({isCancel: !isCancel});

    handleShow();
  }


  static getDerivedStateFromProps(nextProps, prevState) {
    // If isCancel changed, change state.
    if (nextProps.isCancel !== prevState.isCancel) {
      return {isCancel: nextProps.isCancel};
    } else return null;
  }


  render() {
    const {
      handleClick,
      state: { isCancel },
      props: { icon, caption, color }
    } = this;

    const bgColor = isCancel ? 'bg-gray' : ''

    return (
      <div className={`col p-2 rounded-top-lg ${bgColor}`}>
        <a
          className={`btn ${isCancel ? 'btn-dark' : 'btn-' + color} btn-block`}
          href="#!"
          onClick={handleClick}
        >
          <FontAwesomeIcon icon={isCancel ? faTimes : icon} />&nbsp;
          {isCancel ? `Cancel ${caption.toLowerCase()}` : caption}
        </a>
      </div>
    );
  }
}


// Mapping
export default PrefixRegistrationSessionNewEventBtn;

