import React from 'react';


const PageTitle = ({ icon, id, title, extraTitle, description }) => (
  <>
    <div className="row">
      <div className="col">
        <h1 id={id}>
          <i className={`icon icon-common ${icon}`} />&nbsp;{title}
          <span className="font-italic"> {extraTitle}</span>
        </h1>
      </div>
    </div>
    <div className="row">
      <div className="col">
        <p>{description}</p>
      </div>
    </div>
  </>
);


export default PageTitle;