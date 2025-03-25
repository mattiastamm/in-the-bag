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
      {/* Disc Color Circle */}
      <div
        className="w-9 h-9 rounded-full mr-4"
        style={{ backgroundColor: color }}
      />

      {/* Disc Info */}
      <div className="flex-1 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 sm:gap-4 text-sm sm:text-base">
        <div className="flex flex-col sm:flex-row sm:gap-2">
          <span className="font-bold">{manufacturerName}</span>
          <span>{name}</span>
        </div>
        <div>{plasticName}</div>
        <div className="flex gap-1">
          <span className={getTextClass(customSpeed, speed)}>{customSpeed}</span>
          <span className={getTextClass(customGlide, glide)}>{customGlide}</span>
          <span className={getTextClass(customTurn, turn)}>{customTurn}</span>
          <span className={getTextClass(customFade, fade)}>{customFade}</span>
        </div>
        <div>{weight}g</div>
      </div>

      {/* Delete Button */}
      <button
        onClick={onRemove}
        className="ml-4 text-red-500 hover:text-red-700 transition-transform transform hover:scale-110 cursor-pointer"
        title="Remove from bag"
      >
        <Trash2 size={20} />
      </button>
    </div>
  );
}
