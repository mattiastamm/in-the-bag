import React, { useState } from "react";
import SuggestionCard from "./SuggestionCard";
import { addDiscsToWishlist } from "../api/addDiscsToWishlist";

export default function SuggestionModal({ suggestions, onClose }) {
  const [pageIndex, setPageIndex] = useState(0);
  const [selectedDiscs, setSelectedDiscs] = useState(new Set());

  const totalPages = suggestions.length;
  const current = suggestions[pageIndex];

  const toggleDisc = (discId) => {
    const updated = new Set(selectedDiscs);
    updated.has(discId) ? updated.delete(discId) : updated.add(discId);
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
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm p-4">
      <div className="bg-white rounded-lg p-6 max-w-4xl w-full shadow-lg">
        {totalPages === 0 ? (
          <>
            <h2 className="text-2xl font-bold text-center mb-4">🥏 No Suggestions Needed!</h2>
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
              {current.discs.map((disc) => {
                const isSelected = selectedDiscs.has(disc.id);
                return (
                  <div
                    key={disc.id}
                    onClick={() => toggleDisc(disc.id)}
                    className={`cursor-pointer border-4 rounded-lg transition-all ${
                      isSelected ? "border-blue-500" : "border-transparent"
                    }`}
                  >
                    <SuggestionCard {...disc} />
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
                  className="bg-gray-300 hover:bg-gray-400 px-4 py-2 rounded disabled:opacity-50 cursor-pointer"
                >
                  Prev
                </button>

                <button
                  onClick={handleNext}
                  disabled={pageIndex === totalPages - 1}
                  className="bg-gray-300 hover:bg-gray-400 px-4 py-2 rounded disabled:opacity-50 cursor-pointer"
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
