import React from "react";

export default function InventoryCard({
  name,
  customSpeed,
  customGlide,
  customTurn,
  customFade,
  color,
  plasticName,
  customPlastic,
  speed,
  glide,
  turn,
  fade,
}) {
  // Function to determine color (green if custom value differs)
  const getTextColor = (customValue, originalValue) => {
    return customValue !== originalValue ? "text-green-500 font-bold" : "text-gray-300";
  };

  return (
    <div className="w-full border-2 bg-gray-700 border-gray-600 shadow-md rounded-lg flex flex-col 
      transition-transform transform hover:shadow-lg hover:bg-gray-600 hover:border-gray-500">
  
      {/* Header - Name and Plastic on Separate Lines */}
      <div className="p-1 text-center leading-tight">
        <div className="text-base text-gray-300 truncate max-w-full" title={plasticName || customPlastic}>
          {plasticName || customPlastic}
        </div>
        <div className="text-lg font-semibold truncate">{name}</div>
      </div>
  
      {/* Circle (Disc Color) */}
      <div className="flex-1 flex items-center justify-center">
        <div
          className="w-20 h-20 md:w-24 md:h-24 rounded-full border border-gray-500"
          style={{ backgroundColor: color }}
        />
      </div>
  
      {/* Flight Numbers */}
      <div className="p-1 text-base text-gray-300 text-center">
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
