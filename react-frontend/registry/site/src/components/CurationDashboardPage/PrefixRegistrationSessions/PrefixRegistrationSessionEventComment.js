import React from 'react';


const PrefixRegistrationSessionEventComment = ({ data }) => (
  <div className="card">
    <div className="card-body">
      <i className="icon icon-common icon-quote-left text-muted me-3" />
      <span className="text-dark font-italic">{data.comment}</span>
      <i className="icon icon-common icon-quote-right text-muted ms-3" />
      <hr/>
      <span className="text-muted fw-bold me-3">Additional information:</span>
      <span className="text-dark">{data.additionalInformation}</span>
    </div>
  </div>
);


export default PrefixRegistrationSessionEventComment;
