export type LoginInfo = {
  email: string;
  pw: string;
};

export type JoinInfo = {
  email: string;
  pw: string;
  accessToken: string;
  gitLab: boolean; //false면 github
};
