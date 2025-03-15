import React from "react";

export default function DiscCard({
  name,
  type,
  customSpeed,
  customGlide,
  customTurn,
  customFade,
  color,
  plasticName,
  manufacturer,
  speed,
  glide,
  turn,
  fade,
  inUse
}) {
  // Function to determine color (green if custom value differs)
  const getTextColor = (customValue, originalValue) => {
    return customValue !== originalValue ? "text-green-500 font-bold" : "text-gray-700";
  };

  return (
    <div className="w-full border-2 border-gray-400 shadow-md rounded-lg flex flex-col 
    transition-transform transform hover:scale-105 hover:shadow-lg hover:border-gray-600 hover:bg-gray-100">
      {/* Header */}
      <div className="p-1 text-center text-lg font-semibold leading-tight">
        {name} <span className="text-lg text-gray-600">{plasticName}</span>
      </div>

      {/* Circle (Disc Color) */}
      <div className="flex-1 flex items-center justify-center">
        <div
          className="w-20 h-20 md:w-24 md:h-24 rounded-full border border-gray-300"
          style={{ backgroundColor: color }}
        />
      </div>

      {/* Flight Numbers */}
      <div className="p-1 text-base text-gray-700 text-center">
        <p>
          <span className={getTextColor(customSpeed, speed)}>{customSpeed}</span> | 
          <span className={getTextColor(customGlide, glide)}> {customGlide}</span> | 
          <span className={getTextColor(customTurn, turn)}> {customTurn}</span> | 
          <span className={getTextColor(customFade, fade)}> {customFade}</span>
        </p>
      </div>
    </div>
  );
}
