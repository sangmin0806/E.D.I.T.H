import { useNavigate } from "react-router-dom";
import { validAccessTokenRequest } from "../api/userApi";
import { useEffect } from "react";

export const useRedirectIfLoggedIn = async () => {
  // const navigate = useNavigate();
  // try {
  //   const result = await validAccessTokenRequest();
  //   useEffect(() => {
  //     const checkLogin = async () => {
  //       if (result) {
  //         alert("잘못된 접근입니다.");
  //         navigate(redirectTo);
  //       }
  //     };
  //     checkLogin();
  //   }, []);
  // } catch {
  //   alert("서버 에러가 발생하였습니다.");
  //   navigate("/");
  // }
};
export const useRedirectIfNotLoggedIn = async (redirectTo: string = "/") => {
  //   const navigate = useNavigate();
  //   try {
  //     const result = await validAccessTokenRequest();
  //     useEffect(() => {
  //       const checkLogin = async () => {
  //         if (result) {
  //           alert("잘못된 접근입니다.");
  //           navigate(redirectTo);
  //         }
  //       };
  //       checkLogin();
  //     }, [navigate, redirectTo]);
  //   } catch {
  //     alert("서버 에러가 발생하였습니다.");
  //     navigate("/");
  //   }
};
