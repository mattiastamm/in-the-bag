import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { isAuthenticated } from "../utils/authHelpers";

export default function ProtectedRoute({ children }) {
  const isLoggedIn = isAuthenticated();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isLoggedIn) {
      setTimeout(() => {
        navigate("/auth");
      }, 3000);
    }
  }, [isLoggedIn, navigate]);

  if (!isLoggedIn) {
    return (
      <div className="text-center mt-20 text-lg text-red-500 font-semibold">
        You need to be logged in to use this page. Redirecting...
      </div>
    );
  }

  return children;
}
