import React from 'react';
import { connect } from 'react-redux';

// Components.
import Spinner from '../../components/common/Spinner';

// Config.
import { config } from '../../config/Config';
import { getInstitutionForRORIDFromRegistry } from '../../actions/InstitutionList';

// Utils.
import validators from '../../utils/validators';


class RORIDInput extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      debounceValue: undefined,
      isLoading: false,
      institution: undefined,
      valid: undefined
    }
  }


  getInstitutionForRORID = async (rorId) => {
    const { getInstitutionForRORID } = this.props;

    this.setState({isLoading: true});

    const institution = await getInstitutionForRORID(rorId);

    if (institution) {
      this.setState({institution, isLoading: false, valid: true});
    } else {
      this.setState({institution: undefined, isLoading: false, valid: false});
    }

    return institution;
  };


  handleInputChange = e => {
    const {
      getInstitutionForRORID,
      props: { onChange, onInstitutionFound },
      state: { debounceValue },
    } = this;
    const value = e.target.value;

    // Debounces value change.
    clearTimeout(debounceValue);

    this.setState({debounceValue: setTimeout(async () => {
      const valid = validators['rorId'](value).errorMessage === null;
      if (value && valid) {
        const institution = await getInstitutionForRORID(value);
        onInstitutionFound(institution);
      } else {
        this.setState({isLoading: false, valid: false});
      }
    }, config.DEBOUNCE_DELAY)});

    // Updates parent.
    onChange(e);
  }


  render() {
    const {
      handleInputChange,
      props: { disabled, value },
      state: { isLoading, institution, valid }
    } = this;

    return (
      <>
        <input
          className={`form-control ${typeof valid === 'undefined' ? '' : valid ? 'is-valid' : 'is-invalid'}`}
          disabled={disabled}
          value={value}
          onChange={handleInputChange}
          spellCheck={false}
          placeholder={!disabled ? 'Enter a ROR Id in the form https://ror.org/<ror_id>' : ''}
        />
        {typeof valid !== 'undefined' && !valid ? (
          <div className="h-2 mt-1">
            <i className="icon icon-common icon-exclamation-triangle mr-1" />
            Invalid ROR Id.
          </div>
        ) : (
          <small className="form-text text-muted h-2">
            {isLoading ? <Spinner noText noCenter /> : <>The ROR ID of the organization</>}
          </small>
        )}
      </>
    );
  }
}

// Redux Mappings.
const mapDispatchToProps = (dispatch) => ({
  getInstitutionForRORID: (rorId) => dispatch(getInstitutionForRORIDFromRegistry(rorId))
});

export default connect(undefined, mapDispatchToProps)(RORIDInput);
