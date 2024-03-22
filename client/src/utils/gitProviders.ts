import { GitProviderType } from '@/enums/gitProvider';
import type { IGitProvider } from '@/types/gitProvider';

export const getLinkForTargetProvider = (item: IGitProvider): string => {
  if (item.type === GitProviderType.GITHUB) {
    return `https://github.com/${item.target}`;
  } else if (item.type === GitProviderType.GITLAB) {
    return `https://gitlab.com/${item.target}`;
  }

  return '';
};
