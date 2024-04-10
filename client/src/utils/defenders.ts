export function instanceIResponseError(object: any): boolean {
  return 'type' in object && 'message' in object;
}
