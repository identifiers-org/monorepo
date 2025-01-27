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
