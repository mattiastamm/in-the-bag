import React, { useState } from "react";
import { signup } from "../api/auth";
import { saveAuthData } from "../utils/authHelpers";

export default function SignupModal({ onClose, onSignupSuccess, switchToLogin }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState(null);

  const handleSignup = async () => {
    setError(null);
    if (!email || !password) {
      setError("Email and password are required.");
      return;
    }
    if (password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    try {
      const { token } = await signup({ email, password });
      saveAuthData({ token });
      onSignupSuccess?.();
      onClose();
    } catch (err) {
      setError(err.message || "Signup failed.");
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm">
      <div className="bg-white p-6 rounded-lg w-[90%] max-w-md shadow-lg">
        <h2 className="text-2xl font-bold mb-4">Sign Up</h2>

        {error && <p className="text-red-500 mb-2">{error}</p>}

        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="w-full border px-3 py-2 mb-3 rounded"
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="w-full border px-3 py-2 mb-3 rounded"
        />

        <input
          type="password"
          placeholder="Confirm Password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          className="w-full border px-3 py-2 mb-4 rounded"
        />

        <button
          onClick={handleSignup}
          className="w-full bg-green-600 text-white py-2 rounded hover:bg-green-700 cursor-pointer"
        >
          Create Account
        </button>

        <div className="text-center mt-4 text-sm">
          Already have an account?{" "}
          <button className="text-blue-600 underline cursor-pointer" onClick={switchToLogin}>
            Log in
          </button>
        </div>
      </div>
    </div>
  );
}
