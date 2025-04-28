import React from 'react';


const RegistrationSessionEventAccept = ({ data }) => (
  <div className="card">
    <div className="card-body">
      <span className="text-success fw-bold me-3">Acceptance reason:</span>
      <span className="text-dark font-italic">{data.acceptanceReason}</span>
      <hr/>
      <span className="fw-bold me-3">Additional information:</span>
      <span className="text-dark">{data.additionalInformation}</span>
    </div>
  </div>
);


export default RegistrationSessionEventAccept;
