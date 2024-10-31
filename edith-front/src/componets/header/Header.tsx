import logoImg from "../../assets/header/header-logo.png";
import repo_active from "../../assets/header/project-active.png";
import repo_inactive from "../../assets/header/project-inactive.png";
import setting_active from "../../assets/header/setting-active.png";
import setting_inactive from "../../assets/header/setting-inactive.png";

interface HeaderProps {
  repoPage: boolean;
}
function Header({ repoPage }: HeaderProps) {
  const repoImg = repoPage ? repo_active : repo_inactive;
  const settingImg = repoPage ? setting_inactive : setting_active;

  return (
    <>
      <div className="fixed left-0 top-0 w-24 h-full bg-black flex flex-col items-center">
        <img className="border-b-white w-[64px] h-[64px]" src={logoImg} />
        <img className="w-[64px] h-[64px]" src={repoImg} />
        <img className="w-[64px] h-[64px]" src={settingImg} />
      </div>
    </>
  );
}
export default Header;
