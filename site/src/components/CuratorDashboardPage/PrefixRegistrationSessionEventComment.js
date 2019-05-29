import React from 'react';


const PrefixRegistrationSessionEventComment = ({ data }) => (
  <div className="card">
    <div className="card-body">
      <i className="icon icon-common icon-quote-left text-muted mr-3" />
      <span className="text-dark font-italic">{data.comment}</span>
      <i className="icon icon-common icon-quote-right text-muted ml-3" />
      <hr/>
      <span className="text-muted font-weight-bold mr-3">Additional information:</span>
      <span className="text-dark">{data.additionalInformation}</span>
    </div>
  </div>
);


export default PrefixRegistrationSessionEventComment;
