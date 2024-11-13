export type PortfolioRequestInfo = {
  name: string;
  content: string;
  endDate: string;
  portfolio: string;
};

export type PortfolioListItem = {
  name: string;
  content: string;
  lastModified: string;
  projectId: number;
};

export type PortfolioInfo = {
  name: string;
  projectId: number;
  content: string;
  startDate: string;
  endDate: string;
  portfolio: string;
};
