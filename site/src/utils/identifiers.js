//
// evaluateSearch: evaluates the correctness of a search query. Returns a result ('ok', 'warn', 'fail') and
// a description message.
export const evaluateSearch = function (queryParts, namespaceList) {
  // Case analysis for incorrect identifier strings:
  // 1. Empty prefix.
  if (queryParts.prefix === '') {
    return 'prefix_empty';
  }

  // 2. Non-existant prefix.
  if (namespaceList.filter(namespace => namespace.prefix === queryParts.prefix).length === 0) {
    return 'prefix_unknown';
  }

  // Below here, we are supposed to have a namespace.
  const currentNamespace = namespaceList.filter(namespace => namespace.prefix === queryParts.prefix)[0];

  // 3. Empty local id.
  if (queryParts.id === '') {
    return 'id_empty';
  }

  // 4. Non-conforming local id.
  const regex = new RegExp(currentNamespace.pattern);
  const matches = queryParts.id.match(regex);

  if (!matches) {
    return 'id_bad';
  }

  // 5. Non-existant resource.
  if (queryParts.resource !== '') {
    const currentResource = currentNamespace.resources ?
      currentNamespace.resources.filter(resource => resource.providerCode === queryParts.resource)[0]
      :
      undefined;

    if (!currentResource) {
      return 'resource_bad';
    }
  }

  // 99. All ok.
  return 'ok';
}


//
// querySplit: splits a query according to the identifiers.org algorithm.
// For the future: Implement FDA
//
export const querySplit = function (query) {
  const [prefixSide, ...idSide] = query.split(':');
  const prefixParts = prefixSide.split('/');
  let resource = '';

  if (prefixParts.length > 1) {
    resource = prefixParts.shift()
  }

  const prefix = prefixParts.join('/');
  const id = idSide ? idSide.join(':') : '';

  return {prefix, id, resource};
}


//
// completeQuery: completes a identifier string by concatenating the different parts,
// and optionally adding resource and its trailing / or not.
export const completeQuery = (resource, prefix, id) => `${resource ? resource + '/' : ''}${prefix}:${id}`;
