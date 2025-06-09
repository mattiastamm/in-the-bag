import React, { useState } from "react";
import SuggestionCard from "./SuggestionCard";
import { addDiscsToWishlist } from "../api/addDiscsToWishlist";

export default function SuggestionModal({ suggestions, onClose }) {
  const [pageIndex, setPageIndex] = useState(0);
  const [selectedDiscs, setSelectedDiscs] = useState(new Set());

  const totalPages = suggestions.length;
  const currentCategory = suggestions[pageIndex];

  const toggleDisc = (suggestionId) => {
    const updated = new Set(selectedDiscs);
    updated.has(suggestionId) ? updated.delete(suggestionId) : updated.add(suggestionId);
    setSelectedDiscs(updated);
  };

  const handleAddToWishlist = async () => {
    const ids = Array.from(selectedDiscs);
    try {
      await addDiscsToWishlist(ids);
      alert("Discs added to wishlist!");
      onClose();
    } catch (err) {
      alert("Failed to add to wishlist.");
      console.error(err);
    }
  };

  const handleNext = () => {
    if (pageIndex < totalPages - 1) setPageIndex(pageIndex + 1);
  };

  const handlePrev = () => {
    if (pageIndex > 0) setPageIndex(pageIndex - 1);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/70 backdrop-blur-sm p-4">
      <div className="bg-gray-800 rounded-lg p-6 max-w-4xl w-full shadow-lg max-h-[80vh] overflow-y-auto">
        {totalPages === 0 ? (
          // No suggestions block (unchanged)
          <>
            <h2 className="text-2xl font-bold text-center mb-4">🥏 No Suggestions Needed!</h2>
            <p className="text-white text-center mb-2">
              Looks like your bag is already extremely versatile and covers all the essential disc types—great work!
            </p>
            <p className="text-white text-center mb-4">
              If you’re looking to take your game to the next level, here are a few suggestions:
            </p>
            <ul className="list-disc list-inside text-white space-y-2">
              <li>
                <strong>Specialty discs</strong> – Try out discs with unique flight characteristics for rollers,
                utility shots, or strong headwinds.
              </li>
              <li>
                <strong>Plastic variety</strong> – Try your favorite molds in premium or baseline plastic.
              </li>
              <li>
                <strong>Practice backups</strong> – Always helpful to have duplicates of your go-to discs.
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
              <p className="font-normal mb-5">{currentCategory.categoryTitle}</p>
              <p className="text-lg text-gray-400 italic">
                Select the discs you want to add to your Wishlist
              </p>
            </h2>

            {/* Disc Cards */}
            <div className="grid sm:grid-cols-2 md:grid-cols-3 gap-4 mb-6">
              {currentCategory.discSuggestionDtos.map((disc) => {
                const isSelected = selectedDiscs.has(disc.suggestionId);
                return (
                  <div
                    key={disc.suggestionId}
                    onClick={() => toggleDisc(disc.suggestionId)}
                    className={`cursor-pointer border-4 rounded-lg transition-transform transform hover:scale-105 ${
                      isSelected ? "border-blue-500" : "border-transparent"
                    }`}
                  >
                    <div className="transform-gpu">
                      <SuggestionCard {...disc} />
                    </div>
                  </div>
                );
              })}
            </div>

            {/* Action Buttons */}
            <div className="flex flex-wrap justify-between items-center mt-4 gap-4">
              <div className="flex gap-2">
                <button
                  onClick={handlePrev}
                  disabled={pageIndex === 0}
                  className="bg-gray-400 hover:bg-gray-500 px-4 py-2 rounded disabled:opacity-50 cursor-pointer"
                >
                  Prev
                </button>

                <button
                  onClick={handleNext}
                  disabled={pageIndex === totalPages - 1}
                  className="bg-gray-400 hover:bg-gray-500 px-4 py-2 rounded disabled:opacity-50 cursor-pointer"
                >
                  Next
                </button>
              </div>

              <div className="flex gap-2">
                <button
                  onClick={onClose}
                  className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 cursor-pointer"
                >
                  Close
                </button>

                <button
                  onClick={handleAddToWishlist}
                  disabled={selectedDiscs.size === 0}
                  className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:opacity-50 cursor-pointer"
                >
                  Add to Wishlist
                </button>
              </div>
            </div>
          </>
        )}
      </div>
    </div>
  );
}
