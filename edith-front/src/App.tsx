import React, { Suspense } from "react";
import { HelmetProvider } from "react-helmet-async";
import { BrowserRouter, Routes, Route } from "react-router-dom";

const MainPage = React.lazy(() => import("./pages/common/MainPage"));
const JoinPage = React.lazy(() => import("./pages/common/JoinPage"));
const FinishJoinPage = React.lazy(
  () => import("./pages/common/FinishJoinPage")
);
const RepoPage = React.lazy(() => import("./pages/repos/RepoPage"));
const RepoDetailPage = React.lazy(() => import("./pages/repos/RepoDetailPage"));

// const Portfolio = React.lazy(() => import("./pages/repos/Portfolio"));
// const RepoDashboard = React.lazy(
//   () => import("./componets/repos/RepoDashboard")
// );
// const RepoEnroll = React.lazy(() => import("./componets/repos/RepoEnroll"));
// const RepoList = React.lazy(() => import("./componets/repos/RepoList"));

function App() {
  return (
    <HelmetProvider>
      <BrowserRouter>
        {/* 로딩중 */}
        <Suspense fallback={<div>loading</div>}>
          <Routes>
            {/* Common Domain */}
            <Route path="/" element={<MainPage />} />
            <Route path="/join" element={<JoinPage />} />
            <Route path="/join/finish" element={<FinishJoinPage />} />
            {/* Repo Domain */}

            <Route path="/repo" element={<RepoPage />} />
            <Route path="/repo/detail" element={<RepoDetailPage />} />
            {/* <Route path="/repo" element={<RepoEnroll />} /> */}
            {/* <Route path="/repo/list" element={<RepoList />} />
            <Route path="/repo/enroll" element={<RepoEnroll />} />
            <Route path="/repo/dashboard/:repoID" element={<RepoDashboard />} />
            <Route path="/repo/portfolio/:repoID" element={<Portfolio />} /> */}
          </Routes>
        </Suspense>
      </BrowserRouter>
    </HelmetProvider>
  );
}

export default App;
