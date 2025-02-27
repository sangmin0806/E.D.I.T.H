export type FormValues = {
  id: number;
  name: string;
  content: string;
  branches: string[];
};

export type branchItem = {
  id: number;
  url: string;
};

export type ProjectItem = {
  id: number;
  url: string;
  name: string;
  token: string;
  branches: Branch[];
};

export type Branch = {
  id: number;
  name: string;
};

export type Contributor = {
  name: string;
  avatarUrl: string;
};

export type ProjectListItem = {
  id: number;
  url: string | null;
  name: string;
  token: string;
  content: string;
  branches: Branch[];
  updatedAt: string;
  contributors: Contributor[];
};

export type commitStat = {
  todayCommitsCount: number;
  totalMergeRequestsCount: number;
  todayMergeRequestsCount: number;
};

export type myCommitStat = {
  totalProjectsCount: number;
  todayCommitsCount: number;
  todayMergeRequestsCount: number;
};

export type projectDashboard = {
  recentCommitMessage: string;
  recentCodeReview: string;
  advice: string;
  fixLogs: string[];
  techStack: string[];
};
