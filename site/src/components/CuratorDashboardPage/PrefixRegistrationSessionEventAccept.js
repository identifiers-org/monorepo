import React from 'react';


const PrefixRegistrationSessionEventAccept = ({ data }) => (
  <div className="card">
    <div className="card-body">
      <span className="text-success font-weight-bold mr-3">Acceptance reason:</span>
      <span className="text-dark font-italic">{data.acceptanceReason}</span>
      <hr/>
      <span className="font-weight-bold mr-3">Additional information:</span>
      <span className="text-dark">{data.additionalInformation}</span>
    </div>
  </div>
);


export default PrefixRegistrationSessionEventAccept;
