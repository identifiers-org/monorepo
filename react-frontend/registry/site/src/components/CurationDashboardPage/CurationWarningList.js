import React, {memo, useEffect, useState} from "react";
import { config } from "../../config/Config";
import CurationWarningListItem from "./CurationWarningListItem"
import Paginator from "../common/Paginator";

const CurationWarningList = () => {
  const [queryResponse, setQueryResponse] = useState(null);

  // Status state
  const [loading, setLoading] = useState(null);
  const [failed, setFailed] = useState(false);

  // Pagination state
  const [pgSize, setPgSize] = useState(5);
  const [pageIdx, setPageIdx] = useState(0);

  // filter state
  const [includeClosed, setIncludeClosed] = useState(false);
  const [selectedTargetType, setSelectedTargetType] = useState(undefined);
  const [selectedWarningType, setSelectedWarningType] = useState(undefined);
  const [recentFirst, setRecentFirst] = useState(false);

  // Warning types list
  const [warningTypes, setWarningTypes] = useState(undefined);

  useEffect(() => {
    const queryParams = new URLSearchParams();
    if (selectedTargetType) queryParams.set("type", selectedTargetType)
    fetch(config.registryApi + "/curationApi/getWarningTypesByTargetType?" + queryParams)
        .then(response => response.json())
        .then(json => setWarningTypes(json.payload))
        .catch(() => setWarningTypes(null))

  }, [selectedTargetType, setWarningTypes]);

  useEffect(() => {
    if (loading === null || loading === false) {
      setLoading(true);

      const queryParams = new URLSearchParams({
        includeClosed, page: pageIdx, size: pgSize,
        sort: recentFirst ? "lastNotification,asc" : "lastNotification,desc"
      });
      if (selectedWarningType) queryParams.set("warningType", selectedWarningType)
      if (selectedTargetType) queryParams.set("targetType", selectedTargetType)

      fetch(config.registryApi + "/curationApi/queryWarnings?" + queryParams)
          .then(response => response.json())
          .then(json => setQueryResponse(json))
          .catch(() => setFailed(true))
          .finally(() => setLoading(false));
    }
  }, [
    includeClosed, pageIdx, pgSize, recentFirst,
    selectedTargetType, selectedWarningType,
    setQueryResponse, setFailed, setLoading
  ]);

  const paginationVals = {
    items: null,
    totalElements: 0,
    totalPages: 0
  };
  if (loading) {
    paginationVals.items = [<div key={0}> Loading... </div>];
    paginationVals.totalElements = 1;
    paginationVals.totalPages = 1;
  } else if (failed) {
    paginationVals.items = [<div key={0}> Failed to get list of curation warnings! </div>];
    paginationVals.totalElements = 1;
    paginationVals.totalPages = 1;
  } else if (queryResponse) {
    paginationVals.items = queryResponse._embedded?.curationWarnings
      .map(curationWarning => //Map to list item components
        <CurationWarningListItem key={curationWarning.id} warning={curationWarning} />
      );
    paginationVals.totalElements = queryResponse.page?.totalElements || 0;
    paginationVals.totalPages = queryResponse.page?.totalPages || 0;
  }

  const handleSelectedTargetTypeChange = (e) => {
    setSelectedWarningType(undefined);
    setSelectedTargetType(e.target.value);
  }
  return (<>
    <form id="curation-warning-filters" className="form-inline mb-2">
      <div className="input-group input-group-sm">
        <div className="input-group-prepend">
          <label className="input-group-text" htmlFor="target-type-selector">Target entity</label>
        </div>
        <select value={selectedTargetType} id="target-type-selector"
                className="custom-select" onChange={handleSelectedTargetTypeChange}>
          <option value="all">All</option>
          <option value="namespace">Namespace</option>
          <option value="resource">Resource</option>
          <option value="institution">Institution</option>
        </select>
      </div>

      <div className="input-group input-group-sm ml-2">
        <div className="input-group-prepend">
          <label className="input-group-text" htmlFor="warning-type-selector">Warning type</label>
        </div>
        <select value={selectedWarningType} id="warning-type-selector" className="custom-select"
                onChange={e => setSelectedWarningType(e.target.value)}>
          <option value="all">All</option>
          { warningTypes && (
            warningTypes.map(type => <option value={type}>{type}</option>)
          )}
        </select>
      </div>

      <div className="input-group input-group-sm ml-2">
        <div className="input-group-prepend">
          <label className="input-group-text" htmlFor="order-selector">Order</label>
        </div>
        <select value={recentFirst} id="order-selector" className="custom-select"
                onChange={e => setRecentFirst(e.target.value === "true")}>
          <option value="false">Oldest first</option>
          <option value="true">Recent first</option>
        </select>
      </div>

      <div className="custom-control custom-switch ml-2">
        <input className="custom-control-input" type="checkbox" id="include-closed-checkbox"  onChange={e => setIncludeClosed(e.target.checked)}/>
        <label className="custom-control-label" htmlFor="include-closed-checkbox">Include non-active</label>
      </div>
    </form>
    <Paginator
        navigate={v => setPageIdx(parseInt(v))}
        number={pageIdx} size={pgSize}
        setSize={e => setPgSize(parseInt(e.target.value))}
        totalElements={paginationVals.totalElements}
        totalPages={paginationVals.totalPages}
    />
    <table className="table table-sm table-striped table-hover table-borderless table-fixed">
      <thead className="thead-light thead-rounded">
        <tr>
          <th className="med">Warning Type</th>
          <th>Target</th>
          <th className="med">Last notification</th>
          <th className="med">Last event</th>
          <th className="bttn-col"></th>
        </tr>
      </thead>
      <tbody>{paginationVals.items}</tbody>
    </table>
  </>)
}


export default CurationWarningList;