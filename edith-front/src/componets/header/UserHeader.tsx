import React, { useState } from "react";
interface UserProps {
  userGitAccount: string;
}
const UserHeader = React.memo(({ userGitAccount }: UserProps) => {
  return (
    <div className="flex justify-center ml-4 mr-4">
      <div className="w-full flex justify-between items-center">
        <p className="text-black text-[28px] font-semibold">
          @{userGitAccount} Projects 💻
        </p>
      </div>
    </div>
  );
});
export default UserHeader;
