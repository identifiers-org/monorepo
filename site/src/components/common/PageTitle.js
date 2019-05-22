import React from 'react';


const PageTitle = ({ icon, title, extraTitle, description }) => (
  <>
    <div className="row">
      <div className="col">
        <h1>
          <i className={`icon icon-common ${icon}`} />&nbsp;
          <span className="font-italic">{extraTitle}</span>
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