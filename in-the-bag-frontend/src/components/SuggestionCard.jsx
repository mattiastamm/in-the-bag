import React from "react";

export default function SuggestionCard({ name, manufacturer, speed, glide, turn, fade }) {
  return (
    <div className="w-full border-2 bg-gray-700 border-gray-600 shadow-md rounded-lg flex flex-col transition-transform transform hover:shadow-lg hover:bg-gray-600 hover:border-gray-500">
      {/* Name */}
      <div className="p-1 text-center leading-tight">
        <p className="text-gray-300"> {manufacturer} </p>
        <div className="text-lg font-semibold truncate">{name}</div>
      </div>

      {/* Circle (Disc Color) */}
      <div className="flex-1 flex items-center justify-center">
        <div
          className="w-20 h-20 md:w-24 md:h-24 rounded-full border border-gray-500"
          style={{ backgroundColor: "#89CFF0" }}
        />
      </div>

      {/* Flight Numbers */}
      <div className="p-1 text-base text-white text-center">
        <p>
          <span>{speed}</span> | <span>{glide}</span> | <span>{turn}</span> | <span>{fade}</span>
        </p>
      </div>
    </div>
  );
}
