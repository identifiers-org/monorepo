import React from 'react';


const ResourceItem = ({ resource }) => {
  const providerCodeLabel = resource.providerCode === 'CURATOR_REVIEW' ? 'Empty provider code' : resource.providerCode;

  return (
    <table className="table table-sm table-striped">
      <tbody>
        <tr>
          <td
            rowSpan="5"
            className={`w-20 align-middle ${resource.official ? 'bg-warning' : 'bg-primary text-white'}`}
          >
            <p className="font-weight-bold text-center n-0">{providerCodeLabel}</p>
            <p className="text-center m-0">{resource.mirId}</p>
            <p className="font-weight-bold text-center m-0">{resource.official ? 'Primary' : ''}</p>
          </td>
          <td className="w-15">
            Description</td>
          <td className="resourceitem-table__wide">
            {resource.description}
          </td>
        </tr>
        <tr>
          <td>Access URL</td>
          <td>{resource.urlPattern}</td>
        </tr>
        <tr>
          <td>Institution</td>
          <td>{resource.institution ? resource.institution.name : 'None'}</td>
        </tr>
        <tr>
          <td>Website</td>
          <td>{resource.resourceHomeUrl}</td>
        </tr>
        <tr>
          <td>Location</td>
          <td>{resource.location.countryName}</td>
        </tr>
      </tbody>
    </table>
  );
};


export default ResourceItem;
