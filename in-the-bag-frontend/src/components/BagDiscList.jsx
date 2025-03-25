import React from "react";
import BagDiscItem from "./BagDiscItem";

export default function BagDiscList({ discs, bagId, onRemoveDisc }) {
    const discTypeOrder = ["Distance Driver", "Fairway Driver", "Midrange", "Putt & Approach"];
  
    const grouped = discs.reduce((acc, disc) => {
      if (!acc[disc.type]) acc[disc.type] = [];
      acc[disc.type].push(disc);
      return acc;
    }, {});
  
    return (
      <div className="w-full mt-6">
        {discTypeOrder.map((type) => (
          grouped[type] && grouped[type].length > 0 && (
            <div key={type} className="mb-4">
              <h2 className="text-xl font-semibold -mx-6 mb-4 pl-6 bg-gray-200 py-2">{type}</h2>
              <div className="flex flex-col gap-2">
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
