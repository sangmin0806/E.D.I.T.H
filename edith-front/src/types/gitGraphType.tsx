export interface Commit {
  id: string;
  message: string;
  author_name: string;
  author_email: string;
  authored_date: string;
  parent_ids: string[];
}

export interface BranchData {
  sourceBranch: string;
  targetBranch: string;
  mergeCommit: Commit;
  sourceBranchCommits: Commit[];
}
