export function lineSlice(str: string, exceedsLength: number): string {
  if (str.length > exceedsLength) {
    return `${str.slice(0, Math.floor(exceedsLength / 2))}...${str.slice(
      -Math.floor(exceedsLength / 2)
    )}`;
  }

  return str;
}
