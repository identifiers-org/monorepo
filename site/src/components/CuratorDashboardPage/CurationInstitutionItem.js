import React from 'react';
import moment from 'moment';

import { Link } from 'react-router-dom';

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
    const {
      props: { institution: { id, name, homeUrl, description } },
      state: { expanded }
     } = this;

    return (
      <div>
        <div className="card mb-1">
          <div className="card-header py-1 pr-1">
            <div className="row justify-content-between">
              <div className="col col-md-10 col-lg-9 col-xl-10">
                <a
                  className="clear-link"
                  href="#!"
                  onClick={this.toggle}
                >
                  <p className="m-0">
                    <strong><i className="icon icon-common icon-sitemap mr-2" /></strong>{name}
                  </p>
                </a>
              </div>
              <div className="col col-md-2 col-lg-3 col-xl-2 text-right">
                <button
                  className="clear-link btn btn-warning btn-sm m-0 py-0 px-2"
                >
                  <i className="icon icon-common icon-ellipsis-h" /> Edit
                </button>
              </div>
            </div>
          </div>

          {expanded && (
            <div className="card-body">
              <p>Home URL: {homeUrl}</p>
              <p>Description: {description}</p>
            </div>
          )}
        </div>
      </div>
    );
  }
}


export default PrefixRegistrationSessionItem;
