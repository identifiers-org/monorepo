import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';

// Actions.
import { resourceRegistrationRequestAccept } from '../../actions/CurationDashboardPage/ResourceRegistrationSession';
import { setRegistrationSessionAccept } from '../../actions/CurationDashboardPage/RegistrationSessionAccept';

// Utils.
import { swalSuccess, swalError } from '../../utils/swalDialogs';


class ResourceRegistrationSessionAcceptForm extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      acceptanceReason: this.props.resourceRegistrationSessionAccept.acceptanceReason
    }
  }


  static getDerivedStateFromProps(nextProps, prevState) {
    // If acceptanceReason changed, change state.
    if (nextProps.resourceRegistrationSessionAccept.acceptanceReason !== prevState.acceptanceReason) {
      return {acceptanceReason: nextProps.resourceRegistrationSessionAccept.acceptanceReason};
    } else return null;
  }


  handleAccept = async () => {
    const { id, resourceRegistrationRequestAccept, setResourceRegistrationSessionAccept } = this.props;
    const { acceptanceReason } = this.state;

    const response = await resourceRegistrationRequestAccept(id, acceptanceReason || 'No acceptance reason specified.');

    if (response.status === 200) {
      await swalSuccess.fire({
        title: 'Resource Registration Request accepted succesfully'
      });

      setResourceRegistrationSessionAccept('', '');
      history.push('/curation');
    } else {
      await swalError.fire({
        title: 'Error',
        text: 'Could not accept Resource Registration Request'
      });
    }
  }


  handleAcceptanceReasonChange = (e) => {
    const { setResourceRegistrationSessionAccept } = this.props;

    setResourceRegistrationSessionAccept(e.target.value, 'additionalInformation');
  }


  render() {
    const {
      props: {
        isOpen
      },
      handleAccept,
      handleAcceptanceReasonChange
    } = this;

    return isOpen ? (
      <div className="bg-gray pt-3 pb-2 px-4 rounded-all-but-top-left-lg">
        <div className="row">
          <div className="col mb-3">
            <h4><i className="icon icon-common icon-check" /> Accept request</h4>
          </div>
        </div>

        {/* ========================================  FORM FOR REGISTRATION ACCEPT ======================================== */}
        <div className="row no-gutters align-items-center bg-light rounded p-2 mb-1">
          <div className="col">
            <div className="input-group">
              <div className="input-group-prepend">
                <span className="input-group-text">Acceptance reason</span>
              </div>
              <textarea
                className="form-control"
                aria-label="Acceptace reason"
                onChange={handleAcceptanceReasonChange}
              />
            </div>
          </div>
        </div>

        <div className="row">
          <div className="col mt-2">
            <a
              className="btn btn-success btn-block text-white"
              href="#!"
              onClick={handleAccept}
            >
              <i className="icon icon-common icon-check" /> Confirm acception
            </a>
          </div>
        </div>

        {/* ======================================== TABLE FORM FOR REGISTRATION AMEND ======================================== */}
      </div>
    ) : null;
  }
}


// Mapping
const mapStateToProps = (state) => ({
  resourceRegistrationSessionAccept: state.curationDashboard.resourceRegistrationSessionAccept
});

const mapDispatchToProps = (dispatch) => ({
  resourceRegistrationRequestAccept: (id, reason) => dispatch(resourceRegistrationRequestAccept(id, reason)),
  setResourceRegistrationSessionAccept: (acceptanceReason) => dispatch(setRegistrationSessionAccept(acceptanceReason, undefined, 'resource'))
});

export default withRouter( connect (mapStateToProps, mapDispatchToProps)(ResourceRegistrationSessionAcceptForm));

