import React from 'react';


const Spinner = () => (
  <div className="text-center">
    <h2 className="text-muted font-weight-bold mb-0">Loading</h2>
    <div className="lds-ellipsis">
      <div></div>
      <div></div>
      <div></div>
      <div></div>
    </div>
  </div>
);


export default Spinner;
