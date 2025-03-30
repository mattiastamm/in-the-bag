import React from "react";
import BagDiscItem from "./BagDiscItem";
import { PenLine, Trash2 } from "lucide-react";

export default function BagDiscList({ discs, bagId, onRemoveDisc, onEditBag, onDeleteBag }) {
  const discTypeOrder = ["Putt & Approach", "Midrange", "Fairway Driver", "Distance Driver"];

  const grouped = discs.reduce((acc, disc) => {
    if (!acc[disc.type]) acc[disc.type] = [];
    acc[disc.type].push(disc);
    return acc;
  }, {});

  return (
    <div className="w-full mt-6">

      {/* Buttons at the Top */}
      <div className="mb-6 flex justify-center gap-6">
        {/* Edit Bag Button */}
        <button
            onClick={onEditBag}
            className="bg-yellow-500 text-white text-xl px-6 py-3 rounded-lg hover:bg-yellow-600 transition cursor-pointer shadow-md inline-flex items-center space-x-2"
        >
            <span>Edit Bag</span>
            <PenLine size={24} />
        </button>

        {/* Delete Bag Button */}
        <button
            onClick={onDeleteBag}
            className="bg-red-500 text-white text-xl px-6 py-3 rounded-lg hover:bg-red-600 transition cursor-pointer shadow-md inline-flex items-center space-x-2"
        >
            <span>Delete Bag</span>
            <Trash2 size={24} />
        </button>
      </div>

      {/* ✅ Grouped Disc Display */}
      {discTypeOrder.map((type) => (
        grouped[type] && grouped[type].length > 0 && (
          <div key={type} className="mb-4">
            <h2 className="text-xl font-semibold mb-4 pl-6 bg-gray-200 py-2">{type}</h2>
            <div className="flex flex-col gap-2 mx-5">
              {grouped[type].map((disc) => (
                <BagDiscItem
                  key={disc.userDiscId}
                  disc={disc}
                  onRemove={() => onRemoveDisc(disc.userDiscId, bagId)}
                />
              ))}
            </div>
          </div>
        )
      ))}
    </div>
  );
}
