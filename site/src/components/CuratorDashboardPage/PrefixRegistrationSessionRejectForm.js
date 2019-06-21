import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';

import { Collapse } from 'reactstrap';

import { prefixRegistrationRequestReject } from '../../actions/PrefixRegistrationSession';
import { setPrefixRegistrationSessionReject } from '../../actions/PrefixRegistrationSessionReject';

import { swalSuccess, swalError } from '../../utils/swalDialogs';

class PrefixRegistrationSessionRejectForm extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      rejectionReason: this.props.prefixRegistrationSessionReject.rejectionReason
    }
  }


  static getDerivedStateFromProps(nextProps, prevState) {
    // If rejectionReason changed, change state.
    if (nextProps.prefixRegistrationSessionReject.rejectionReason !== prevState.rejectionReason) {
      return {rejectionReason: nextProps.prefixRegistrationSessionReject.rejectionReason};
    } else return null;
  }


  handleReject = async () => {
    const { id, history, prefixRegistrationRequestReject, setPrefixRegistrationSessionReject } = this.props;
    const { rejectionReason } = this.state;

    const response = await prefixRegistrationRequestReject(id, rejectionReason || 'No rejection reason specified.');

    if (response.status === 200) {
      await swalSuccess.fire({
        title: 'Prefix Registration Request rejected succesfully'
      });

      setPrefixRegistrationSessionReject('', '');
      history.push('/curator');
    } else {
      await swalError.fire({
        title: 'Error',
        text: 'Could not reject Prefix Registration Request'
      });
    }
  }


  handleRejectionReasonChange = (e) => {
    const { setPrefixRegistrationSessionReject } = this.props;

    setPrefixRegistrationSessionReject(e.target.value, 'additionalInformation');
  }


  render() {
    const {
      props: {
        isOpen
      },
      handleReject,
      handleRejectionReasonChange
    } = this;

    return (
      <Collapse className="bg-gray pt-3 pb-2 px-4 rounded-all-but-top-right-lg" isOpen={isOpen}>
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

      </Collapse>
    );
  }
}


// Mapping
const mapStateToProps = (state) => ({
  prefixRegistrationSessionReject: state.curatorDashboard.prefixRegistrationSessionReject
});

const mapDispatchToProps = (dispatch) => ({
  prefixRegistrationRequestReject: (id, reason) => dispatch(prefixRegistrationRequestReject(id, reason)),
  setPrefixRegistrationSessionReject: (rejectionReason) => dispatch(setPrefixRegistrationSessionReject(rejectionReason))
});

export default withRouter( connect (mapStateToProps, mapDispatchToProps)(PrefixRegistrationSessionRejectForm));

