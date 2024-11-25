import { useNavigate } from "react-router-dom";
import logoImg from "../../assets/header/header-logo.png";
import setting_active from "../../assets/header/setting-active.png";
import defaultImg from "../../assets/defaultImg.jpg";
import { useState, useEffect, useRef } from "react";
import { userInfo } from "../../types/userTypes";
import { tempUserInfo } from "../../assets/defaultData";
import { logout } from "../../hooks/useAuth";

function Header({ userImgSrc }: any) {
  const navigate = useNavigate();
  const [imgSrc, setImgSrc] = useState(userImgSrc);
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [activeTab, setActiveTab] = useState("Projects");
  const [userInfo, setUserInfo] = useState<userInfo>(tempUserInfo);

  useEffect(() => {
    const getUserInfo = sessionStorage.getItem("userInfo");

    if (getUserInfo) {
      setUserInfo(JSON.parse(getUserInfo));
    }
  }, []);
  useEffect(() => {
    console.log(userInfo.profileImageUrl);
    setImgSrc(userInfo.profileImageUrl);
  }, [userInfo]);

  // menuRef를 useRef로 선언
  const menuRef = useRef<HTMLDivElement>(null);

  const handleMoveToRepo = () => {
    setActiveTab("Projects");
    navigate("/project");
  };
  const handleToMoveToMyPortfolio = () => {
    setActiveTab("MyPortfolio");
    navigate("/portfolio/my");
  };
  const handleToMoveToSetFaceId = () => {
    setActiveTab("");
    navigate("/register-face");
  };
  const handleError = () => {
    setImgSrc(defaultImg);
  };
  const handleLogout = () => {
    logout();
    sessionStorage.clear();
    navigate("/");
  };
  const toggleMenu = () => setIsMenuOpen(!isMenuOpen);

  // 메뉴 외부 클릭 시 메뉴 닫기
  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
        setIsMenuOpen(false);
      }
    }

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <>
      <nav className="bg-gray-800">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <div className="flex h-16 items-center justify-between">
            <div className="flex items-center">
              <div className="shrink-0">
                <img className="size-8 cursor-pointer" src={logoImg} alt="Your Company" onClick={handleMoveToRepo}/>
              </div>
              <div className="hidden md:block">
                <div className="ml-10 flex items-baseline space-x-4">
                  <a
                    href="#"
                    className={`rounded-md px-3 py-2 text-sm font-medium ${
                      activeTab === "Projects"
                        ? "bg-gray-900 text-white"
                        : "text-gray-300 hover:bg-gray-700 hover:text-white"
                    }`}
                    aria-current={activeTab === "Projects" ? "page" : undefined}
                    onClick={handleMoveToRepo}
                  >
                    Projects
                  </a>
                  <a
                    href="#"
                    className={`rounded-md px-3 py-2 text-sm font-medium ${
                      activeTab === "MyPortfolio"
                        ? "bg-gray-900 text-white"
                        : "text-gray-300 hover:bg-gray-700 hover:text-white"
                    }`}
                    aria-current={
                      activeTab === "MyPortfolio" ? "page" : undefined
                    }
                    onClick={handleToMoveToMyPortfolio}
                  >
                    MyPortfolio
                  </a>
                </div>
              </div>
            </div>

            <div className="hidden md:block">
              <div className="ml-4 flex items-center md:ml-6">
                <div className="relative ml-3" ref={menuRef}>
                  <div>
                    <button
                      onClick={toggleMenu}
                      type="button"
                      className={`relative flex max-w-xs items-center rounded-full bg-gray-800 text-sm focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800 ${
                        isMenuOpen ? "border-2 border-white" : ""
                      }`}
                      id="user-menu-button"
                      aria-expanded={isMenuOpen}
                      aria-haspopup="true"
                    >
                      <span className="absolute -inset-1.5"></span>
                      <span className="sr-only">Open user menu</span>
                      <img
                        className="size-8 rounded-full"
                        src={imgSrc}
                        onError={handleError}
                        alt=""
                      />
                    </button>
                  </div>

                  {/* 드롭다운 메뉴 */}
                  {isMenuOpen && (
                    <div
                      className="absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-md bg-white py-1 shadow-lg ring-1 ring-black/5 focus:outline-none"
                      role="menu"
                      aria-orientation="vertical"
                      aria-labelledby="user-menu-button"
                      tabIndex={-1}
                    >
                      <a
                        href="#"
                        className="block px-4 py-2 text-sm text-gray-700"
                        role="menuitem"
                        tabIndex={-1}
                        id="user-menu-item-2"
                        onClick={() => {
                          handleLogout();
                          setIsMenuOpen(false);
                        }}
                      >
                        로그아웃
                      </a>
                      <a
                        className="block px-4 py-2 text-sm text-gray-700 cursor-pointer"
                        role="menuitem"
                        tabIndex={-1}
                        id="user-menu-item-2"
                        onClick={() => {
                          handleToMoveToSetFaceId();
                          setIsMenuOpen(false);
                        }}
                      >
                        Face Login 등록
                      </a>
                    </div>
                  )}
                </div>
              </div>
            </div>

            <div className="-mr-2 flex md:hidden">
              <button
                type="button"
                className="relative inline-flex items-center justify-center rounded-md bg-gray-800 p-2 text-gray-400 hover:bg-gray-700 hover:text-white focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800"
                aria-controls="mobile-menu"
                aria-expanded="false"
              >
                <span className="absolute -inset-0.5"></span>
                <span className="sr-only">Open main menu</span>
                <svg
                  className="block size-6"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth="1.5"
                  stroke="currentColor"
                  aria-hidden="true"
                  data-slot="icon"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"
                  />
                </svg>
                <svg
                  className="hidden size-6"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth="1.5"
                  stroke="currentColor"
                  aria-hidden="true"
                  data-slot="icon"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M6 18 18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </nav>
    </>
  );
}

export default Header;
