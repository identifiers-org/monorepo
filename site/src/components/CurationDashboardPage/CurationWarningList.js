import React, { memo, useState } from "react";
import { config } from "../../config/Config";
import CurationWarningListItem from "./CurationWarningListItem"
import Paginator from "../common/Paginator";

const CurationWarningList = () => {
  const [warningList, setWarningList] = useState([]);
  const [loading, setLoading] = useState(null);
  const [failed, setFailed] = useState(false);
  const [pgSize, setPgSize] = useState(5);
  const [pageIdx, setPageIdx] = useState(0);

  if (loading === null) {
    setLoading(true);
    fetch(config.registryApi + "/registryInsightApi/getResourcesWithLowAvailability")
      .then(response => response.json())
      .then(json => setWarningList(Object.entries(json)))
      .catch(() => setFailed(true))
      .finally(() => setLoading(false));
  }

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
  } else if (warningList) {
    const first = pageIdx * pgSize;
    const last = (pageIdx+1) * pgSize;
    paginationVals.items =  warningList
      .sort((a,b) => a[1]-b[1])//Sort by availability
      .slice(first,last) //Pagination slice
      .map(([resourceId, availability]) => //Map to list item components
        <CurationWarningListItem key={resourceId} resourceId={resourceId} availability={availability} />
      );
    paginationVals.totalElements = warningList.length;
    paginationVals.totalPages = Math.ceil(warningList.length / pgSize);
  }

  return (<>
    <Paginator
      navigate={v => setPageIdx(parseInt(v))}
      number={pageIdx}
      setSize={e => setPgSize(parseInt(e.target.value))}
      size={pgSize}
      totalElements={paginationVals.totalElements}
      totalPages={paginationVals.totalPages}
    />
    {paginationVals.items}
  </>)
}


export default memo(CurationWarningList, () => true);