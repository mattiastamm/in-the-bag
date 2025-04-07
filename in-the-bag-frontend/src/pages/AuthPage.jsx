import React, { useState, useEffect } from "react";
import LoginModal from "../components/LoginModal";
import SignupModal from "../components/SignupModal";
import { isAuthenticated } from "../utils/authHelpers";
import { useNavigate } from "react-router-dom";

export default function AuthPage() {
  const [showSignup, setShowSignup] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated()) {
      navigate("/inventory");
    }
  }, []);

  const handleSuccess = () => {
    navigate("/inventory");
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center">
      {showSignup ? (
        <SignupModal
          onClose={() => setShowSignup(false)}
          switchToLogin={() => setShowSignup(false)}
          onSignupSuccess={handleSuccess}
        />
      ) : (
        <LoginModal
          onClose={() => {}}
          switchToSignup={() => setShowSignup(true)}
          onLoginSuccess={handleSuccess}
        />
      )}
    </div>
  );
}
