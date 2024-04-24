export interface ITree {
  path: string;
  filename: string;
  isFile: boolean;
  isDirectory: boolean;
  children: ITree[];
}
