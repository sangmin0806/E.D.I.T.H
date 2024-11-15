import { useNavigate } from "react-router-dom";
import { logoutRequest, validAccessTokenRequest } from "../api/userApi";

export const useRedirectIfLoggedIn = async () => {
  const navigate = useNavigate();
  const userInfo = sessionStorage.getItem("userInfo");
  if (!userInfo) return;
  logout();
  navigate("/");
};
export const useRedirectIfNotLoggedIn = async (redirectTo: string = "/") => {
  const navigate = useNavigate();
  const userInfo = sessionStorage.getItem("userInfo");
  if (userInfo) {
    return;
  }
  alert("잘못된 접근입니다.");
  navigate(redirectTo);
};

export const logout = async () => {
  try {
    const result = await logoutRequest();
    if (!result.success) {
      throw new Error();
    }
    sessionStorage.removeItem("userInfo");
  } catch (error) {
    alert(error);
  }
};
