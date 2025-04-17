import React, { useState } from "react";
import SuggestionCard from "./SuggestionCard";

export default function SuggestionModal({ suggestions, onClose }) {
  const [pageIndex, setPageIndex] = useState(0);
  const totalPages = suggestions.length;

  const handleNext = () => {
    if (pageIndex < totalPages - 1) setPageIndex(pageIndex + 1);
  };

  const handlePrev = () => {
    if (pageIndex > 0) setPageIndex(pageIndex - 1);
  };

  const current = suggestions[pageIndex];

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm p-4">
      <div className="bg-white rounded-lg p-6 max-w-3xl w-full shadow-lg">
        {totalPages === 0 ? (
          <>
            <h2 className="text-2xl font-bold text-center mb-4">
              🥏 No Suggestions Needed!
            </h2>
            <p className="text-gray-700 text-center mb-2">
              Looks like your bag is already extremely versatile and covers all the essential disc types—great work!
            </p>
            <p className="text-gray-700 text-center mb-4">
              If you’re looking to take your game to the next level, here are a few suggestions:
            </p>
            <ul className="list-disc list-inside text-gray-700 space-y-2">
              <li>
                <strong>Specialty discs</strong> – Try out discs with unique flight characteristics for rollers,
                utility shots, or strong headwinds.
              </li>
              <li>
                <strong>Plastic variety</strong> – Different plastics can affect grip and flight. Consider trying your
                favorite molds in premium or baseline plastic.
              </li>
              <li>
                <strong>Practice backups</strong> – It’s always helpful to have duplicates of your go-to discs for
                field work or in case of loss.
              </li>
            </ul>

            <div className="text-center mt-6">
              <button
                onClick={onClose}
                className="bg-red-600 text-white px-6 py-2 rounded hover:bg-red-700 cursor-pointer"
              >
                Close
              </button>
            </div>
          </>
        ) : (
          <>
            {/* Title */}
            <h2 className="text-2xl text-center mb-6">
              <p className="font-bold">Suggestion {pageIndex + 1}</p>
              <p className="font-normal">{current.categoryLabel}</p>
            </h2>

            {/* Disc Cards */}
            <div className="grid sm:grid-cols-2 md:grid-cols-3 gap-4 mb-6">
              {current.discs.map((disc) => (
                <SuggestionCard
                  key={disc.id}
                  name={disc.name}
                  manufacturer={disc.manufacturer}
                  speed={disc.speed}
                  glide={disc.glide}
                  turn={disc.turn}
                  fade={disc.fade}
                />
              ))}
            </div>

            {/* Pagination + Close */}
            <div className="flex justify-between items-center mt-4">
              <button
                onClick={handlePrev}
                disabled={pageIndex === 0}
                className="bg-gray-300 hover:bg-gray-400 px-4 py-2 rounded disabled:opacity-50 cursor-pointer"
              >
                Prev
              </button>

              <button
                onClick={onClose}
                className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 cursor-pointer"
              >
                Close
              </button>

              <button
                onClick={handleNext}
                disabled={pageIndex === totalPages - 1}
                className="bg-gray-300 hover:bg-gray-400 px-4 py-2 rounded disabled:opacity-50 cursor-pointer"
              >
                Next
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}
