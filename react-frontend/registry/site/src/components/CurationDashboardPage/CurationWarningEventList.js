import React, {useEffect, useState} from "react";
import Spinner from "../common/Spinner";
import dateTimeFormat from "../../utils/dateTimeFormat";

export default ({ eventsUrl }) => {
    const [loading, setLoading] = useState(null);
    const [failed, setFailed] = useState(false);
    const [eventList, setEventList] = useState([])

    useEffect(() => {
        if (loading === null) {
            setLoading(true);
            fetch(eventsUrl)
                .then(response => response.json())
                .then(json => setEventList(json._embedded?.curationWarningEvents))
                .catch(() => setFailed(true))
                .finally(() => setLoading(false));
        }
    }, [
        setLoading, setEventList, setFailed
    ]);

    if (loading) {
        return <div> <Spinner compact noText noCenter /> </div>;
    }
    if (failed) {
        return <div> Failed get list of events for this warning! </div>;
    }

    return eventList?.slice(0,5).map((warningEvent, idx) => {
        const date = new Date(warningEvent.created);
        return (
            <div key={'curation-event-' + idx} className="card mb-2">
                <div className="card-header p-1">
                    Marked as <span className="font-weight-bold">{warningEvent.type}</span> by {warningEvent.actor} on {dateTimeFormat.format(date)}
                </div>
                { warningEvent.comment &&
                    <div className="card-body">
                        <p className="card-text"> {warningEvent.comment} </p>
                    </div>
                }
            </div>
        )
    })
}