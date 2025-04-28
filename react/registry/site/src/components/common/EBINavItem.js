import React from 'react';


const EBINavItem = (props) => {
  return (
    <li className={props.className} onClick={props.handleClickChildFunction}>
      {props.children}
    </li>
  );
}


export default EBINavItem;
