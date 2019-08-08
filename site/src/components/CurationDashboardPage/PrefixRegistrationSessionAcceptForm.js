import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';

// Actions.
import { prefixRegistrationRequestAccept } from '../../actions/CurationDashboardPage/PrefixRegistrationSession';
import { setRegistrationSessionAccept } from '../../actions/CurationDashboardPage/RegistrationSessionAccept';

// Utils.
import { swalSuccess, swalError } from '../../utils/swalDialogs';


class PrefixRegistrationSessionAcceptForm extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      acceptanceReason: this.props.prefixRegistrationSessionAccept.acceptanceReason
    }
  }


  static getDerivedStateFromProps(nextProps, prevState) {
    // If acceptanceReason changed, change state.
    if (nextProps.prefixRegistrationSessionAccept.acceptanceReason !== prevState.acceptanceReason) {
      return {acceptanceReason: nextProps.prefixRegistrationSessionAccept.acceptanceReason};
    } else return null;
  }


  handleAccept = async () => {
    const { id, history, prefixRegistrationRequestAccept, setPrefixRegistrationSessionAccept } = this.props;
    const { acceptanceReason } = this.state;

    const response = await prefixRegistrationRequestAccept(id, acceptanceReason || 'No acceptance reason specified.');

    if (response.status === 200) {
      await swalSuccess.fire({
        title: 'Prefix Registration Request accepted succesfully'
      });

      setPrefixRegistrationSessionAccept('', '');
      history.push('/curation');
    } else {
      await swalError.fire({
        title: 'Error',
        text: 'Could not accept Prefix Registration Request'
      });
    }
  }


  handleAcceptanceReasonChange = (e) => {
    const { setPrefixRegistrationSessionAccept } = this.props;

    setPrefixRegistrationSessionAccept(e.target.value, 'additionalInformation');
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
  prefixRegistrationSessionAccept: state.curationDashboard.prefixRegistrationSessionAccept
});

const mapDispatchToProps = (dispatch) => ({
  prefixRegistrationRequestAccept: (id, reason) => dispatch(prefixRegistrationRequestAccept(id, reason)),
  setPrefixRegistrationSessionAccept: (acceptanceReason) => dispatch(setRegistrationSessionAccept(acceptanceReason, undefined, 'prefix'))
});

export default withRouter( connect (mapStateToProps, mapDispatchToProps)(PrefixRegistrationSessionAcceptForm));

