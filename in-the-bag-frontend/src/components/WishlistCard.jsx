import React from "react";

export default function WishlistCard({ name, manufacturer, speed, glide, turn, fade, type }) {
  return (
    <div className="w-full border-2 border-gray-400 shadow-md rounded-lg flex flex-col transition-transform transform hover:scale-105 hover:shadow-lg hover:border-gray-600 hover:bg-gray-100">
      {/* Manufacturer & Name */}
      <div className="p-1 text-center leading-tight">
        <p className="text-sm text-gray-600">{manufacturer}</p>
        <div className="text-lg font-semibold truncate">{name}</div>
      </div>

      {/* Disc Color (defaulted) */}
      <div className="flex-1 flex items-center justify-center">
        <div
          className="w-20 h-20 md:w-24 md:h-24 rounded-full border border-gray-300"
          style={{ backgroundColor: "#89CFF0" }}
        />
      </div>

      {/* Flight Numbers */}
      <div className="p-1 text-base text-gray-700 text-center">
        <p>
          <span>{speed}</span> | <span>{glide}</span> | <span>{turn}</span> | <span>{fade}</span>
        </p>
      </div>
    </div>
  );
}
