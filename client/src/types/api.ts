export interface IError {
  type: string;
  message: string;
}

export interface IResponseInstallError extends IError {
  type: string;
  message: string;
}

export interface IResponseError extends IError {
  type: string;
  message: string;
}
