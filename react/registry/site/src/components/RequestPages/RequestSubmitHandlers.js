import {config} from "../../config/Config";
import {swalError, swalSuccess} from "../../utils/swalDialogs";

export const submitResourceRequest = (values) => {
  const requestBody = {
    apiVersion: "1.0",
    payload: {
      ...values,
      requester: {
        name: values.requesterName,
        email: values.requesterEmail
      }
    }
  };

  const fetch_options = {
    method: 'POST',
    headers: {'content-type': 'application/json'},
    body: JSON.stringify(requestBody)
  };

  // Make request and update the store.
  const requestUrl = `${config.registryApi}/${config.resourceRequestEndpoint}`;
  return fetch(requestUrl, fetch_options)
}

const submitPrefixRequest = (values) => {
  const requestBody = {
    apiVersion: "1.0",
    payload: {
      ...values,
      supportingReferences: values.supportingReferences.split(/\/r?\/n/),
      requester: {
        name: values.requesterName,
        email: values.requesterEmail
      }
    }
  };

  const fetch_options = {
    method: 'POST',
    headers: {'content-type': 'application/json'},
    body: JSON.stringify(requestBody)
  };

  // Make request and update the store.
  const requestUrl = `${config.registryApi}/${config.prefixRequestEndpoint}`;
  return fetch(requestUrl, fetch_options);
}

export const onPrefixSubmit = async (values) => {
  await submitPrefixRequest(values)
      .then(response =>
          response.json().then(json => {
            if (response.ok)
              swalSuccess.fire({
                icon: 'success',
                title: 'Prefix registration request sent',
                text: 'Thank you. We will contact you shortly with more information about your request'
              })
            else
              swalError.fire({
                icon: 'error',
                title: 'Something went wrong when submitting request',
                text: json.errorMessage
              })
          })
      ).catch(err => {
        console.error(err)
        swalError.fire({
          icon: 'error',
          title: 'Something went wrong when submitting request',
          text: err.message
        })
      });
}

export const onResourceSubmit = async (values) =>
    submitResourceRequest(values)
        .then(response =>
            response.json().then(json => {
              if (response.ok)
                swalSuccess.fire({
                  icon: 'success',
                  title: 'Resource registration request sent',
                  text: 'Thank you. We will contact you shortly with more information about your request'
                })
              else
                swalError.fire({
                  icon: 'error',
                  title: 'Something went wrong when submitting request',
                  text: json.errorMessage
                })
            })
        ).catch(err => {
      console.error(err)
      swalError.fire({
        icon: 'error',
        title: 'Something went wrong when submitting request',
        text: err.message
      })
    });