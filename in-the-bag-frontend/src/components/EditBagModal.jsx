import React, { useEffect, useState } from "react";
import { getDiscs } from "../api/getDiscs";
import { updateBagDiscs } from "../api/updateBagDiscs";
import InventoryCard from "./InventoryCard";

export default function EditBagModal({ bagId, initialSelectedDiscIds, onClose, refetch }) {
  const [allDiscs, setAllDiscs] = useState([]);
  const [selectedDiscs, setSelectedDiscs] = useState(new Set(initialSelectedDiscIds));

  const discTypeOrder = ["Putt & Approach", "Midrange", "Fairway Driver", "Distance Driver"];

  useEffect(() => {
    async function fetchInventory() {
      const discs = await getDiscs(); 
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

  // Group discs by type
  const groupedDiscs = allDiscs.reduce((acc, disc) => {
    if (!acc[disc.type]) acc[disc.type] = [];
    acc[disc.type].push(disc);
    return acc;
  }, {});

  return (
    <div className="fixed inset-0 bg-black/70 backdrop-blur-sm z-50 flex items-center justify-center">
      <div className="bg-gray-800 w-[90%] max-h-[90vh] rounded-lg p-6 overflow-y-auto shadow-lg">
        {/* Header */}
        <div className="flex justify-between items-center border-b pb-2 mb-4">
          <h2 className="text-xl font-bold">Edit Bag Contents</h2>
          <button onClick={onClose} className="text-red-500 text-3xl font-bold hover:text-red-700 cursor-pointer">&times;</button>
        </div>

        {/* Grouped Disc Lists */}
        {discTypeOrder.map((type) => (
          groupedDiscs[type]?.length > 0 && (
            <div key={type} className="mb-6">
              <h3 className="text-lg font-semibold mb-3 bg-gray-900 py-2 px-6 rounded -mx-6">{type}</h3>
              <div className="editBagModal-grid gap-6 auto-rows-fr">
                {groupedDiscs[type].map((disc) => (
                  <div
                  key={disc.userDiscId}
                  className={`cursor-pointer border-4 rounded-lg transition-transform transform hover:scale-105 ${
                    selectedDiscs.has(disc.userDiscId)
                      ? "border-blue-500"
                      : "border-transparent"
                  }`}
                  onClick={() => toggleDisc(disc.userDiscId)}
                >
                  <div className="transform-gpu">
                    <InventoryCard {...disc} />
                  </div>
                </div>
                ))}
              </div>
            </div>
          )
        ))}

        {/* Action Buttons */}
        <div className="mt-15 flex gap-4 justify-center">
          <button
            className="bg-gray-400 px-4 py-2 rounded hover:bg-gray-500 cursor-pointer"
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
