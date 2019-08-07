import React from 'react';
import { connect } from 'react-redux';

// Actions.


// Components.
import PageTitle from '../common/PageTitle';


class ManageResourceRegistrationRequestPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      acceptFormVisible: false,
      amendFormVisible: false,
      commentFormVisible: false,
      rejectFormVisible: false
    };
  }

  componentDidMount() {
    const { id } = this.props.match.params;

    //this.updatePrefixRegistrationSession(id);
  }


  updatePrefixRegistrationSession = (id) => {
    //this.props.getPrefixRegistrationSession(id);
  }


  //
  // Form management functions.
  //
  hideAllForms = () => {
    this.setState({
      acceptFormVisible: false,
      amendFormVisible: false,
      commentFormVisible: false,
      rejectFormVisible: false
    });
  }

  handleFormVisibility = (formName) => {
    const { id } = this.props.match.params;

    this.hideAllForms();
    this.setState({[`${formName}Visible`]: !this.state[`${formName}Visible`]});
    this.updatePrefixRegistrationSession(id);
  }

  handleFormSubmit = () => {
    const { id } = this.props.match.params;

    this.hideAllForms();
    this.updatePrefixRegistrationSession(id);
  }


  render() {
    const {
      handleFormSubmit,
      handleFormVisibility,
      props: {
        prefixRegistrationSession: {
          prefixRegistrationRequest,
          prefixRegistrationSessionEvents
        },
        match: { params: { id } }
      },
      state: {
        acceptFormVisible,
        amendFormVisible,
        commentFormVisible,
        rejectFormVisible
      }
    } = this;


    return (
      !prefixRegistrationRequest ? '' : (
      <>
        <PageTitle
          icon="icon-cube"
          title={`Managing request:`}
          extraTitle="yup" //{prefixRegistrationRequest.name}
        />
      </>
      )
    );
  }
}


// Mapping
const mapStateToProps = (state) => ({
  //prefixRegistrationSession: state.curationDashboard.prefixRegistrationSession
});

const mapDispatchToProps = (dispatch) => ({
  //getResourceRegistrationSession: (params) => dispatch(getPrefixRegistrationSessionFromRegistry(params)),
});

export default connect (mapStateToProps, mapDispatchToProps)(ManageResourceRegistrationRequestPage);
