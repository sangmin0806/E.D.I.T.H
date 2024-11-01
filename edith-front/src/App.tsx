import React, { Suspense } from "react";
import { HelmetProvider } from "react-helmet-async";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import HeaderLayout from "../src/componets/header/HeaderLayout";

const MainPage = React.lazy(() => import("./pages/common/MainPage"));
const JoinPage = React.lazy(() => import("./pages/common/JoinPage"));
const FinishJoinPage = React.lazy(
  () => import("./pages/common/FinishJoinPage")
);
const RepoPage = React.lazy(() => import("./pages/repos/RepoPage"));
const DashboardPage = React.lazy(() => import("./pages/repos/DashBoard"));
const PortfolioPage = React.lazy(() => import("./pages/repos/PortfolioPage"));
const MyPorfolioListPage = React.lazy(
  () => import("./pages/repos/MyPortfolioPage")
);

function App() {
  return (
    <HelmetProvider>
      <BrowserRouter>
        <Suspense fallback={<div>loading...</div>}>
          <Routes>
            {/* 헤더가 없는 라우트 */}
            <Route path="/" element={<MainPage />} />
            <Route path="/join" element={<JoinPage />} />
            <Route path="/join/finish" element={<FinishJoinPage />} />

            {/* 헤더가 포함된 라우트 */}
            <Route
              path="/repo"
              element={
                <HeaderLayout>
                  <RepoPage />
                </HeaderLayout>
              }
            />
            <Route
              path="/dashboard" //나중에 dashboardID 파라미터로 추가하기
              element={
                <HeaderLayout>
                  <DashboardPage />
                </HeaderLayout>
              }
            />
            <Route
              path="/portfolio" //나중에 portfolioID 파라미터로 추가하기
              element={
                <HeaderLayout>
                  <PortfolioPage />
                </HeaderLayout>
              }
            />
            <Route
              path="/portfolio/my" //나중에 portfolioID 파라미터로 추가하기
              element={
                <HeaderLayout>
                  <MyPorfolioListPage />
                </HeaderLayout>
              }
            />
          </Routes>
        </Suspense>
      </BrowserRouter>
    </HelmetProvider>
  );
}

export default App;
