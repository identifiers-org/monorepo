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


  handleChangeField = (newValue) => {
    const { handleChangeField, fieldName } = this.props;
    const value = typeof newValue !== 'undefined' ? newValue : this.state.defaultValue;

    this.setState({value});
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
    const isCheckbox = fieldChild.props.type === 'checkbox';

    const preparedFieldChild = React.cloneElement(fieldChild, {
      className: `form-control${modified ? ' border-warning border-2' : ''}${isCheckbox ? ' checkbox-input' : ''}`,
      value,
      checked: value,
      onChange: (e) => handleChangeField(fieldChild.props.type === 'checkbox' ? e.target.checked : e.target.value)
    });

    return (
      <div className={`input-group input-group-sm${isCheckbox ? ' justify-content-between' : ''}`}>
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
