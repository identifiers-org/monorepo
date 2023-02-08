import React from 'react';


const EBINavItem = (props) => (
  <li className={props.className} onClick={props.handleClickChildFunction}>
    {props.children}
  </li>
)


export default EBINavItem;
