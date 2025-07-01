import React, {memo} from "react";
import openIndividualWarningsImg from 'url:../../../assets/individual_warnings.png'
import individualWarningDetailsImg from 'url:../../../assets/warning_details.png'

export default memo(() => <div className="modal-content">
  <h2 className="modal-header">
    <span>
      <i className="icon icon-common icon-info me-2"></i>
      Instructions for handling curation warnings
    </span>
    <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
  </h2>

  <div className="modal-body">
    <div className="container-fluid">

      {/*Warnings and how to solve*/}
      <div className="card mt-3">
        <div className="card-header">
        <button type="button" data-bs-toggle="collapse" data-bs-target="#warnings-and-table-descrition" title="Click to toogle"
                  className="h4 my-0 py-0 collapsed" aria-expanded="false" aria-controls="warnings-and-table-descrition">
            <span className="collapse-symbol" />
            Warnings and how to solve them
          </button>
        </div>
        <div className="card-body collapse" id="warnings-and-table-descrition">
          <p>
            <strong>Curation warnings indicate possible issues with values on the identifiers.org registry</strong>.
            Their objective is to flag entries that likely need to be updated.
            Each type of warning indicates a different issue, for example, the "low availability" warning
            indicates that the link checker has measured low availability for URLs under a specific resource.
          </p>

          <p>
            <strong>The summary table aggregates different warnings by the entry that caused the warning to be raised</strong>.
            We label these as the <em>targets</em> of warnings.
            This can be a namespace or an institution.
            Curators must check the summary table, identify entries that need fixing, and act as needed by updating
            entries to solve warnings.
            Each row of the table provides a link <i className="icon icon-common icon-external-link-alt" /> to the
            target of the warning.
            For example, changing a URL pattern or homepage URL when it has been changed by the date provider.
          </p>

          <p>
            When applicable, <strong>a curator may deprecate a namespace or resource to disable verification of an entry</strong>.
            Deprecating a namespace disables validation on all it's resources,
            while deprecating a resource disables verification only on it.
          </p>
        </div>
      </div>

      {/*Descriptions of columns*/}
      <div className="card mt-3">
        <div className="card-header">
          <button type="button" data-bs-toggle="collapse" data-bs-target="#types-of-warnings-description" title="Click to toogle"
                  className="h4 my-0 py-0 collapsed" aria-expanded="false" aria-controls="types-of-warnings-description">
            <span className="collapse-symbol" />
            Description of columns
          </button>
        </div>
        <div className="card-body collapse" id="types-of-warnings-description">
          <p>
            Find below the descriptions of values of each column. Beware that rows can indicate warnings
            for namespaces or institutions and not all columns are applicable to all rows.
            For example, low availability does not apply to institutions since
            the link checker does not track its availability.
          </p>

          <p>
            The type of entry a column applies to is displayed by <span className="badge bg-light text-dark">badges</span>.
          </p>

          <div className="row border-top border-info py-2">
            <div className="col-12 col-sm-12 col-md-3 col-lg-2 fw-bold"> Target </div>
            <div className="col-12 col-sm-12 col-md-9 col-lg-10">
              This indicates which type of entry that row groups warnings by. It's either one of:
              <ul>
                <li>Institution name and its database ID or</li>
                <li>Name of namespace that the rows refer to and it's prefix</li>
              </ul>
            </div>
          </div>

          <div className="row border-top border-info py-2">
            <div className="col-12 col-sm-12 col-md-3 col-lg-2 fw-bold">
              Access score
              <span className="badge bg-light rounded-pill text-dark ms-1">Namespaces</span>
            </div>
            <div className="col-12 col-sm-12 col-md-9 col-lg-10">
              A numerical score that indicates how much usage a namespace has.
              The aim of this column is to allow the prioritization of warnings
              based on how used a namespace is.
              The higher the score, the more usage it has.
              This is currently implemented based on the number of visits on the matomo tracker.
            </div>
          </div>

          <div className="row border-top border-info py-2">
            <div className="col-12 col-sm-12 col-md-3 col-lg-2 fw-bold">
              Low availability
              <span className="badge bg-light rounded-pill text-dark ms-1">Namespaces</span>
            </div>
            <div className="col-12 col-sm-12 col-md-9 col-lg-10">
              This number indicates the number of resources under the namespace that are
              marked as low availability by the link checker service.
              This directly relates to URI resolution and takes priority over other warnings.
              Please beware that even after correcting URLs, it takes time for the the link checker to update
              its availability metric as measurements only are discarded after 36h.
              If a resource has gone completely offline, the curator can deprecate it
              or its namespace to disable this warning.
            </div>
          </div>

          <div className="row border-top border-info py-2">
            <div className="col-12 col-sm-12 col-md-3 col-lg-2 fw-bold">
              Curation review
              <span className="badge bg-light rounded-pill text-dark ms-1">Both</span>
            </div>
            <div className="col-12 col-sm-12 col-md-9 col-lg-10">
              Some registry entries have the value "CURATOR_REVIEW" set as to mark the entry to be curated.
              This indicates whether a namespace or institution has this value set in any of its attributes.
              Updating the entries in the registry to not have this will solve the warning.
            </div>
          </div>

          <div className="row border-top border-info py-2">
            <div className="col-12 col-sm-12 col-md-3 col-lg-2 fw-bold">
              Bad institution URL
              <span className="badge bg-light rounded-pill text-dark ms-1">Institutions</span>
            </div>
            <div className="col-12 col-sm-12 col-md-9 col-lg-10">
              Since the link checker does not measure availability for institution URLs, a one shot request is made
              by the verifier to check them.
              Errors detected this way are listed in this column.
            </div>
          </div>

          <div className="row border-top border-bottom border-info py-2">
            <div className="col-12 col-sm-12 col-md-3 col-lg-2 fw-bold">
              Wikidata discrepancy
              <span className="badge bg-light rounded-pill text-dark ms-1">Institutions</span>
            </div>
            <div className="col-12 col-sm-12 col-md-9 col-lg-10">
              Using a best-effort algorithm, the validator tries to match institutions to wikidata entries.
              Then use these entries to validate institution entries on the registry.
              Since the algorithm does not guarantee matches, false positives are possible .
            </div>
          </div>
        </div>
      </div>

      {/*Sorting*/}
      <div className="card mt-3">
        <div className="card-header">
          <button type="button" data-bs-toggle="collapse" data-bs-target="#sorting-description" title="Click to toogle"
                  className="h4 my-0 py-0 collapsed" aria-expanded="false" aria-controls="sorting-description">
            <span className="collapse-symbol" />
            Sorting the summary table
          </button>
        </div>
        <div id='sorting-description' className="card-body collapse">
          <p>
            The column headers can be used to sort rows.
            The indicator to the right of the column header indicates the current sorting.
            <strong> By pressing shift, the table can be sorted by multiple columns in the order they are clicked</strong>.
          </p>

          <p>
            For example, if you click on the first column header, you sort rows by the values on the first column.
            Then by holding the shift key and pressing the second column header,
            it will keep the sort on the first column and further sort on the second column.
          </p>

          <p>
            <strong>Sorting and pagination is maintained across sessions</strong>.
            Clear the browser to reset settings.
            The default settings is to sort by number of low availability and usage score.
          </p>
        </div>
      </div>

      {/*individual warnings*/}
      <div className="card mt-3">
        <div className="card-header">
          <button type="button" data-bs-toggle="collapse" data-bs-target="#individual-warnings-description" title="Click to toogle"
                  className="h4 my-0 py-0 collapsed" aria-expanded="false" aria-controls="individual-warnings-description">
            <span className="collapse-symbol" />
            Inspecting individual curation warnings under row
          </button>
        </div>
        <div id='individual-warnings-description' className="card-body collapse">
          <p>
            <strong>One row of the summary table can indicate multiple warning</strong>.
            Clicking on the link <i className="icon icon-common icon-external-link-alt" /> of that row takes you
            to the namespace or institution target of these warnings.
            To see all warnings and their details, click on the right-most column of that row that contains
            the button with the <i className="icon icon-common icon-search-plus" /> icon.
            This will load the individual warnings grouped under that row.
          </p>

          <div className="text-center">
            <img src={openIndividualWarningsImg}
                 alt="Button to show individual warnings"
                 className="img-fluid border border-dark mt-1 mb-5" />
          </div>

          <p>
            <strong>On the warning list that opens, each item lists information on a different warning</strong>.
            The curator can click on it to expand it and see more details.
            Each warning type will list different information when expanded.
            For low availability resources, it will list the URLs and the availability of that resource.
            For Wikidata discrepancies, it will list the values that are different and the expected values.
            The link <i className="icon icon-common icon-external-link-alt" /> at the top of this view
            also takes you to the target of these warnings.
          </p>

          <div className="text-center">
            <img src={individualWarningDetailsImg}
                 alt="Individual warnings under row"
                 className="img-fluid mt-1 mb-5 mx-auto" />
          </div>

          <p>
            If a warning does not have extra information associated with it, the history of that warning will show.
            This is the case for the "curator review" warnings.
            <strong> The history lists the most recent events where the warning has been opened and closed</strong>.
            For warnings with extra information, this history can be seen by clicking the "See history" button after it is expanded.
          </p>
        </div>
      </div>

      {/*Warning updates*/}
      <div className="card mt-3">
        <div className="card-header">
          <button type="button" data-bs-toggle="collapse" data-bs-target="#warning-update-descrition" title="Click to toogle"
                  className="h4 my-0 py-0 collapsed" aria-expanded="false" aria-controls="warning-update-descrition">
            <span className="collapse-symbol" />
            How warnings are updated
          </button>
        </div>
        <div className="card-body collapse" id="warning-update-descrition">
          <p>
            <strong>Warnings are updated periodically by the registry verifier job</strong>.
            This job runs weekly and checks all entries for possible issues.
            After running, it sends notifications to the registry for warnings that are active.
          </p>

          <p>
            When a active warning is not notified by the job, it is marked as solved.
            A history of when jobs are opened and solved is kept for each warning by the registry.
            This history can be seen when inspecting a row of the warning summary table.
          </p>

          <p>
            Please note that the availability metric of the link checker takes 36h to update.
            So a resource may be marked as low availability from one run of the validator job.
          </p>
        </div>
      </div>

      {/*Disabled warnings*/}
      <div className="card mt-3">
        <div className="card-header">
          <button type="button" data-bs-toggle="collapse" data-bs-target="#disabled-warning-descrition" title="Click to toogle"
                  className="h4 my-0 py-0 collapsed" aria-expanded="false" aria-controls="disabled-warning-descrition">
            <span className="collapse-symbol" />
            Disabled warnings
          </button>
        </div>
        <div className="card-body collapse" id="disabled-warning-descrition">
          <p>
            When inspecting a line of the warning summary table, <strong>you can disable specific warnings</strong>.
            This is intended as way to ignore warnings due to reasons such as them being false positives.
          </p>

          <p>
            Disabled warnings still show on the summary table and upon notification they stay as disabled.
            Please note that a disabled warning will still be marked as solved if not notified by a run of the verifier job.
            So it could happen that an intermittent warning to be marked as disabled,
            then be detected as solved by the verifier, and then notified again later by the verifier job.
          </p>

          <p>
            When all warnings are marked as disabled, the summary table line is considered disabled, allowing sorting
            the table to show other warnings first.
            <strong> To enable a warning</strong>, simply click on the "<em>enable</em>" button when inspecting a line in the warning table.
          </p>
        </div>
      </div>

    </div>
  </div>
</div>, () => true)