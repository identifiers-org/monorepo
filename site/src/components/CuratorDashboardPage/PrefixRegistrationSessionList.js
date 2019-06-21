import React from 'react';
import { connect } from 'react-redux';

import Paginator from '../common/Paginator';
import PrefixRegistrationSessionItem from './PrefixRegistrationSessionItem';

import { getPrefixRegistrationSessionListFromRegistry } from '../../actions/PrefixRegistrationSessionList';
import { setPrefixRegistrationSessionListParams} from '../../actions/PrefixRegistrationSessionListParams';

import { equalParams } from '../../utils/equalParams';


class PrefixRegistrationSessionList extends React.Component {
  constructor(props) {
    super(props);
  }


  updatePrefixRegistrationSessionList = (params) => {
    this.props.getPrefixRegistrationSessionList(params);
  }

  componentDidMount() {
    this.updatePrefixRegistrationSessionList(this.props.prefixRegistrationSessionListParams);
  }

  // Updates list if params have changed.
  componentWillReceiveProps = (newProps) => {
    if (!equalParams(this.props.prefixRegistrationSessionListParams, newProps.prefixRegistrationSessionListParams)) {
      const params = {
        page: newProps.prefixRegistrationSessionListParams.number,
        size: newProps.prefixRegistrationSessionListParams.size
      };

      this.updatePrefixRegistrationSessionList(params);
    }
  }

  navigate = (number) => {
    const params = {
      ...this.props.prefixRegistrationSessionListParams,
      number
    }

    this.props.setPrefixregistrationSessionListParams(params);
  }


  render() {
    const {
      prefixRegistrationSessionListParams: { number, totalPages },
      prefixRegistrationSessionList
    } = this.props;
    const { navigate } = this;

    return (
      <>
        <Paginator
          number={number}
          totalPages={totalPages}
          navigate={navigate}
        />
        <div className="row justify-content-md-center mt-2">
          <div className="col">
            {
              prefixRegistrationSessionList.length === 0 ? (
                <p>No pending prefix registration requests</p>
              ) : (
                prefixRegistrationSessionList.map(data => (
                  <PrefixRegistrationSessionItem
                    key={`prr-${data.prefixRegistrationRequest.requestedPrefix}-${data.created}`}
                    request={data}
                  />
                ))
              )
            }
          </div>
        </div>
      </>
    );
  }
}

// Mapping
const mapStateToProps = (state) => ({
  prefixRegistrationSessionList: state.curatorDashboard.prefixRegistrationSessionList,
  prefixRegistrationSessionListParams: state.curatorDashboard.prefixRegistrationSessionListParams
});

const mapDispatchToProps = (dispatch) => ({
  getPrefixRegistrationSessionList: (params) => dispatch(getPrefixRegistrationSessionListFromRegistry(params)),
  setPrefixregistrationSessionListParams: (params) => dispatch(setPrefixRegistrationSessionListParams(params))
});

export default connect (mapStateToProps, mapDispatchToProps)(PrefixRegistrationSessionList);
