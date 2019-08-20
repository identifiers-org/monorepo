export function appendSchemaOrg(contents) {
  const script = document.getElementById('schemaorg-metadata') || document.createElement('script');

  script.id = 'schemaorg-metadata';
  script.type = 'application/ld+json';
  script.innerHTML = JSON.stringify(contents, null, 2);

  document.head.appendChild(script);
}
