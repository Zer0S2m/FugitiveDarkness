import { type IMatcherFoundGroupByGrepGitRepository } from '@/types/gitRepository';

export const REG_PATTERN: string = '($0)(?![^<]*>|[^<>]*\\\\)';

export const MATCH_CLASS: string = 'match-found';

function replace(
  code: string,
  pattern: string,
  groups: IMatcherFoundGroupByGrepGitRepository[]
): string {
  let index: number = 0;
  return code.replace(
    new RegExp(REG_PATTERN.replace('$0', pattern), 'g'),
    (substring: string, args: any[]): string => {
      if (groups[index]) {
        const resultHtmlCode: string = `<span class="${MATCH_CLASS}">${groups[index].group}</span>`;
        index++;
        return resultHtmlCode;
      } else {
        // TODO: Crutch - the lines of code can be combined and there will be only one group
        index = 0;
        const resultHtmlCode: string = `<span class="${MATCH_CLASS}">${groups[index].group}</span>`;
        index++;
        return resultHtmlCode;
      }
    }
  );
}

export function searchMatch(
  code: string,
  pattern: string,
  groups: IMatcherFoundGroupByGrepGitRepository[]
): string {
  const patternParts: string[] = pattern.split(' ');

  if (patternParts.length !== 1) {
    return code;
  } else {
    return replace(code, patternParts[0], groups);
  }
}
