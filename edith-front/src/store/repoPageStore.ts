import create from "zustand";

interface ComponentState {
  showComponentList: boolean;
  toggleComponent: () => void;
}

export const useComponentStore = create<ComponentState>((set) => ({
  showComponentList: true,
  toggleComponent: () =>
    set((state) => ({ showComponentList: !state.showComponentList })),
}));
