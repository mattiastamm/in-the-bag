import { useState } from "react";
import { createBag } from "../api/createBag";

export default function AddNewBagModal({ onClose, refetch }) {
  const [title, setTitle] = useState("");
  const [comment, setComment] = useState("");

  const handleSave = async () => {
    if (!title.trim()) {
      alert("Please enter a bag name");
      return;
    }

    const success = await createBag({ title, comment });
    if (success) {
      refetch();
      onClose();
    } else {
      alert("Bag creation failed.");
    }
  };

  return (
    <div className="fixed inset-0 bg-gray-500/50 backdrop-blur-sm flex justify-center items-center z-50">
      <div className="bg-white p-6 rounded-lg shadow-md w-[90%] md:w-[60%] lg:w-[40%]">
        <div className="flex justify-between items-center mb-4 border-b pb-2">
          <h2 className="text-xl font-bold">Create New Bag</h2>
          <button
            onClick={onClose}
            className="text-3xl font-bold text-red-500 hover:text-red-700"
          >
            &times;
          </button>
        </div>

        <div className="mb-4">
          <label className="block mb-1 font-medium text-gray-700">Bag Name</label>
          <input
            type="text"
            className="w-full border rounded px-3 py-2"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Tournament, Casual, etc."
          />
        </div>

        <div className="mb-4">
          <label className="block mb-1 font-medium text-gray-700">Comment (optional)</label>
          <textarea
            className="w-full border rounded px-3 py-2"
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            placeholder="Add a description..."
          />
        </div>

        <div className="flex justify-end gap-3">
          <button
            className="px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400 cursor-pointer"
            onClick={onClose}
          >
            Cancel
          </button>
          <button
            className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 cursor-pointer"
            onClick={handleSave}
          >
            Save
          </button>
        </div>
      </div>
    </div>
  );
}
