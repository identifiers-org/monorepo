import React from 'react';
import moment from 'moment';

import { Link } from 'react-router-dom';

import { Collapse } from 'reactstrap';

import PrefixRegistrationSessionRequest from './PrefixRegistrationSessionRequest';


class PrefixRegistrationSessionItem extends React.Component {
  constructor(props) {
    super(props)

    this.state = {
      expanded: false
    };
  }

  toggle = () => {
    this.setState(state => ({ expanded: !state.expanded }));
  }


  render() {
    const { id, prefixRegistrationRequest } = this.props.request;
    const { expanded } = this.state;

    return (
      <div>
        <div className="card mb-3">
          <div className="card-header">
            <div className="row justify-content-between">
              <div className="col col-md-10 col-lg-9 col-xl-10">
                <a
                  className="clear-link"
                  href="#!"
                  onClick={this.toggle}
                >
                  <p className="m-0">
                    <strong>Requested Prefix: </strong>
                    {prefixRegistrationRequest.requestedPrefix}
                  </p>
                  <p className="m-0 pl-2 text-small text-muted">
                    <small>
                      <strong>Date of original request: </strong>
                      {moment(prefixRegistrationRequest.created).format('llll')}
                    </small>
                  </p>
                </a>
              </div>
              <div className="col col-md-2 col-lg-3 col-xl-2 pt-2">
                <Link
                  className="clear-link btn btn-warning btn-block"
                  to={`/curator/${id}`}
                >
                  <i className="icon icon-common icon-ellipsis-h" /> Manage
                </Link>
              </div>
            </div>
          </div>

          <Collapse isOpen={expanded}>
            <div className="card-body">
              <PrefixRegistrationSessionRequest data={prefixRegistrationRequest} />
            </div>
          </Collapse>
        </div>
      </div>
    );
  }
}


export default PrefixRegistrationSessionItem;
