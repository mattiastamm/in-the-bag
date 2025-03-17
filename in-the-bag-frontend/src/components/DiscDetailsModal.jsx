import React, { useState } from "react";
import { updateDisc } from "../api/updateDisc";
import { deleteDisc } from "../api/deleteDisc"; 
import { useEffect } from "react";

export default function DiscDetailsModal({ disc, onClose, isLoading, error, refetch }) {
    if (!disc && !isLoading) return null; // ✅ Don't render if no disc is selected  

    useEffect(() => {
      document.body.classList.add("modal-open");  // ✅ Disable scrolling when modal opens
      return () => document.body.classList.remove("modal-open"); // ✅ Enable scrolling when modal closes
    }, []);
    
    // ✅ Local state to track changes before saving
    const [formData, setFormData] = useState(() => ({
        customSpeed: disc.customSpeed,
        customGlide: disc.customGlide,
        customTurn: disc.customTurn,
        customFade: disc.customFade,
        color: disc.color,
        plasticId: disc.plasticId,
        weight: 0,
        comment: disc.comment,
    }));

    // ✅ Flight number limits
    const flightNumberLimits = {
        customSpeed: { min: 1, max: 14 },
        customGlide: { min: 1, max: 7 },
        customTurn: { min: -5, max: 1 },
        customFade: { min: 0, max: 5 },
    };

    // ✅ Function to update flight numbers within limits
    const updateFlightNumber = (field, change) => {
        const newValue = formData[field] + change;
        if (newValue >= flightNumberLimits[field].min && newValue <= flightNumberLimits[field].max) {
            setFormData({ ...formData, [field]: newValue });
        }
    };

    const handleSave = async () => {
        try {
            const success = await updateDisc(disc.userDiscId, formData);

            if (success) {
                console.log("Disc updated successfully!");
                await refetch(); // ✅ Refresh inventory after saving
                onClose(); // ✅ Close modal if update is successful
            } else {
                alert("Failed to update the disc. It may not exist or belong to you.");
            }
        } catch (error) {
            console.error("Error updating disc:", error);
            alert("An error occurred while updating the disc.");
        }
    };

    // ✅ Handle Disc Deletion
    const handleDelete = async () => {
        const confirmDelete = window.confirm(`Are you sure you want to delete ${disc.name}?`);
        if (!confirmDelete) return;

        try {
            const success = await deleteDisc(disc.userDiscId);
            if (success) {
                console.log("Disc deleted successfully!");
                await refetch(); // ✅ Refresh inventory after deleting
                onClose(); // ✅ Close modal
            } else {
                alert("Failed to delete the disc. It may not exist.");
            }
        } catch (error) {
            console.error("Error deleting disc:", error);
            alert("An error occurred while deleting the disc.");
        }
    };

  return (
    <div 
      className="fixed inset-0 flex items-center justify-center bg-gray-300/40 backdrop-blur-md z-50" 
    >
      {/* Modal Box */}
      <div 
        className="bg-white w-[90%] md:w-[70%] lg:w-[50%] max-h-[90vh] rounded-lg shadow-lg p-6 flex flex-col overflow-y-auto" 
        onClick={(e) => e.stopPropagation()} // ✅ Prevent click inside from closing
      >
        {/* Header */}
        <div className="flex justify-between items-center border-b pb-2">
          <h2 className="text-xl font-bold">{disc?.name} ({disc?.plasticName})</h2>
          <button 
            onClick={onClose} 
            className="text-red-500 font-bold text-3xl transition-transform transform hover:scale-105 hover:text-red-700"> &times;
          </button>
        </div>

        {/* Loading and Error Handling */}
        {isLoading ? (
          <p className="text-center text-gray-500">Loading...</p>
        ) : error ? (
          <p className="text-center text-red-500">{error}</p>
        ) : (
          <>
            {/* Main Content */}
            <div className="flex-1 flex flex-col overflow-auto">

              {/* Disc Details */}
              <div className="text-center flex flex-col items-center mt-2">
                <p className="text-gray-600"><strong>Type:</strong> {disc.type}</p>
                <p className="text-gray-600"><strong>Manufacturer:</strong> {disc.manufacturerName}</p>
              </div>

              {/* Flight Numbers & Additional Fields in Centered Grid Layout */}
              <div className="grid grid-cols-2 gap-6 mt-4 place-items-center">
                
                {/* ✅ Flight Numbers (Left) - Centered */}
                <div className="flex flex-col items-center">
                  <p className="text-gray-700 text-lg font-semibold mb-2">Flight Numbers:</p>
                  <div className="flex gap-6">
                      {Object.keys(flightNumberLimits).map((field, index) => (
                          <div key={index} className="flex flex-col items-center">
                          {/* Increase Button */}
                          <button
                              onClick={() => updateFlightNumber(field, 0.5)}
                              disabled={formData[field] >= flightNumberLimits[field].max}
                              className="bg-gray-200 px-3 py-1 rounded disabled:opacity-50"
                          >
                              +
                          </button>

                          {/* Number Display (Centered) */}
                          <span className={`text-lg font-bold px-3 py-1 text-center w-10 ${
                            formData[field] !== disc[field.replace("custom", "").toLowerCase()]
                              ? "text-green-500"
                              : "text-gray-700"
                          }`}>
                              {formData[field]}
                          </span>

                          {/* Decrease Button */}
                          <button
                              onClick={() => updateFlightNumber(field, -0.5)}
                              disabled={formData[field] <= flightNumberLimits[field].min}
                              className="bg-gray-200 px-3 py-1 rounded disabled:opacity-50"
                          >
                              -
                          </button>
                          </div>
                      ))}
                  </div>
                </div>

                {/* ✅ Color, Plastic, and Weight (Right) - Centered */}
                <div className="flex flex-col items-center gap-4">
                  
                  {/* Color Picker */}
                  <div className="flex flex-col items-center">
                    <label className="block text-gray-700 mb-1">Color:</label>
                    <input
                      type="color"
                      value={formData.color}
                      onChange={(e) => setFormData({ ...formData, color: e.target.value })}
                      className="w-12 h-12 cursor-pointer"
                    />
                  </div>

                  {/* Plastic Dropdown */}
                  <div className="flex flex-col items-center">
                    <label className="block text-gray-700 mb-1">Plastic:</label>
                    <select
                      value={formData.plasticId}
                      onChange={(e) => setFormData({ ...formData, plasticId: parseInt(e.target.value) })}
                      className="border rounded px-2 py-1"
                    >
                      {disc.availablePlastics.map((plastic) => (
                        <option key={plastic.id} value={plastic.id}>
                          {plastic.name}
                        </option>
                      ))}
                    </select>
                  </div>

                  {/* Weight Input */}
                  <div className="flex flex-col items-center">
                    <label className="block text-gray-700 mb-1">Weight (grams):</label>
                    <input
                      type="number"
                      value={formData.weight}
                      min="1"
                      onChange={(e) => {
                        const newWeight = parseFloat(e.target.value);
                        if (newWeight >= 1 || e.target.value === "") {
                            setFormData({ ...formData, weight: e.target.value });
                        }
                      }}
                      className="border rounded px-2 py-1 w-24 text-center"
                    />
                  </div>
                </div>
              </div>

              {/* Comment Box */}
              <div className="mt-4 flex flex-col">
                <label className="block text-gray-700 mb-1">Comment:</label>
                <textarea
                  value={formData.comment}
                  onChange={(e) => setFormData({ ...formData, comment: e.target.value })}
                  className="border rounded px-2 py-1 w-full h-32 resize-none"
                />
              </div>
            </div>

            {/* Action Buttons - Save & Delete */}
            <div className="mt-4 flex justify-between gap-4">
                {/* Save Button */}
                <button 
                    className="bg-blue-500 text-white px-4 py-2 rounded w-1/2 transition-transform transform hover:bg-blue-700"
                    onClick={handleSave}>
                    Save
                </button>

                {/* Delete Button */}
                <button 
                    className="bg-red-500 text-white px-4 py-2 rounded w-1/2 transition-transform transform hover:bg-red-700"
                    onClick={handleDelete}>
                    Delete
                </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}
