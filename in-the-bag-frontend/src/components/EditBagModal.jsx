import React, { useEffect, useState } from "react";
import { getDiscs } from "../api/fetchDiscs";
import { updateBagDiscs } from "../api/updateBagDiscs";
import DiscCard from "./DiscCard";

export default function EditBagModal({ bagId, initialSelectedDiscIds, onClose, refetch }) {
  const [allDiscs, setAllDiscs] = useState([]);
  const [selectedDiscs, setSelectedDiscs] = useState(new Set(initialSelectedDiscIds));

  useEffect(() => {
    async function fetchInventory() {
      const discs = await getDiscs(1); // hardcoded userId = 1
      setAllDiscs(discs);
    }
    fetchInventory();
  }, []);

  const toggleDisc = (discId) => {
    const newSelection = new Set(selectedDiscs);
    newSelection.has(discId) ? newSelection.delete(discId) : newSelection.add(discId);
    setSelectedDiscs(newSelection);
  };

  const handleSave = async () => {
    const success = await updateBagDiscs(bagId, Array.from(selectedDiscs));
    if (success) {
      await refetch();
      onClose();
    } else {
      alert("Failed to update the bag");
    }
  };

  return (
    <div className="fixed inset-0 bg-black/30 backdrop-blur-sm z-50 flex items-center justify-center">
      <div className="bg-white w-[90%] max-h-[90vh] rounded-lg p-6 overflow-y-auto shadow-lg">
        <div className="flex justify-between items-center border-b pb-2 mb-4">
          <h2 className="text-xl font-bold">Edit Bag Contents</h2>
          <button onClick={onClose} className="text-red-500 text-3xl font-bold hover:text-red-700">&times;</button>
        </div>

        {/* Disc Grid */}
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-6">
          {allDiscs.map((disc) => (
            <div
              key={disc.userDiscId}
              className={`cursor-pointer border-4 rounded-lg transition-all ${
                selectedDiscs.has(disc.userDiscId)
                  ? "border-blue-500"
                  : "border-transparent"
              }`}
              onClick={() => toggleDisc(disc.userDiscId)}
            >
              <DiscCard {...disc} />
            </div>
          ))}
        </div>

        {/* Action Buttons */}
        <div className="mt-6 flex justify-end gap-4">
          <button
            className="bg-gray-300 px-4 py-2 rounded hover:bg-gray-400 cursor-pointer"
            onClick={onClose}
          >
            Cancel
          </button>
          <button
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 cursor-pointer"
            onClick={handleSave}
          >
            Save Changes
          </button>
        </div>
      </div>
    </div>
  );
}
