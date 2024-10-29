import React, { Suspense } from "react";
import { HelmetProvider } from "react-helmet-async";
import { BrowserRouter, Routes, Route } from "react-router-dom";

const MainPage = React.lazy(() => import("./pages/common/MainPage"));
const JoinPage = React.lazy(() => import("./pages/common/JoinPage"));
const FinishJoinPage = React.lazy(
  () => import("./pages/common/FinishJoinPage")
);

const Portfolio = React.lazy(() => import("./pages/repos/Portfolio"));
const RepoDashboard = React.lazy(() => import("./pages/repos/RepoDashboard"));
const RepoEnroll = React.lazy(() => import("./pages/repos/RepoEnroll"));
const RepoList = React.lazy(() => import("./pages/repos/RepoList"));

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
            <Route path="/repository/list" element={<RepoList />} />
            <Route path="/repository/enroll" element={<RepoEnroll />} />
            <Route path="/repository/:repoID" element={<RepoDashboard />} />
            <Route path="/portfolio/:repoID" element={<Portfolio />} />
          </Routes>
        </Suspense>
      </BrowserRouter>
    </HelmetProvider>
  );
}

export default App;
