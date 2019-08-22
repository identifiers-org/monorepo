import React from 'react';
import { connect } from 'react-redux';

// Components.
import Spinner from '../../components/common/Spinner';

// Config.
import { config } from '../../config/Config';
import { getInstitutionForRORIDFromRegistry } from '../../actions/CurationDashboardPage/CurationInstitutionList';


class RORIDInput extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      value: this.props.value,
      debounceValue: undefined,
      isLoading: false,
      institution: undefined
    }
  }


  getInstitutionForRORID = async (rorid) => {
    const { getInstitutionForRORID } = this.props;

    this.setState({isLoading: true});

    const institution = await getInstitutionForRORID(rorid);
    this.setState({institution, isLoading: false});

    return institution;
  };


  handleInputChange = e => {
    const {
      getInstitutionForRORID,
      props: { onChange, onInstitutionFound },
      state: { debounceValue },
    } = this;
    const value = e.target.value;

    this.setState({value});

    // Debounces value change.
    clearTimeout(debounceValue);
    this.setState({debounceValue: setTimeout(async () => {
      const institution = await getInstitutionForRORID(value);
      onInstitutionFound(institution);
    }, config.DEBOUNCE_DELAY)});

    // Updates parent.
    onChange(e);
  }


  render() {
    const {
      handleInputChange,
      props: { disabled },
      state: { isLoading, value }
    } = this;

    return (
      <>
        <input
          className="form-control"
          disabled={disabled}
          value={value}
          onChange={handleInputChange}
          spellCheck={false}
        />
        {/* TODO: This should be in a small div */}
        {isLoading && <Spinner noText noCenter />}
      </>
    );
  }
}

// Redux Mappings.
const mapDispatchToProps = (dispatch) => ({
  getInstitutionForRORID: (rorid) => dispatch(getInstitutionForRORIDFromRegistry(rorid))
});

export default connect(undefined, mapDispatchToProps)(RORIDInput);
