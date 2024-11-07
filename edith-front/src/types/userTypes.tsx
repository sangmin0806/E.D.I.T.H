export type LoginInfo = {
  email: string;
  password: string;
};

export type userInfo = {
  userId: number;
  email: string;
  username: string;
  name: string;
  profileImageUrl: string;
};

export type JoinInfo = {
  email: string;
  password: string;
  vcsAccessToken: string;
  vcs: boolean;
};
