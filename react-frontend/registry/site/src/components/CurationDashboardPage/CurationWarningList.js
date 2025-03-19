import React, {useCallback, useEffect, useState} from "react";
import { config } from "../../config/Config";
import Spinner from "../common/Spinner";
import CurationWarningModal from "./CurationWarningModal";

import DataTable from "datatables.net-react";
import DT from 'datatables.net-bs4';
import 'datatables.net-bs4/css/dataTables.bootstrap4.min.css'


DataTable.use(DT);

const CurationWarningList = () => {
  const [tableRows, setTableRows] = useState([]);
  const [loading, setLoading] = useState(null);
  const [failed, setFailed] = useState(false);
  const [selectedTarget, setSelectedTarget] = useState(null);

  const getLinkToTarget = useCallback((targetInfo, col) => {
    const href = getHrefForTarget(targetInfo);
    return <a className="btn btn-link" href={href} target="_blank">
      <i className="icon icon-common icon-external-link-alt"></i>
    </a>
  }, []);

  const getSelectTargetBttn = useCallback((targetInfo, col) =>
      <button type="button" className="btn btn-primary" data-toggle="modal" data-target="#targetInfoModal"
              onClick={() => setSelectedTarget(targetInfo)}>
        <i className="icon icon-common icon-search-plus"></i>
      </button>
    , [])

  const parseWarningSummaryIntoRows = useCallback((summaryPayload) => {
    if (summaryPayload == null || !Array.isArray(summaryPayload.summaryEntries)) return [];

    const usageScores = summaryPayload.namespaceUsage;

    return summaryPayload.summaryEntries.map(r => [
          `${r.targetInfo.label} (${r.targetInfo.identifier})`,
          r.targetInfo,
          usageScores[r.targetInfo.identifier] || 0,
          r.lowAvailabilityResources,
          r.failingInstitutionUrl,
          r.hasCurationValues,
          r.hasPossibleWikidataError,
          r.targetInfo,
    ])
  }, []);

  useEffect(() => {
    if (loading === null || loading === false) {
      setLoading(true);

      fetch(config.registryApi + "/curationApi/warningsSummary")
          .then(response => response.json())
          .then(json => parseWarningSummaryIntoRows(json.payload))
          .then(rows => setTableRows(rows))
          .catch(() => setFailed(true))
          .finally(() => setLoading(false));
    }
  }, []);

  if (loading) return <div> <Spinner/> </div>
  if (failed) return <div className="alert alert-danger"> Failed to download warnings! </div>

  const opts = {
    columnDefs: [
      {searchable: false, targets: [1,2,3,4,5,6,7]},
      {sortable: false, targets: [7]},
      {className: 'text-right', targets: [2,3,4]},
      {className: 'text-center', targets: [1,7]}
    ],
    order: [[3, 'desc'], [2, 'desc']],
    layout: {
      topStart: null,
      topEnd: {
        search: {
          placeholder: 'Search here...',
          text: ''
        }
      }
    },
    pageLength: 5,
    stateSave: true,
    lengthChange: false
  }
  return <>
    <DataTable data={tableRows} slots={{1: getLinkToTarget, 7: getSelectTargetBttn}} options={opts}
                    className="table table-striped table-bordered">
      <thead>
        <tr>
          <th className="cursor-pointer" colSpan={2}>Target</th>
          <th className="cursor-pointer">Access score</th>
          <th className="cursor-pointer">Low availability</th>
          <th className="cursor-pointer">Bad institution URL</th>
          <th className="cursor-pointer">Curation review</th>
          <th className="cursor-pointer">Wikidata discrepancy</th>
          <th className="cursor-pointer"></th>
        </tr>
      </thead>
    </DataTable>
    <CurationWarningModal selectedTarget={selectedTarget}
                          modalId="targetInfoModal"
                          editHref={getHrefForTarget(selectedTarget)} />
  </>

}

const getHrefForTarget = targetInfo => {
  if (!targetInfo) return null;

  return targetInfo.type === 'Institution' ?
      '/curation?institution=' + targetInfo.identifier :
      '/registry/' + targetInfo.identifier;
}

export default CurationWarningList;