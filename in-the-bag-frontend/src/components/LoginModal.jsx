import React, { useState } from "react";
import { login } from "../api/auth";
import { saveAuthData } from "../utils/authHelpers";

export default function LoginModal({ onClose, onLoginSuccess, switchToSignup }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);

  const handleSubmit = async () => {
    try {
      const { token } = await login({ email, password });
      saveAuthData({ token });
      onLoginSuccess?.(); // Notify parent if needed
      onClose(); // Close modal
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm">
      <div className="bg-white p-6 rounded-lg w-[90%] max-w-md shadow-lg">
        <h2 className="text-2xl font-bold mb-4">Log In</h2>

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
          className="w-full border px-3 py-2 mb-4 rounded"
        />

        <button
          onClick={handleSubmit}
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 cursor-pointer"
        >
          Log In
        </button>

        <div className="text-center mt-4 text-sm">
          Don't have an account?{" "}
          <button className="text-blue-600 underline cursor-pointer" onClick={switchToSignup}>
            Sign up
          </button>
        </div>
      </div>
    </div>
  );
}
