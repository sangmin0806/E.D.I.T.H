import React, { Suspense } from "react";
import { HelmetProvider } from "react-helmet-async";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import HeaderLayout from "../src/componets/header/HeaderLayout";

const MainPage = React.lazy(() => import("./pages/common/MainPage"));
const JoinPage = React.lazy(() => import("./pages/common/JoinPage"));
const FinishJoinPage = React.lazy(
  () => import("./pages/common/FinishJoinPage")
);
const FaceRegistration = React.lazy(
  () => import("./pages/common/FaceRegistration")
);
const FaceLogin = React.lazy(
  () => import("./pages/common/FaceLogin")
);
const RepoPage = React.lazy(() => import("./pages/repos/RepoPage"));
const DashboardPage = React.lazy(() => import("./pages/repos/DashBoard"));
const PortfolioPage = React.lazy(() => import("./pages/repos/PortfolioPage"));
const MyPorfolioListPage = React.lazy(
  () => import("./pages/repos/MyPortfolioPage")
);
const NotFoundErrorPage = React.lazy(
  () => import("./pages/common/NotFoundErrorPage")
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
            <Route path="/register-face" element={<FaceRegistration />} />
            <Route path="/face-login" element={<FaceLogin />} />
            {/* 헤더가 포함된 라우트 */}
            <Route
              path="/project"
              element={
                <HeaderLayout>
                  <RepoPage />
                </HeaderLayout>
              }
            />
            <Route
              path="/dashboard/:projectID" //나중에 dashboardID 파라미터로 추가하기
              element={
                <HeaderLayout>
                  <DashboardPage />
                </HeaderLayout>
              }
            />
            <Route
              path="/portfolio/:projectID" //나중에 portfolioID 파라미터로 추가하기
              element={
                <HeaderLayout>
                  <PortfolioPage />
                </HeaderLayout>
              }
            />
            <Route
              path="/portfolio/my"
              element={
                <HeaderLayout>
                  <MyPorfolioListPage />
                </HeaderLayout>
              }
            />
            <Route
              path="/portfolio/my/:projectID" //나중에 portfolioID 파라미터로 추가하기
              element={
                <HeaderLayout>
                  <PortfolioPage />
                </HeaderLayout>
              }
            />
            {/* 지정되지 않은 URL에 대한 NotFoundErrorPage */}
            <Route path="*" element={<NotFoundErrorPage />} />
          </Routes>
        </Suspense>
      </BrowserRouter>
    </HelmetProvider>
  );
}

export default App;
