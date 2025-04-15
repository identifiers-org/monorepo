import React from 'react';


const RegistrationSessionEventReject = ({ data }) => (
  <div className="card">
    <div className="card-body">
      <span className="text-danger fw-bold me-3">Rejection reason:</span>
      <span className="text-dark font-italic">{data.rejectionReason}</span>
      <hr/>
      <span className="text-muted fw-bold me-3">Additional information:</span>
      <span className="text-dark">{data.additionalInformation}</span>
    </div>
  </div>
);


export default RegistrationSessionEventReject;
