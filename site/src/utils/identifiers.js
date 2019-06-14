//
// evaluateSearch: evaluates the correctness of a search query. Returns a result ('ok', 'warn', 'fail') and
// a description message.
//
export const evaluateSearch = function (queryParts, namespaceList, enableResourcePrediction) {
  // Case analysis for incorrect identifier strings:
  // 1. Empty prefix.
  if (queryParts.prefix === '') {
    return 'prefix_empty';
  }

  // 2. Non-existant prefix.
  if (namespaceList.filter(namespace => namespace.prefix === queryParts.prefixEffectiveValue).length === 0) {
    return 'prefix_unknown';
  }

  // Below here, we are supposed to have a namespace.
  const currentNamespace = namespaceList.filter(namespace => namespace.prefix === queryParts.prefixEffectiveValue)[0];

  // 3. Empty local id.
  if (queryParts.id === '') {
    return 'id_empty';
  }

  // 4. Non-conforming local id.
  const regex = new RegExp(currentNamespace.pattern);
  const matches = currentNamespace.namespaceEmbeddedInLui ? queryParts.idWithEmbeddedPrefix.match(regex) : queryParts.id.match(regex);

  if (!matches) {
    return 'id_bad';
  }

  // 5. Resource specified when resource prediction is disabled.
  if (queryParts.resource !== '' && !enableResourcePrediction) {
    return 'resource_not_empty';
  }

  // 6. Non-existant resource.
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
  const prefixEffectiveValue = prefix.toLowerCase();
  const id = idSide ? idSide.join(':') : '';
  const idWithEmbeddedPrefix = `${prefix}:${id}`;

  return {prefix, prefixEffectiveValue, id, idWithEmbeddedPrefix, resource};
}


//
// completeQuery: completes a identifier string by concatenating the different parts,
// and optionally adding resource and its trailing / or not.
// Also, if the namespace is special (prefix embedded in LUI), take prefix from pattern instead, because
// they are allowed to have caps.
export const completeQuery = (resource, namespace, id) => {
  const prefix = namespace.namespaceEmbeddedInLui ? namespace.pattern.slice(1).split(':')[0] : namespace.prefix;

  return `${resource ? resource + '/' : ''}${prefix}:${id}`;
};
