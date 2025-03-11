import React, {useCallback, useEffect, useState} from "react";
import { config } from "../../config/Config";
import DataTable from "datatables.net-react";
import DT from 'datatables.net-bs4';
import Spinner from "../common/Spinner";
DataTable.use(DT);

const CurationWarningList = () => {
  const [tableRows, setTableRows] = useState([]);
  const [loading, setLoading] = useState(null);
  const [failed, setFailed] = useState(false);

  const getLinkToTarget = useCallback((targetInfo, col) => {
    const href = targetInfo.type === 'Institution' ?
        '/curation?institution=' + targetInfo.identifier :
        '/registry/' + targetInfo.identifier;
    return <a className="btn btn-link" href={href} target="_blank">
      <i className="icon icon-common icon-external-link-alt"></i>
    </a>
  }, []);

  const parseWarningSummaryIntoRows = useCallback((rows) => {
    if (!Array.isArray(rows)) return [];

    return rows.map(r => [
          `${r.targetInfo.label} (${r.targetInfo.identifier})`,
          r.targetInfo,
          r.accessNumber,
          r.lowAvailabilityResources,
          r.failingInstitutionUrl,
          r.hasCurationValues,
          r.hasPossibleWikidataError
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
  if (failed) return <div> Failed to download warnings! </div>

  const opts = {
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
  return <DataTable data={tableRows} slots={{1: getLinkToTarget}} options={opts}
                    className="table table-striped table-bordered">
    <thead>
      <tr>
        <th colSpan={2}>Target</th>
        <th>Access score</th>
        <th>Low availability</th>
        <th>Failing institution URL</th>
        <th>Curation review</th>
        <th>Wikidata discrepancy</th>
      </tr>
    </thead>
  </DataTable>
}


export default CurationWarningList;