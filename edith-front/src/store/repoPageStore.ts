import create from "zustand";

interface ComponentState {
  showComponentList: boolean;
  showDashboard: boolean;
  toggleComponent: () => void;
  togglePortfolio: () => void;
}

export const useComponentStore = create<ComponentState>((set) => ({
  showComponentList: true,
  showDashboard: true,
  toggleComponent: () =>
    set((state) => ({ showComponentList: !state.showComponentList })),
  togglePortfolio: () =>
    set((state) => ({ showDashboard: !state.showDashboard })),
}));
