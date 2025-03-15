import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/Header";
import Inventory from "./pages/Inventory";
import MyBags from "./pages/MyBags";
import Wishlist from "./pages/Wishlist";
import Profile from "./pages/Profile";

export default function App() {
  return (
    <Router>
      <Header />
      <div className="p-6"> {/* Adds spacing so content doesn't stick to the header */}
        <Routes>
          <Route path="/inventory" element={<Inventory />} />
          <Route path="/my-bags" element={<MyBags />} />
          <Route path="/wishlist" element={<Wishlist />} />
          <Route path="/profile" element={<Profile />} />
        </Routes>
      </div>
    </Router>
  );
}
