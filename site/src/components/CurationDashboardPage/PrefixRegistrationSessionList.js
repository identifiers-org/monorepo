import React from 'react';
import { connect } from 'react-redux';

import Paginator from '../common/Paginator';
import PrefixRegistrationSessionItem from './PrefixRegistrationSessionItem';

import { getRegistrationSessionListFromRegistry } from '../../actions/CurationDashboardPage/RegistrationSessionList';
import { setRegistrationSessionListParams } from '../../actions/CurationDashboardPage/RegistrationSessionListParams';

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
  prefixRegistrationSessionList: state.curationDashboard.prefixRegistrationSessionList,
  prefixRegistrationSessionListParams: state.curationDashboard.prefixRegistrationSessionListParams
});

const mapDispatchToProps = (dispatch) => ({
  getPrefixRegistrationSessionList: (params) => dispatch(getRegistrationSessionListFromRegistry(params, 'prefix')),
  setPrefixregistrationSessionListParams: (params) => dispatch(setRegistrationSessionListParams(params, 'prefix'))
});

export default connect (mapStateToProps, mapDispatchToProps)(PrefixRegistrationSessionList);
