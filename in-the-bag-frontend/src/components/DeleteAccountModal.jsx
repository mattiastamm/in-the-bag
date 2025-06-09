import { useState } from "react";
import { deleteAccount } from "../api/deleteAccount";
import { logout } from "../utils/authHelpers";

export default function DeleteAccountModal({ onClose }) {
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);

  const handleDelete = async () => {
    if (!password.trim()) {
      setError("Please enter your password.");
      return;
    }

    try {
      await deleteAccount(password);
      logout(); // Logs the user out after deletion
    } catch (err) {
      setError(err.message || "Failed to delete account");
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/70 backdrop-blur-sm">
      <div className="bg-gray-700 p-6 rounded-lg w-[90%] max-w-md shadow-lg">
        <h2 className="text-2xl font-bold text-red-600 mb-4">
          Confirm Account Deletion
        </h2>
        <p className="mb-4 text-sm text-white">
          This action cannot be undone. Please enter your password to proceed.
        </p>

        <input
          type="password"
          placeholder="Your password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="w-full border px-3 py-2 rounded mb-4"
        />

        {error && <p className="text-red-500 mb-3">{error}</p>}

        <div className="flex justify-between">
          <button
            onClick={onClose}
            className="text-white hover:underline cursor-pointer"
          >
            Cancel
          </button>
          <button
            onClick={handleDelete}
            className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded cursor-pointer"
          >
            Delete Account
          </button>
        </div>
      </div>
    </div>
  );
}
