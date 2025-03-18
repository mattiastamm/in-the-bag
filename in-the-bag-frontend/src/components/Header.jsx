import { Link } from "react-router-dom";
import { FaUserCircle } from "react-icons/fa"; // Profile icon
import basketLogo from "../assets/basket.svg"; // ✅ Import new SVG logo

export default function Header() {
  return (
    <header className="bg-gray-800 text-white p-6 flex items-center justify-between shadow-md">
      {/* Logo Section with Text */}
      <div className="flex items-center space-x-1 w-1/5 pl-3">
        {/* ✅ SVG Logo */}
        <img src={basketLogo} alt="Disc Golf Basket" className="h-10 w-auto filter invert" />
        
        {/* ✅ Text beside the logo */}
        <span className="text-2xl font-bold">In The Bag</span>
      </div>

      {/* Navigation Links */}
      <nav className="flex-1 flex justify-center space-x-15 font-semibold">
        <Link to="/inventory" className="hover:text-gray-300 text-2xl">Inventory</Link>
        <Link to="/my-bags" className="hover:text-gray-300 text-2xl">My Bags</Link>
        <Link to="/wishlist" className="hover:text-gray-300 text-2xl">Wishlist</Link>
      </nav>

      {/* Profile Icon */}
      <div className="w-1/5 flex justify-end pr-3">
        <Link to="/profile">
          <FaUserCircle className="text-5xl cursor-pointer hover:text-gray-300" />
        </Link>
      </div>
    </header>
  );
}
