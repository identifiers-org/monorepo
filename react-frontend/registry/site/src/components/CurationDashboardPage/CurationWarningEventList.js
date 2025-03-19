import React, {useCallback, useEffect, useState} from "react";
import Spinner from "../common/Spinner";
import dateTimeFormat from "../../utils/dateTimeFormat";

export default ({ href, preload }) => {
    const [loading, setLoading] = useState(null);
    const [failed, setFailed] = useState(false);
    const [expanded, setExpanded] = useState(Boolean(preload))
    const [eventList, setEventList] = useState([])

    const ensureEventsPopulated = useCallback((loading, href) => {
        if (loading === null) {
            setLoading(true);
            fetch(href)
                .then(response => response.json())
                .then(json => setEventList(json._embedded?.curationWarningEvents))
                .catch(() => setFailed(true))
                .finally(() => setLoading(false));
        }
    }, [
        setLoading, setEventList, setFailed
    ]);

    if (preload && loading === null) {
        ensureEventsPopulated(loading, href);
    }

    if (loading) {
        return <div> <Spinner compact noText noCenter /> </div>;
    }
    if (failed) {
        return <div className="alert-danger alert"> Failed get list of events for this warning! </div>;
    }

    return <div>
        { !preload &&
            <button type="button" className="btn btn-link text-decoration-none text-reset p-0 px-1 mt-2 mb-0"
                    onClick={() => {
                        ensureEventsPopulated(loading, href);
                        setExpanded(!expanded)
                    }}>
                { expanded ? "Hide history" : "See history" }
            </button>
        }
        { expanded &&
            <table className="table table-striped">
                <tbody>
                    { eventList.map( (e, idx) =>
                        <tr key={idx}>
                            <td>{e.type}</td>
                            <td>{dateTimeFormat.format(new Date(e.created))}</td>
                        </tr>
                    )}
                </tbody>
            </table>
        }
    </div>
}