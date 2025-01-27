import React from 'react';
import { connect } from 'react-redux';

// Components.
import { prefixRegistrationRequestComment } from '../../actions/CurationDashboardPage/PrefixRegistrationSession';
import { setRegistrationSessionComment } from '../../actions/CurationDashboardPage/RegistrationSessionComment';

// Utils.
import { swalSuccess, swalError } from '../../utils/swalDialogs';


class PrefixRegistrationSessionCommentForm extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      comment: this.props.prefixRegistrationSessionComment.comment
    }
  }


  static getDerivedStateFromProps(nextProps, prevState) {
    // If comment changed, change state.
    if (nextProps.prefixRegistrationSessionComment.comment !== prevState.comment) {
      return {comment: nextProps.prefixRegistrationSessionComment.comment};
    } else return null;
  }


  handleComment = async () => {
    const {
      id,
      handleFormSubmit,
      prefixRegistrationRequestComment,
      setPrefixRegistrationSessionComment
    } = this.props;
    const { comment } = this.state;

    if (!comment || comment === '') {
      return;
    }

    const response = await prefixRegistrationRequestComment(id, comment);

    if (response.status === 200) {
      await swalSuccess.fire({
        title: 'Prefix Registration Request comment added successfully'
      });

      setPrefixRegistrationSessionComment('', '');
      handleFormSubmit();
    } else {
      await swalError.fire({
        title: 'Error',
        text: 'Could not add comment to Prefix Registration Request'
      });
    }
  }


  handleCommentChange = (e) => {
    const { setPrefixRegistrationSessionComment } = this.props;

    setPrefixRegistrationSessionComment(e.target.value, 'additionalInformation');
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
  prefixRegistrationSessionComment: state.curationDashboard.prefixRegistrationSessionComment
});

const mapDispatchToProps = (dispatch) => ({
  prefixRegistrationRequestComment: (id, reason) => dispatch(prefixRegistrationRequestComment(id, reason)),
  setPrefixRegistrationSessionComment: (comment) => dispatch(setRegistrationSessionComment(comment, undefined, 'prefix'))
});

export default connect (mapStateToProps, mapDispatchToProps)(PrefixRegistrationSessionCommentForm);

