import React from 'react';
import { connect } from 'react-redux';

import Paginator from '../common/Paginator';
import ResourceRegistrationSessionItem from './ResourceRegistrationSessionItem';

import { getRegistrationSessionListFromRegistry } from '../../actions/CurationDashboardPage/RegistrationSessionList';
import { setRegistrationSessionListParams } from '../../actions/CurationDashboardPage/RegistrationSessionListParams';

import { equalParams } from '../../utils/equalParams';


class ResourceRegistrationSessionList extends React.Component {
  constructor(props) {
    super(props);
  }


  updateResourceRegistrationSessionList = (params) => {
    this.props.getResourceRegistrationSessionList(params);
  }

  componentDidMount() {
    this.updateResourceRegistrationSessionList(this.props.resourceRegistrationSessionListParams);
  }

  componentDidUpdate(prevProps) {
    if (!equalParams(this.props.resourceRegistrationSessionListParams, prevProps.resourceRegistrationSessionListParams)) {
      const params = {
        page: this.props.resourceRegistrationSessionListParams.number,
        size: this.props.resourceRegistrationSessionListParams.size
      };

      this.updateResourceRegistrationSessionList(params);
    }
  }

  navigate = (number) => {
    const params = {
      ...this.props.resourceRegistrationSessionListParams,
      number
    }

    this.props.setResourceRegistrationSessionListParams(params);
  }


  render() {
    const {
      resourceRegistrationSessionListParams: { number, totalPages },
      resourceRegistrationSessionList
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
              resourceRegistrationSessionList.length === 0 ? (
                <p>No pending resource registration requests</p>
              ) : (
                resourceRegistrationSessionList.map(data => (
                  <ResourceRegistrationSessionItem
                    key={`prr-${data.resourceRegistrationRequest.providerCode}-${data.created}`}
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
  resourceRegistrationSessionList: state.curationDashboard.resourceRegistrationSessionList,
  resourceRegistrationSessionListParams: state.curationDashboard.resourceRegistrationSessionListParams
});

const mapDispatchToProps = (dispatch) => ({
  getResourceRegistrationSessionList: (params) => dispatch(getRegistrationSessionListFromRegistry(params, 'resource')),
  setResourceRegistrationSessionListParams: (params) => dispatch(setRegistrationSessionListParams(params, 'resource'))
});

export default connect (mapStateToProps, mapDispatchToProps)(ResourceRegistrationSessionList);
