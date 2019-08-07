import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';

// Components.
import { resourceRegistrationRequestComment } from '../../actions/CurationDashboardPage/ResourceRegistrationSession';
import { setRegistrationSessionComment } from '../../actions/CurationDashboardPage/RegistrationSessionComment';

// Utils.
import { swalSuccess, swalError } from '../../utils/swalDialogs';


class ResourceRegistrationSessionCommentForm extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      comment: this.props.resourceRegistrationSessionComment.comment
    }
  }


  static getDerivedStateFromProps(nextProps, prevState) {
    // If comment changed, change state.
    if (nextProps.resourceRegistrationSessionComment.comment !== prevState.comment) {
      return {comment: nextProps.resourceRegistrationSessionComment.comment};
    } else return null;
  }


  handleComment = async () => {
    const {
      id,
      handleFormSubmit,
      resourceRegistrationRequestComment,
      setResourceRegistrationSessionComment
    } = this.props;
    const { comment } = this.state;

    if (!comment || comment === '') {
      return;
    }

    const response = await resourceRegistrationRequestComment(id, comment);

    if (response.status === 200) {
      await swalSuccess.fire({
        title: 'Resource Registration Request comment added successfully'
      });

      setResourceRegistrationSessionComment('', '');
      handleFormSubmit();
    } else {
      await swalError.fire({
        title: 'Error',
        text: 'Could not add comment to Resource Registration Request'
      });
    }
  }


  handleCommentChange = (e) => {
    const { setResourceRegistrationSessionComment } = this.props;

    setResourceRegistrationSessionComment(e.target.value, 'additionalInformation');
  }


  render() {
    const {
      props: { isOpen },
      state: { comment },
      handleComment,
      handleCommentChange
    } = this;

    return isOpen ? (
      <div className="bg-gray pt-3 pb-2 px-4">
        <div className="row">
          <div className="col mb-3">
            <h4><i className="icon icon-common icon-check" /> Add comment to request</h4>
          </div>
        </div>

        {/* ========================================  FORM FOR REGISTRATION COMMENT ======================================== */}
        <div className="row no-gutters align-items-center bg-light rounded p-2 mb-1">
          <div className="col">
            <div className="input-group">
              <div className="input-group-prepend">
                <span className="input-group-text">Comment</span>
              </div>
              <textarea
                className="form-control"
                aria-label="Comment reason"
                onChange={handleCommentChange}
                value={comment}
              />
            </div>
          </div>
        </div>

        <div className="row">
          <div className="col mt-2">
            <a
              className="btn btn-secondary btn-block text-white"
              href="#!"
              onClick={handleComment}
            >
              <i className="icon icon-common icon-plus" /> Add comment
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
  resourceRegistrationSessionComment: state.curationDashboard.resourceRegistrationSessionComment
});

const mapDispatchToProps = (dispatch) => ({
  resourceRegistrationRequestComment: (id, reason) => dispatch(resourceRegistrationRequestComment(id, reason)),
  setResourceRegistrationSessionComment: (comment) => dispatch(setRegistrationSessionComment(comment, undefined, 'resource'))
});

export default withRouter( connect (mapStateToProps, mapDispatchToProps)(ResourceRegistrationSessionCommentForm));

