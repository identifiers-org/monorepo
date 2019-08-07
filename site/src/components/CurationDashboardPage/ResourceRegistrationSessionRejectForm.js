import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';

import { resourceRegistrationRequestReject } from '../../actions/CurationDashboardPage/ResourceRegistrationSession';
import { setRegistrationSessionReject } from '../../actions/CurationDashboardPage/RegistrationSessionReject';

import { swalSuccess, swalError } from '../../utils/swalDialogs';

class ResourceRegistrationSessionRejectForm extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      rejectionReason: this.props.resourceRegistrationSessionReject.rejectionReason
    }
  }


  static getDerivedStateFromProps(nextProps, prevState) {
    // If rejectionReason changed, change state.
    if (nextProps.resourceRegistrationSessionReject.rejectionReason !== prevState.rejectionReason) {
      return {rejectionReason: nextProps.resourceRegistrationSessionReject.rejectionReason};
    } else return null;
  }


  handleReject = async () => {
    const { id, history, resourceRegistrationRequestReject, setResourceRegistrationSessionReject } = this.props;
    const { rejectionReason } = this.state;

    const response = await resourceRegistrationRequestReject(id, rejectionReason || 'No rejection reason specified.');

    if (response.status === 200) {
      await swalSuccess.fire({
        title: 'Resource Registration Request rejected succesfully'
      });

      setResourceRegistrationSessionReject('', '');
      history.push('/curation');
    } else {
      await swalError.fire({
        title: 'Error',
        text: 'Could not reject Resource Registration Request'
      });
    }
  }


  handleRejectionReasonChange = (e) => {
    const { setResourceRegistrationSessionReject } = this.props;

    setResourceRegistrationSessionReject(e.target.value, 'additionalInformation');
  }


  render() {
    const {
      props: {
        isOpen
      },
      handleReject,
      handleRejectionReasonChange
    } = this;

    return isOpen ? (
      <div className="bg-gray pt-3 pb-2 px-4 rounded-all-but-top-right-lg">
        <div className="row">
          <div className="col mb-3">
            <h4><i className="icon icon-common icon-check" /> Reject request</h4>
          </div>
        </div>

        {/* ======================================== FORM FOR REGISTRATION REJECT ======================================== */}
        <div className="row no-gutters align-items-center bg-light rounded p-2 mb-1">
          <div className="col">
            <div className="input-group">
              <div className="input-group-prepend">
                <span className="input-group-text">Rejection reason</span>
              </div>
              <textarea
                className="form-control"
                aria-label="Rejectace reason"
                onChange={handleRejectionReasonChange}
              />
            </div>
          </div>
        </div>

        <div className="row">
          <div className="col mt-2">
            <a
              className="btn btn-danger btn-block text-white"
              href="#!"
              onClick={handleReject}
            >
              <i className="icon icon-common icon-times" /> Confirm reject
            </a>
          </div>
        </div>

        {/* ======================================== FORM FOR REGISTRATION REJECT ======================================== */}

      </div>
    ) : null;
  }
}


// Mapping
const mapStateToProps = (state) => ({
  resourceRegistrationSessionReject: state.curationDashboard.resourceRegistrationSessionReject
});

const mapDispatchToProps = (dispatch) => ({
  resourceRegistrationRequestReject: (id, reason) => dispatch(resourceRegistrationRequestReject(id, reason)),
  setResourceRegistrationSessionReject: (rejectionReason) => dispatch(setRegistrationSessionReject(rejectionReason, undefined, 'resource'))
});

export default withRouter( connect (mapStateToProps, mapDispatchToProps)(ResourceRegistrationSessionRejectForm));

