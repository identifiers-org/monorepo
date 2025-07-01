import React, {useCallback, useEffect, useState} from "react";
import { config } from "../../../config/Config";
import Spinner from "../../common/Spinner";
import CurationWarningModal from "./CurationWarningModal";

import DataTable from "datatables.net-react";
import 'datatables.net-bs5/css/dataTables.bootstrap5.min.css'
import {renewToken} from "../../../utils/auth";
import DT from 'datatables.net-bs5';


DataTable.use(DT);

const CurationWarningList = () => {
  const [tableRows, setTableRows] = useState([]);
  const [loading, setLoading] = useState(null);
  const [failed, setFailed] = useState(false);
  const [selectedTarget, setSelectedTarget] = useState(null);

  const getLinkToTarget = useCallback((targetInfo, col) => {
    const href = getHrefForTarget(targetInfo);
    return <a className="btn btn-link m-0 p-1 text-decoration-none" href={href} target="_blank">
      <i className="icon icon-common icon-external-link-alt"></i>
    </a>
  }, []);

  const getSelectTargetBttn = useCallback((targetInfo, col) =>
      <button type="button" className="btn btn-primary text-light py-1 px-2" data-bs-toggle="modal" data-bs-target="#targetInfoModal"
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
          r.allDisabled ? "Yes" : "No",
          r.lowAvailabilityResources,
          r.hasCurationValues,
          r.failingInstitutionUrl,
          r.hasPossibleWikidataError,
          r.targetInfo,
    ])
  }, []);

  useEffect(() => {
    const fn = async () => {
      if (loading === null || loading === false) {
        setLoading(true);
        const authToken = await renewToken();
        const init = {headers: {'Authorization': `Bearer ${authToken}`}};

        fetch(config.registryApi + "/curationApi/warningsSummary", init)
            .then(response => response.json())
            .then(json => parseWarningSummaryIntoRows(json.payload))
            .then(rows => setTableRows(rows))
            .catch(() => setFailed(true))
            .finally(() => setLoading(false));
      }
    }
    fn();
  }, []);

  if (loading) return <div> <Spinner/> </div>
  if (failed) return <div className="alert alert-danger"> Failed to download warnings! </div>

  const opts = {
    columnDefs: [
      {searchable: false, targets: [1,2,3,4,5,6,7,8]},
      {sortable: false, targets: [8]},
      {className: 'align-middle', targets: '_all'},
      {className: 'text-center', targets: [1,8]},
      {className: 'text-end', targets: [2,4,6]},
    ],
    order: [[3, 'asc'], [2, 'desc']],
    layout: {
      topStart: {
        info: {
          text: "Warnings _START_ to _END_ out of _TOTAL_"
        },
        paging: {}
      },
      topEnd: {
        pageLength: {
          text: 'Entries per page: _MENU_',
          menu: [5, 20, 100]
        },
        search: {
          placeholder: 'Search here...',
          text: ''
        }
      },
      bottomStart: null,
      bottomEnd: null
    },
    pageLength: 20,
    stateSave: true
  }
  return <div id="curation-warning-list-container">
    <DataTable data={tableRows} slots={{1: getLinkToTarget, 8: getSelectTargetBttn}} options={opts}
                    className="table table-sm table-striped table-bordered">
      <thead>
        <tr>
          <th className="cursor-pointer" colSpan={2} scope="col">Target</th>
          <th className="cursor-pointer" scope="col"
              title="Target usage score based on previous month">
            Access score
          </th>
          <th className="cursor-pointer" scope="col"
              title="Whether all warnings are disabled">
            Disabled
          </th>
          <th className="cursor-pointer" scope="col"
              title="Number of resources marked as low availability">
            Low availability
          </th>
          <th className="cursor-pointer" scope="col"
              title="Whether one or more fields are marked as CURATOR_REVIEW">
            Curation review
          </th>
          <th className="cursor-pointer" scope="col"
              title="Number of institutions with non-responsive URLs">
            Bad institution URL
          </th>
          <th className="cursor-pointer" scope="col"
              title="Whether there is discrepancy between institution information and Wikidata">
            Wikidata discrepancy
          </th>
          <th scope="col"></th>
        </tr>
      </thead>
    </DataTable>
    <CurationWarningModal selectedTarget={selectedTarget}
                          modalId="targetInfoModal"
                          editHref={getHrefForTarget(selectedTarget)} />
  </div>

}

const getHrefForTarget = targetInfo => {
  if (!targetInfo) return null;

  return targetInfo.type === 'Institution' ?
      '/curation?institution=' + targetInfo.identifier :
      '/registry/' + targetInfo.identifier;
}

export default CurationWarningList;