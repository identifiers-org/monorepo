import React from 'react';


class ReversibleField extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      defaultValue: props.defaultValue,
      value: props.defaultValue
    };
  }


  static getDerivedStateFromProps(nextProps, prevState) {
    return (nextProps.defaultValue !== prevState.defaultValue) ? {defaultValue: nextProps.default, value: nextProps.defaultValue} : null;
  }


  handleChangeField = (value) => {
    const { handleChangeField, fieldName } = this.props;

    this.setState({value: value ? value : this.state.defaultValue});
    handleChangeField(fieldName, value);
  }


  render() {
    const {
      handleChangeField,
      props: { children },
      state: { defaultValue, value }
    } = this;

    const modified = (defaultValue !== value);

    const fieldChild = React.Children.only(children);
    const preparedFieldChild = React.cloneElement(fieldChild, {
      className: `form-control ${ modified && 'border-warning border-2' }`,
      value,
      onChange: (e) => handleChangeField(e.target.value)
    });


    // TODO: COMPOSITE COMPONENT - APPLY PROPS TO CHILD INPUT / TEXTAREA OR SELECT.
    return (
      <div className="input-group input-group-sm">
        {preparedFieldChild}
        {
          modified && (
            <div className="input-group-append">
              <button
                className="btn btn-warning"
                onClick={() => handleChangeField(undefined)}
              >
                Revert
              </button>
              <span className="badge badge-dark ml-2 badge-modified">Modified</span>
            </div>
          )
        }
      </div>
    );
  }
}


export default ReversibleField;
