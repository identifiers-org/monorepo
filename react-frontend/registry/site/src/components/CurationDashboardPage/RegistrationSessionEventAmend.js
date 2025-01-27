import React from 'react';

// Components.
import PrefixRegistrationSessionRequest from './PrefixRegistrationSessionRequest';
import ResourceRegistrationSessionRequest from './ResourceRegistrationSessionRequest';


class RegistrationSessionEventAmend extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      expanded: false
    };
  }

  toggle = () => {
    this.setState(state => ({ expanded: !state.expanded }));
  }


  render() {
    const { data, registrationSessionType } = this.props;
    const { expanded } = this.state;

    return (
      <>
        <div className="row">
          <div className="col mb-3">
            <span className="text-muted font-weight-bold mr-3">Additional information:</span>
            <span className="text-dark">{data.additionalInformation}</span>
          </div>
        </div>
        <div className="row">
          <div className="col col-xs-8 col-sm-6 col-md-5 col-lg-4 col-xl-3">
            <a
              className="btn btn-secondary btn-block"
              href="#!"
              onClick={this.toggle}
            >
              {
                expanded ?
                  <span className="text-white"><i className="icon icon-common icon-minus" /> Hide request contents&nbsp;</span>
                  :
                  <span className="text-white"><i className="icon icon-common icon-plus" /> Show request contents&nbsp;</span>
              }
            </a>
          </div>
        </div>

        {expanded && (
          <div className="card-body">
            {registrationSessionType === 'prefix' && <PrefixRegistrationSessionRequest data={data.prefixRegistrationRequest} />}
            {registrationSessionType === 'resource' && <ResourceRegistrationSessionRequest data={data.resourceRegistrationRequest} />}
          </div>
        )}
      </>
    );
  }
}


export default RegistrationSessionEventAmend;
