interface IBaseMatcherNote {
  value: string;
  file: string;
  line: string;
}

export interface IMatchNote extends IBaseMatcherNote {
  id: number;
  created_at: string;
  line_number: number;
  git_repository_id: number;
}

export interface IResponseMatchNotes {
  success: true;
  matcherNotes: IMatchNote[];
}

export interface ICreateMatcherNote extends IBaseMatcherNote {
  lineNumber: number;
  gitRepositoryId: number;
}

export interface IEditMatcherNote {
  value: string;
}

export interface IResponseCreateMatcherNote {
  success: boolean;
  matcherNote: IMatchNote;
}
