import React from 'react';
import { connect } from 'react-redux';

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


  getInstitutionsForRORID = async (rorid) => {
    const { getInstitutionForRORID } = this.props;

    console.log(`must get institutions using ${rorid}.`);

    const institution = await getInstitutionForRORID(rorid);

    console.log('institution', institution);
  };

  handleInputChange = e => {
    const {
      getInstitutionsForRORID,
      props: { onChange },
      state: { debounceValue },
    } = this;
    const value = e.target.value;

    this.setState({value});

    // Debounces value change.
    clearTimeout(debounceValue);
    this.setState({debounceValue: setTimeout(() => { getInstitutionsForRORID(value) }, config.DEBOUNCE_DELAY)});

    // Updates parent.
    onChange(e);
  }


  render() {
    const {
      handleInputChange,
      props: { disabled },
      state: { value }
    } = this;

    return (
      <input
        className="form-control"
        disabled={disabled}
        value={value}
        onChange={handleInputChange}
      />
    );
  }
}

// Redux Mappings.
const mapDispatchToProps = (dispatch) => ({
  getInstitutionForRORID: (rorid) => dispatch(getInstitutionForRORIDFromRegistry(rorid))
});

export default connect(undefined, mapDispatchToProps)(RORIDInput);