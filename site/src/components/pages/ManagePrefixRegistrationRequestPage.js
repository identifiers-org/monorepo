import React from 'react';
import { connect } from 'react-redux';

import { getPrefixRegistrationSessionFromRegistry } from '../../actions/CuratorDashboardPage/PrefixRegistrationSession';

import PrefixRegistrationSessionEvent from '../CuratorDashboardPage/PrefixRegistrationSessionEvent';
import PrefixRegistrationSessionNewEventBtn from '../CuratorDashboardPage/PrefixRegistrationSessionNewEventBtn';

import PrefixRegistrationSessionAcceptForm from '../CuratorDashboardPage/PrefixRegistrationSessionAcceptForm';
import PrefixRegistrationSessionAmendForm from '../CuratorDashboardPage/PrefixRegistrationSessionAmendForm';
import PrefixRegistrationSessionCommentForm from '../CuratorDashboardPage/PrefixRegistrationSessionCommentForm';
import PrefixRegistrationSessionRejectForm from '../CuratorDashboardPage/PrefixRegistrationSessionRejectForm';

import PrefixRegistrationSessionRequestDetails from '../CuratorDashboardPage/PrefixRegistrationSessionRequest';
import PageTitle from '../common/PageTitle';


class ManagePrefixRegistrationRequestPage extends React.Component {
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

    this.updatePrefixRegistrationSession(id);
  }


  updatePrefixRegistrationSession = (id) => {
    this.props.getPrefixRegistrationSession(id);
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
          icon="icon-check-square"
          title={`Managing request:`}
          extraTitle={prefixRegistrationRequest.name}
        />

        <div className="row">
          <div className="col">
            <div className="card-body">
              <h4><i className="icon icon-common icon-hand-point-up" /> Current request details</h4>
              <PrefixRegistrationSessionRequestDetails
                data={prefixRegistrationRequest}
              />
            </div>
          </div>
        </div>

        <div className="row mt-5 mx-0">
          <PrefixRegistrationSessionNewEventBtn
            caption="Accept"
            color="success"
            handleShow={() => handleFormVisibility('acceptForm')}
            icon="check"
            isCancel={acceptFormVisible}

          />
          <PrefixRegistrationSessionNewEventBtn
            caption="Amend"
            color="warning"
            handleShow={() => handleFormVisibility('amendForm')}
            icon="edit"
            isCancel={amendFormVisible}
          />
          <PrefixRegistrationSessionNewEventBtn
            caption="Comment"
            color="secondary"
            handleShow={() => handleFormVisibility('commentForm')}
            icon="comment"
            isCancel={commentFormVisible}
          />
          <PrefixRegistrationSessionNewEventBtn
            caption="Reject"
            color="danger"
            handleShow={() => handleFormVisibility('rejectForm')}
            icon="times"
            isCancel={rejectFormVisible}
          />
        </div>

        <PrefixRegistrationSessionAcceptForm
          id={id}
          isOpen={acceptFormVisible}
        />

        <PrefixRegistrationSessionAmendForm
          id={id}
          isOpen={amendFormVisible}
          handleShow={() => handleFormVisibility('acceptForm')}
          handleFormSubmit={handleFormSubmit}
        />

        <PrefixRegistrationSessionCommentForm
          id={id}
          isOpen={commentFormVisible}
          handleShow={() => handleFormVisibility('acceptForm')}
          handleFormSubmit={handleFormSubmit}
        />

        <PrefixRegistrationSessionRejectForm
          id={id}
          isOpen={rejectFormVisible}
          handleShow={() => handleFormVisibility('acceptForm')}
          handleFormSubmit={handleFormSubmit}
        />

        <hr className="my-5" />

        <div className="row">
          <div className="col">
            <h4><i className="icon icon-common icon-history" /> Previous events</h4>
            {
              prefixRegistrationSessionEvents.map(prse =>
                <PrefixRegistrationSessionEvent
                  key={`prse-${prse.name}-${prse.created}`}
                  data={prse}
                />
              )
            }
          </div>
        </div>
      </>
      )
    );
  }
}


// Mapping
const mapStateToProps = (state) => ({
  prefixRegistrationSession: state.curatorDashboard.prefixRegistrationSession
});

const mapDispatchToProps = (dispatch) => ({
  getPrefixRegistrationSession: (params) => dispatch(getPrefixRegistrationSessionFromRegistry(params)),
});

export default connect (mapStateToProps, mapDispatchToProps)(ManagePrefixRegistrationRequestPage);
