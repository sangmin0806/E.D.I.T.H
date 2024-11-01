import { useState } from "react";
import defaultImg from "../../assets/defaultImg.jpg";
interface UserProps {
  userGitAccount: string;
}
function UserHeader({ userGitAccount }: UserProps) {
  return (
    <>
      <div className="flex justify-center ml-4 mr-4">
        <div className="w-full flex justify-between items-center ">
          <p className="text-black text-[28px] font-semibold">
            @{userGitAccount} Projects 💻
          </p>
        </div>
      </div>
    </>
  );
}
export default UserHeader;
