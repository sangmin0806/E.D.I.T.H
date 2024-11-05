import create from "zustand";

interface ComponentState {
  showProject: number;
  showDashboard: boolean;
  selectedProjectID: string | undefined;
  toggleComponent: (num: number) => void;
  togglePortfolio: () => void;
  setShowComponentOne: () => void;
  setShowDashboardTrue: () => void;
  setSelectedProjectID: (id: string | null) => void;
}

export const useComponentStore = create<ComponentState>((set) => ({
  showProject: 1,
  selectedProjectID: undefined,
  showDashboard: true,
  toggleComponent: (num: number) => set(() => ({ showProject: num })),
  togglePortfolio: () =>
    set((state) => ({ showDashboard: !state.showDashboard })),
  setShowComponentOne: () => set(() => ({ showProject: 1 })),
  setShowDashboardTrue: () => set(() => ({ showDashboard: true })),
  setSelectedProjectID: (id) => set({ selectedProjectID: id ?? undefined }),
}));
