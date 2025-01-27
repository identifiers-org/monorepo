import React from 'react';


const RegistrationSessionEventReject = ({ data }) => (
  <div className="card">
    <div className="card-body">
      <span className="text-danger font-weight-bold mr-3">Rejection reason:</span>
      <span className="text-dark font-italic">{data.rejectionReason}</span>
      <hr/>
      <span className="text-muted font-weight-bold mr-3">Additional information:</span>
      <span className="text-dark">{data.additionalInformation}</span>
    </div>
  </div>
);


export default RegistrationSessionEventReject;
