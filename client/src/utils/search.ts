export const REG_PATTERN: string = '($0)(?![^<]*>|[^<>]*\\\\)';
export const MATCH_CLASS: string = 'match-found';

function replace(code: string, pattern: string): string {
  return code.replace(
    new RegExp(REG_PATTERN.replace('$0', pattern), 'g'),
    `<span class="${MATCH_CLASS}">${pattern}</span>`
  );
}

export function searchMatch(code: string, pattern: string): string {
  const patternParts: string[] = pattern.split(' ');

  if (patternParts.length !== 1) {
    return code;
  } else {
    return replace(code, patternParts[0]);
  }
}
