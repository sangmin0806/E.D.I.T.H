// components/HeaderLayout.js
import React from "react";
import Header from "./Header"; // Header 컴포넌트를 가져옵니다.

const HeaderLayout = ({ children }: any) => {
  const userImgSrc =
    "https://image.utoimage.com/preview/cp872722/2022/12/202212008462_500.jpg"; //임시
  return (
    <>
      <Header userImgSrc={userImgSrc} />
      <main>{children}</main>
    </>
  );
};

export default HeaderLayout;
