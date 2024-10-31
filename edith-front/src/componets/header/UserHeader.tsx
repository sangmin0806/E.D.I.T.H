import exampleProfileImg from "../../assets/profile.jpg";
interface UserProps {
  userGitAccount: string;
  userImgSrc: string;
}
function UserHeader({ userGitAccount, userImgSrc }: UserProps) {
  return (
    <>
      <div className="flex justify-center ml-4 mr-4">
        <div className="w-full flex justify-between items-center ">
          <p className="text-black text-[28px] font-semibold">
            @{userGitAccount} Projects 💻
          </p>
          <img src={exampleProfileImg} className="w-12 h-12 rounded-full" />
        </div>
      </div>
    </>
  );
}
export default UserHeader;
