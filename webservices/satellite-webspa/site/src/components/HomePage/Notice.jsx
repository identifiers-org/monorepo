import React, { useEffect, useState, memo } from "react";
import { config } from "../../config/Config";



//// Change these variables to have static content on the notice component   ////////////////////////////
const showStaticNotice = false;
const StaticNoticeTitle = "We need your help!"
const StaticNoticeBody = () => (
    <p className="mb-0 text-justify text-muted">
      Has identifiers.org saved you time or effort?
      Please take 15 minutes to fill in a survey and help EMBL-EBI make the case for why open data
      resources are critical to life science research.
      <a href="https://http.cat/status/200"> Click here to fill in the survey </a>
    </p>
)
///////////////////////////////////////////////////////////////////////



const getLatestUpdateInfo = async () =>
  await fetch("https://docs.identifiers.org/updates/latest.json")
      .then(
          res => res.ok ? res.json() : {},
          reason => {
            console.error(reason);
            return {};
          }
      );

const ignoreIfOldUpdate = (latestUpdateJson) => {
  if (!latestUpdateJson.title) return {};

  const { daysPeriodToShowLatestUpdatePost } = config;
  const updateDate = new Date(latestUpdateJson.date);
  const today = new Date();
  const diffTime = Math.abs(today - updateDate);
  const diffDays = Math.floor(diffTime / (1000.0 * 60.0 * 60.0 * 24.0));

  return diffDays < daysPeriodToShowLatestUpdatePost ? latestUpdateJson : {};
}



const LatestUpdateBody = ({latestUpdate}) => <>
  <p>{latestUpdate.description}</p>
  <small className="d-block">
    <a href={latestUpdate.url} target="_blank">More info</a>
    <span className="px-2">&#124;</span>
    <a href="https://docs.identifiers.org/pages/updates.html" target="_blank">Other news</a>
  </small>
</>


const Notice = memo(() => {
  const [latestUpdate, setLatestUpdate] = useState({});

  useEffect(() => {
    if (!showStaticNotice) {
      getLatestUpdateInfo()
          .then(ignoreIfOldUpdate)
          .then(setLatestUpdate)
    }
  }, [showStaticNotice]);

  if (!showStaticNotice && !latestUpdate.title) {
    return <></>
  } else {
    return (
        <div className="alert alert-info border-0 mt-4 w-100 px-3 py-2">
          <h5 className="text-info-emphasis">
            <i className="icon icon-common icon-bullhorn me-2 fs-2 text-align-center"></i>
            {showStaticNotice ? StaticNoticeTitle : latestUpdate.title}
          </h5>
          {showStaticNotice ? <StaticNoticeBody/> : <LatestUpdateBody latestUpdate={latestUpdate}/>}
        </div>
    );
  }
})
export default Notice;