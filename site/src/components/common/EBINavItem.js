import React from 'react';


const EBINavItem = (props) => (
  <li className={`nav-item ${props.className}`}>
    {props.children}
  </li>
)


export default EBINavItem;
