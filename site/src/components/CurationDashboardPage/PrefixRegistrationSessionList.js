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

  componentDidUpdate(prevProps) {
    if (!equalParams(this.props.prefixRegistrationSessionListParams, prevProps.prefixRegistrationSessionListParams)) {
      const params = {
        page: this.props.prefixRegistrationSessionListParams.number,
        size: this.props.prefixRegistrationSessionListParams.size
      };

      this.updatePrefixRegistrationSessionList(params);
    }
  }

  handleSetSize = e => {
    const size = parseInt(e.target.value);
    const number = 0;

    this.props.setPrefixregistrationSessionListParams({size, number});
    this.updatePrefixRegistrationSessionList({size});
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
      handleSetSize,
      navigate,
      props: {
        prefixRegistrationSessionListParams: { number, size, totalElements, totalPages },
        prefixRegistrationSessionList
      }
    } = this;

    return (
      <>
        <Paginator
          navigate={navigate}
          number={number}
          setSize={handleSetSize}
          size={size}
          totalElements={totalElements}
          totalPages={totalPages}
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
