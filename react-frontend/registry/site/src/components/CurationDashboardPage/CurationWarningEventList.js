import React, {useEffect, useState} from "react";
import Spinner from "../common/Spinner";
import dateTimeFormat from "../../utils/dateTimeFormat";
import {renewToken} from "../../utils/auth";

export default ({ href, preload }) => {
    const [loading, setLoading] = useState(null);
    const [failed, setFailed] = useState(false);
    const [expanded, setExpanded] = useState(Boolean(preload))
    const [eventList, setEventList] = useState([])

    useEffect(() => {
        if ((preload || expanded) && loading === null) {
            const fn = async () => {
                setLoading(true);

                const authToken = await renewToken();
                const init = {headers: {'Authorization': `Bearer ${authToken}`}};
                fetch(href, init)
                    .then(response => response.json())
                    .then(json => setEventList(json._embedded?.curationWarningEvents))
                    .catch(() => setFailed(true))
                    .finally(() => setLoading(false));
            }
            fn();
        }
    }, [
        expanded, preload, setLoading, setEventList, setFailed
    ]);

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