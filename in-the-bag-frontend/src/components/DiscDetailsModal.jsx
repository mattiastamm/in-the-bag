import React, { useState } from "react";
import { updateDisc } from "../api/updateDisc";

export default function DiscDetailsModal({ disc, onClose, isLoading, error, refetch }) {
  if (!disc && !isLoading) return null; // ✅ Don't render if no disc is selected

  // ✅ Local state to track changes before saving
  const [formData, setFormData] = useState(() => ({
    customSpeed: disc.customSpeed,
    customGlide: disc.customGlide,
    customTurn: disc.customTurn,
    customFade: disc.customFade,
    color: disc.color,
    plasticId: disc.plasticId,
    weight: disc.weight,
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

  return (
    <div 
      className="fixed inset-0 flex items-center justify-center bg-gray-300/40 backdrop-blur-md z-50" 
    >
      {/* Modal Box */}
      <div 
        className="bg-white w-[60%] h-[80%] rounded-lg shadow-lg p-6 relative flex flex-col" 
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
            <div className="flex-1 flex flex-col mt-2">
              {/* Disc Details */}
              <div>
                <p className="text-gray-600"><strong>Type:</strong> {disc.type}</p>
                <p className="text-gray-600"><strong>Manufacturer:</strong> {disc.manufacturerName}</p>
              </div>

              {/* Flight Numbers */}
              <div className="mt-4">
                <p className="text-gray-700 text-lg font-semibold">Flight Numbers:</p>
                <div className="flex gap-6 mt-2">
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

                        {/* Number Display (Fixed Width) */}
                        <span
                            className={`text-lg font-bold px-3 py-1 text-center w-10 ${
                            formData[field] !== disc[field.replace("custom", "").toLowerCase()]
                                ? "text-green-500"
                                : "text-gray-700"
                            }`}
                        >
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

              {/* Color Picker */}
              <div className="mt-4">
                <label className="block text-gray-700 mb-1">Color:</label>
                <input
                  type="color"
                  value={formData.color}
                  onChange={(e) => setFormData({ ...formData, color: e.target.value })}
                  className="w-12 h-12 cursor-pointer"
                />
              </div>

              {/* Plastic Dropdown */}
              <div className="mt-4">
                <label className="block text-gray-700 mb-1">Plastic:</label>
                <select
                    value={formData.plasticId} // ✅ Now bound to plasticId
                    onChange={(e) => setFormData({ ...formData, plasticId: parseInt(e.target.value) })}
                    className="border rounded px-2 py-1"
                >
                    {disc.availablePlastics.map((plastic) => (
                    <option key={plastic.id} value={plastic.id}>
                        {plastic.name} {/* ✅ Display name, but store ID */}
                    </option>
                    ))}
                </select>
              </div>

              {/* Weight Input */}
              <div className="mt-4">
                <label className="block text-gray-700 mb-1">Weight (grams):</label>
                <input
                    type="number"
                    value={formData.weight}
                    min="1" // ✅ Ensures the input does not allow values below 1
                    onChange={(e) => {
                    const newWeight = parseFloat(e.target.value);
                    if (newWeight >= 1 || e.target.value === "") {
                        setFormData({ ...formData, weight: e.target.value });
                    }
                    }}
                    className="border rounded px-2 py-1 w-24"
                />
              </div>

              {/* Stretchable Comment Box */}
              <div className="mt-4 flex-1 flex flex-col">
                <label className="block text-gray-700 mb-1">Comment:</label>
                <textarea
                  value={formData.comment}
                  onChange={(e) => setFormData({ ...formData, comment: e.target.value })}
                  className="border rounded px-2 py-1 w-full flex-1 resize-none"
                />
              </div>
            </div>

            {/* Save Button - Always at Bottom */}
            <div className="mt-4">
              <button 
                className="bg-blue-500 text-white px-4 py-2 rounded w-full transition-transform transform hover:bg-blue-700"
                onClick={handleSave}>
                Save
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}
