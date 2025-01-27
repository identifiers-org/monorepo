import React from 'react';
import { Link } from 'react-router-dom';

// Components.
import RoleConditional from '../common/RoleConditional';


const NamespaceItem = (namespace) => {
  const isSmallScreen = window.matchMedia("(max-width: 768px)").matches;

  return (
    <>
      <tr>
        <td className="narrow align-middle">
          <Link to={`/registry/${namespace.prefix}`} className="clear-link text-primary">
            {namespace.name}
          </Link>
        </td>
        <td className="med align-middle text-center">
          <span
            className="text-warning font-weight-bold border border-secondary badge badge-dark"
          >
            {namespace.prefix}
          </span>
        </td>
        {!isSmallScreen && (
          <td>
            <div className="d-flex justify-content-between">
              <p className="small m-0">{namespace.description}</p>
              <RoleConditional requiredRoles={['editNamespace']}>
                <Link to={`/registry/${namespace.prefix}?editNamespace=true`}>
                  <button
                    className="btn btn-sm btn-success ml-3 edit-button"
                    >
                    <i className="icon icon-common icon-edit mr-1" />Edit namespace
                  </button>
                </Link>
              </RoleConditional>
            </div>
          </td>
        )}
      </tr>
      {isSmallScreen && (
        <>
          <tr></tr>
          <tr>
            <td colSpan="2">
              <p className="small text-justify mx-3">{namespace.description}</p>
            </td>
          </tr>
        </>
      )}
    </>
  );
};


export default NamespaceItem;
