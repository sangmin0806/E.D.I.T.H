import create from "zustand";

interface ComponentState {
  showProject: number;
  showDashboard: boolean;
  selectedProjectID: number | undefined;
  dashboardProjectName: string;
  dashboardProjectContents: string;
  toggleComponent: (num: number) => void;
  togglePortfolio: () => void;
  setDashboardProjectName: (projectName: string | null) => void;
  setDashboardProjectContents: (projectContents: string | null) => void;
  setShowComponentOne: () => void;
  setShowDashboardTrue: () => void;
  setSelectedProjectID: (id: number | null) => void;
}

export const useComponentStore = create<ComponentState>((set) => ({
  showProject: 1,
  selectedProjectID: undefined,
  showDashboard: true,
  dashboardProjectName: "",
  dashboardProjectContents: "",
  toggleComponent: (num: number) => set(() => ({ showProject: num })),
  togglePortfolio: () =>
    set((state) => ({ showDashboard: !state.showDashboard })),
  setDashboardProjectName: (projectName: string | null) =>
    set(() => ({ dashboardProjectName: projectName ?? "" })),
  setDashboardProjectContents: (projectContents: string | null) =>
    set(() => ({ dashboardProjectContents: projectContents ?? "" })),
  setShowComponentOne: () => set(() => ({ showProject: 1 })),
  setShowDashboardTrue: () => set(() => ({ showDashboard: true })),
  setSelectedProjectID: (id) => set({ selectedProjectID: id ?? undefined }),
}));
