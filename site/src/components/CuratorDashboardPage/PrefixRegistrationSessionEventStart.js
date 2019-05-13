import React from 'react';

import { Collapse } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPlus, faMinus } from '@fortawesome/free-solid-svg-icons';

import PrefixRegistrationSessionRequest from './PrefixRegistrationSessionRequest';


class PrefixRegistrationSessionEventStart extends React.Component {
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
    const { data } = this.props;
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
                  <><FontAwesomeIcon icon={faMinus} /> Hide request contents&nbsp;</>
                :
                  <><FontAwesomeIcon icon={faPlus} /> Show request contents&nbsp;</>
              }
            </a>
          </div>
        </div>

        <Collapse isOpen={expanded}>
          <div className="card-body">
            <PrefixRegistrationSessionRequest data={data.prefixRegistrationRequest} />
          </div>
        </Collapse>
      </>
    );
  }
}


export default PrefixRegistrationSessionEventStart;
