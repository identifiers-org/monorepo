import React from 'react';
import { connect } from 'react-redux';

import {
  setRegistrationRequestFieldField,
  setValue,
  setValidity,
  setErrorMessage,
  setLabel,
  validationDone
} from '../../actions/RegistrationRequestField';

import { config } from '../../config/Config';


class RegistrationRequestField extends React.Component {
  constructor(props) {
    super(props);

    const value = (this.props.field.value && this.props.field.value !== '') ?
      this.props.field.value
      :
      this.props.defaultValue;

    this.state = {
      value,
      errorMessage: undefined,
      options: this.props.options
    };
  }


  //
  // Lifecycle functions.
  //
  componentDidMount() {
    // Set label in state for outside use.
    this.props.setLabel([this.props.formsection, this.props.label]);

    if (this.props.field.value !== '' && this.props.field.shouldValidate) {
      this.validate();
    }
  }

  componentDidUpdate() {
    // Validate field if shouldValidate is enabled and a request to validate arrives.
    if (this.props.field.requestedValidate) {
      this.validate();
    }
  }

  static getDerivedStateFromProps(nextProps, prevState) {
    // If locations are different than stored in the state, update.
    if (nextProps.options && nextProps.options.length !== prevState.options.length) {
      return {options: nextProps.options};
    }

    // If a field changes, update state.
    if (nextProps.field.value !== prevState.value) {
      const value = nextProps.splitByLines && nextProps.field.value !== '' ?
        nextProps.field.value.join('\n')
        :
        nextProps.field.value;

      return {value};
    }

    return null;
  }


  //
  // Field validation function.
  //
  validate = async () => {
    // Skip validation if disabled.
    if (!this.props.field.shouldValidate) {
      return;
    }

    // Creates payload for body.
    const payload = this.props.customvalidationpayload ?
      this.props.customvalidationpayload()
      :
      {[this.props.id]: this.props.field.value};

    // Create request body.
    let body = {
      apiVersion: config.apiVersion,
      payload
    };

    // Add extra validation fields.
    if (this.props.validationfields) {
      Object.keys(this.props.validationfields).forEach(field => {
        body.payload[field] = this.props.validationfields[field];
      });
    }

    let init = {
      method: 'POST',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify(body)
    };

    let responseStatusCode = 0;

    // Make request and update the store.
    const response = await fetch(this.props.validationurl, init);
    responseStatusCode = response.status;
    const json = await response.json();
    const res = {
      valid: responseStatusCode === 200,
      errorMessage: json.errorMessage
    };
    this.props.setValidity(res.valid);
    this.setState({ errorMessage: res.errorMessage });
    this.props.validationDone();

    return response;
  }


  //
  // Handlers
  //
  handleChange = (e) => {
    // Parse value (split by lines if needed in a textarea).
    const value = this.props.splitByLines ? e.target.value.split('\n') : e.target.value;

    // Send value to store (this will update state also).
    this.props.setValue(value);

    // Validate after a delay if the flag is set.
    setTimeout(() => {
      if (this.props.field.shouldValidate) {
        this.validate();
      }
    }, config.VALIDATION_DELAY);
  }

  handleBlur = (e) => {
    // Parse value (split by lines if needed in a textarea).
    const value = this.props.splitByLines ? e.target.value.split('\n') : e.target.value;

    if (value === '' || !value) {
      return;
    }

    // Send value to store (this will update state also).
    this.props.setValue(value);

    // Validate instantly.
    this.validate();
  }


  render() {
    const id = `form-control-${this.props.id}`;
    const { valid } = this.props.field;
    const { errorMessage, options } = this.state;

    const { validationtooltip } = this.props;

    return (
      <div className="form-group row">
        <label
          className="col-lg-3 col-form-label form-control-label"
          htmlFor={id}
        >
          {this.props.label}{!this.props.required && <p className="text-muted">(Optional)</p>}
        </label>
        <div className="col-lg-9">
          {(() => {
            switch (this.props.type) {
            case "text":
            case "password": return (
              <input
                aria-describedby={`${id}-helpblock`}
                className={`form-control ${typeof valid !== 'undefined' ? valid ? 'is-valid' : 'is-invalid' : ''}`}
                disabled={this.props.disabled}
                id={this.props.id}
                onBlur={this.handleBlur}
                onChange={this.handleChange}
                placeholder={this.props.placeholder}
                type={this.props.type}
                value={this.state.value}
              />
            )
            case "textarea": return (
              <textarea
                aria-describedby={`${id}-helpblock`}
                className={`form-control ${typeof valid !== 'undefined' ? valid ? 'is-valid' : 'is-invalid' : ''} textarea`}
                disabled={this.props.disabled}
                id={this.props.id}
                onBlur={this.handleBlur}
                onChange={this.handleChange}
                placeholder={this.props.placeholder}
                rows={this.props.rows}
                value={this.state.value}
              />
            )
            case "select": return (
              <select
                aria-describedby={`${id}-helpblock`}
                className={`form-control ${typeof valid !== 'undefined' ? valid ? 'is-valid' : 'is-invalid' : ''}`}
                disabled={this.props.disabled}
                id={this.props.id}
                onBlur={this.handleBlur}
                onChange={this.handleChange}
                value={this.state.value}
              >
                <option value="" disabled>{this.props.placeholder || 'Select...'}</option>
                {
                  options.map(option => (
                    <option
                      value={option.id}
                      key={`option-${option.id}`}
                    >
                      {option.label}
                    </option>
                  ))
                }
              </select>
            )
            }
          })()}
          {
            validationtooltip && (
              <div className="invalid-feedback bg-warning">
                <span className="text-white ml-1"><i className="icon icon-common icon-exclamation-triangle" /></span> {this.props.validationtooltip}
              </div>
            )
          }
          <div className="invalid-feedback">
            <i className="icon icon-common icon-times-circle" /> {errorMessage}
          </div>
          <small
            id={`${id}-helpblock`}
            className="form-text text-muted"
          >
            {this.props.description}
            {this.props.example && <span> <strong>Example:</strong> <span className="text-dark">{this.props.example}</span>.</span>}
          </small>
        </div>

      </div>
    );
  }
}


// Mapping
const mapStateToProps = (state, ownProps) => ({
  field: state.prefixRegistrationRequestForm[ownProps.id]
});

const mapDispatchToProps = (dispatch, ownProps) => ({
  setValue: (value) => dispatch(setRegistrationRequestFieldField(ownProps.registrationType, ownProps.id, 'value', value)),
  setValidity: (validity) => dispatch(setRegistrationRequestFieldField(ownProps.registrationType, ownProps.id, 'valid', validity)),
  setErrorMessage: (errorMessage) => dispatch(setRegistrationRequestFieldField(ownProps.registrationType, ownProps.id, 'errorMessage', errorMessage)),
  setLabel: (value) => dispatch(setRegistrationRequestFieldField(ownProps.registrationType, ownProps.id, 'label', value)),
  validationDone: () => dispatch(setRegistrationRequestFieldField(ownProps.registrationType, ownProps.id, 'requestedValidate' , false))
});

export default connect (mapStateToProps, mapDispatchToProps)(RegistrationRequestField);
