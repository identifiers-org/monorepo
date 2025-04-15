import React from 'react';


const Spinner = (props) => (
  <div className={`spinner ${props.className || ''} ${props.compact ? 'compact' : ''} ${props.noCenter ? '' : 'text-center'}`}>
    {!props.noText && <h2 className="text-muted fw-bold mb-0">Loading</h2>}
    <div className="lds-ellipsis">
      <div></div>
      <div></div>
      <div></div>
      <div></div>
    </div>
  </div>
);


export default Spinner;
