import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useComponentStore } from "../../store/repoPageStore";
interface KebabMenuProps {
  projectID: number;
}

function KebabMenu({ projectID }: KebabMenuProps) {
  const toggleComponent = useComponentStore((state) => state.toggleComponent);
  const setSelectedProjectID = useComponentStore(
    (state) => state.setSelectedProjectID
  );
  const [isOpen, setIsOpen] = useState(false);

  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };

  const handleMoveToSetting = () => {
    setSelectedProjectID(projectID);
    toggleComponent(3);
  };
  return (
    <div className="relative inline-block text-left">
      <button
        onClick={toggleMenu}
        className="p-2 rounded-full hover:bg-gray-200 focus:outline-none"
      >
        <svg
          className="w-6 h-6 text-gray-600"
          fill="currentColor"
          viewBox="0 0 20 20"
        >
          <path d="M10 6a2 2 0 110-4 2 2 0 010 4zm0 6a2 2 0 110-4 2 2 0 010 4zm0 6a2 2 0 110-4 2 2 0 010 4z" />
        </svg>
      </button>
      {isOpen && (
        <div className="absolute right-0 mt-2 w-36 bg-white rounded-lg shadow-lg z-10">
          <ul className="py-1 text-gray-700">
            <li
              className="px-4 py-2 hover:bg-gray-100 cursor-pointer"
              onClick={handleMoveToSetting}
            >
              ⚙ 프로젝트 설정
            </li>
            <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">
              🗑 프로젝트 삭제
            </li>
          </ul>
        </div>
      )}
    </div>
  );
}

export default KebabMenu;
