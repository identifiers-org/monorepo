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

    this.setState({isLoading: true});

    const institution = await getInstitutionForRORID(rorid);
    this.setState({institution, isLoading: false});
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
      state: { institution, isLoading, value }
    } = this;


    const institutionLoadingSpinner = <p>LOADING</p>
    const institutionNotFound = <p>NOTFOUND</p>

    return (
      <>
        <input
          className="form-control"
          disabled={disabled}
          value={value}
          onChange={handleInputChange}
        />
      {/* TODO: USAR MENSAJES DE ERROR DEL INPUT CON BOOTSTRAP */}
      </>
    );
  }
}

// Redux Mappings.
const mapDispatchToProps = (dispatch) => ({
  getInstitutionForRORID: (rorid) => dispatch(getInstitutionForRORIDFromRegistry(rorid))
});

export default connect(undefined, mapDispatchToProps)(RORIDInput);