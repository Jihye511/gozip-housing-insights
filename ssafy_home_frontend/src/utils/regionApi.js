export async function fetchRegionList(pattern) {
  const url = "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes";
  const params = `regcode_pattern=${pattern}&is_ignore_zero=true`;
  const response = await fetch(`${url}?${params}`);
  return response.json();
}
