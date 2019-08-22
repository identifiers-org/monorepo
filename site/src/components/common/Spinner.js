import React from 'react';


const Spinner = (props) => (
  <div className={props.className || props.noCenter ? '' : 'text-center'}>
    {!props.noText && <h2 className="text-muted font-weight-bold mb-0">Loading</h2>}
    <div className="lds-ellipsis">
      <div></div>
      <div></div>
      <div></div>
      <div></div>
    </div>
  </div>
);


export default Spinner;
