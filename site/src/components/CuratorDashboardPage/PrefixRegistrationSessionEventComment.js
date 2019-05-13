import React from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faQuoteLeft, faQuoteRight } from '@fortawesome/free-solid-svg-icons';


const PrefixRegistrationSessionEventComment = ({ data }) => (
  <div className="card">
    <div className="card-body">
      <FontAwesomeIcon className="text-muted mr-3" icon={faQuoteLeft} />
      <span className="text-dark font-italic">{data.comment}</span>
      <FontAwesomeIcon className="text-muted ml-3" icon={faQuoteRight} />
      <hr/>
      <span className="text-muted font-weight-bold mr-3">Additional information:</span>
      <span className="text-dark">{data.additionalInformation}</span>
    </div>
  </div>
);


export default PrefixRegistrationSessionEventComment;
