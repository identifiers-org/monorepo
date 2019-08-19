export function appendSchemaOrg(contents) {
  const script = document.createElement('script');

  script.type = 'application/ld+json';
  script.innerHTML = JSON.stringify(contents, null, 2);

  document.head.appendChild(script);
}
