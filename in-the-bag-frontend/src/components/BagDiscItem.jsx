import React from "react";
import { Trash2 } from "lucide-react";

export default function BagDiscItem({ disc, onRemove }) {
  const {
    name,
    color,
    plasticName,
    manufacturerName,
    customSpeed,
    customGlide,
    customTurn,
    customFade,
    speed,
    glide,
    turn,
    fade,
    weight,
  } = disc;

  // Helper: Highlight custom numbers if they differ
  const getTextClass = (custom, original) =>
    custom !== original ? "text-green-500 font-semibold" : "text-gray-800";

  return (
    <div className="flex items-center justify-between bg-gray-100 px-4 py-2 rounded-md shadow-sm w-full">
      {/* Disc Color Circle (non-shrinking) */}
      <div
        className="w-9 h-9 rounded-full flex-shrink-0"
        style={{ backgroundColor: color }}
      />
      <span className="font-bold mx-5">{name}</span>

      {/* Disc Info (flexible, wrapping content) */}
      <div className="flex-1 min-w-0 flex items-center justify-evenly gap-2 text-sm sm:text-base">
        {/* Name + Manufacturer/Plastic */}
        <div className="flex items-center gap-x-4 min-w-0">
          <div className="flex flex-wrap gap-x-4 text-gray-600 min-w-0">
            <span className="whitespace-nowrap">{manufacturerName}</span>
            <span className="whitespace-nowrap">{plasticName}</span>
          </div>
        </div>

        {/* Flight numbers + Weight */}
        <div className="flex items-center flex-shrink-0">
          <div className="flex gap-1">
            <span className={getTextClass(customSpeed, speed)}>{customSpeed}</span> /
            <span className={getTextClass(customGlide, glide)}>{customGlide}</span> /
            <span className={getTextClass(customTurn, turn)}>{customTurn}</span> /
            <span className={getTextClass(customFade, fade)}>{customFade}</span> 
          </div>
        </div>

        <div>{weight}g</div>
      </div>

      {/* Delete Button (non-shrinking) */}
      <button
        onClick={onRemove}
        className="ml-3 text-red-500 hover:text-red-700 transition-transform transform hover:scale-110 cursor-pointer flex-shrink-0"
        title="Remove from bag"
      >
        <Trash2 size={20} />
      </button>
    </div>
  );
}
