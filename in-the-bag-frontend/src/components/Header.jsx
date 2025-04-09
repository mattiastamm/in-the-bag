import { useState, useRef, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FaUserCircle } from "react-icons/fa";
import basketLogo from "../assets/basket.svg";
import { logout, isAuthenticated } from "../utils/authHelpers";

export default function Header() {
  const [showDropdown, setShowDropdown] = useState(false);
  const navigate = useNavigate();
  const dropdownRef = useRef(null); // 👈 Ref for the dropdown container

  const handleProfileClick = () => {
    navigate("/profile");
    setShowDropdown(false);
  };

  const handleLogoutClick = () => {
    logout();
  };

  const isLoggedIn = isAuthenticated();

  // 👇 Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowDropdown(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  return (
    <header className="bg-gray-800 text-white p-6 flex items-center justify-between shadow-md">
      {/* Logo Section */}
      <div className="flex items-center space-x-1 w-1/5 pl-3">
        <img src={basketLogo} alt="Disc Golf Basket" className="h-10 w-auto filter invert" />
        <span className="text-2xl font-bold">In The Bag</span>
      </div>

      {/* Nav Links */}
      <nav className="flex-1 flex justify-center space-x-15 font-semibold">
        <Link to="/inventory" className="hover:text-gray-300 text-2xl">Inventory</Link>
        <Link to="/my-bags" className="hover:text-gray-300 text-2xl">My Bags</Link>
        <Link to="/wishlist" className="hover:text-gray-300 text-2xl">Wishlist</Link>
      </nav>

      {/* Profile Icon + Dropdown */}
      <div className="w-1/5 flex justify-end pr-3 relative" ref={dropdownRef}>
        {isLoggedIn && (
          <>
            <button onClick={() => setShowDropdown(prev => !prev)}>
              <FaUserCircle className="text-5xl cursor-pointer hover:text-gray-300" />
            </button>

            {showDropdown && (
              <div className="absolute right-0 mt-12 w-32 -mr-6 bg-white text-gray-800 border rounded shadow z-50">
                <button
                  onClick={handleProfileClick}
                  className="block w-full text-left px-4 py-2 hover:bg-gray-100 cursor-pointer"
                >
                  Profile
                </button>
                <button
                  onClick={handleLogoutClick}
                  className="block w-full text-left px-4 py-2 text-red-500 hover:bg-gray-100 cursor-pointer"
                >
                  Log out
                </button>
              </div>
            )}
          </>
        )}
      </div>
    </header>
  );
}
