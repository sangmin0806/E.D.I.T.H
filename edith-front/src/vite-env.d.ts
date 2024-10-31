interface ImportMeta {
  readonly env: {
    VITE_NOW_BASEURL: string; // Environment variable for base URL
    VITE_API_LOCAL_URL: string; // Local API URL
    VITE_API_DEPLOYED_URL: string; // Deployed API URL
  };
}
