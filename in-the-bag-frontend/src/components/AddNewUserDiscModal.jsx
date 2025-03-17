import React, { useState, useEffect } from "react";
import { addNewDisc } from "../api/addNewDisc";
import { discNameAutoFill } from "../api/discNameAutoFill";
import { discCreationAutoFill } from "../api/discCreationAutoFill";

export default function AddNewUserDiscModal({ onClose, refetch }) {
  const [searchQuery, setSearchQuery] = useState("");
  const [searchResults, setSearchResults] = useState([]);
  const [selectedDisc, setSelectedDisc] = useState(null);
  const [errorMessage, setErrorMessage] = useState(null);
  const [isEditingSearch, setIsEditingSearch] = useState(false);
  const [tempSearchQuery, setTempSearchQuery] = useState("");

  const [formData, setFormData] = useState({
    discId: null,
    plasticId: "",
    weight: 0, // Preset to 0 so the user must enter a weight > 0
    color: "#ffffff",
    customSpeed: null,
    customGlide: null,
    customTurn: null,
    customFade: null,
    comment: "",
  });

  const flightNumberLimits = {
    customSpeed: { min: 1, max: 14 },
    customGlide: { min: 1, max: 7 },
    customTurn: { min: -5, max: 1 },
    customFade: { min: 0, max: 5 },
  };

  // ✅ Only search if no disc has been selected
  useEffect(() => {
    if (selectedDisc) return;
    if (searchQuery.length > 1) {
      discNameAutoFill(searchQuery)
        .then(setSearchResults)
        .catch(() => setSearchResults([]));
    } else {
      setSearchResults([]);
    }
  }, [searchQuery, selectedDisc]);

  // ✅ When a disc is selected from the search results
  const handleDiscSelect = async (discId) => {
    try {
      const details = await discCreationAutoFill(discId);
      setSelectedDisc(details);
      setSearchQuery(details.name); // Keep the disc name in the search bar
      setSearchResults([]); // Clear suggestions immediately
      // Autofill flight numbers from the selected disc details
      setFormData({
        ...formData,
        discId: details.id,
        customSpeed: details.speed,
        customGlide: details.glide,
        customTurn: details.turn,
        customFade: details.fade,
      });
    } catch (error) {
      console.error("Error fetching disc details:", error);
    }
  };

  // ✅ Update flight numbers while respecting limits
  const updateFlightNumber = (field, change) => {
    const newValue = formData[field] + change;
    if (newValue >= flightNumberLimits[field].min && newValue <= flightNumberLimits[field].max) {
      setFormData({ ...formData, [field]: newValue });
    }
  };

  // ✅ Validate form: all required fields must be set and weight must be > 0
  const isFormValid = () => {
    return (
      formData.discId &&
      formData.plasticId &&
      formData.weight > 0 &&
      formData.customSpeed !== null &&
      formData.customGlide !== null &&
      formData.customTurn !== null &&
      formData.customFade !== null
    );
  };

  // ✅ Handle saving the new disc
  const handleSave = async () => {
    if (!isFormValid()) {
      setErrorMessage("Please fill in all required fields and ensure weight is greater than 0.");
      return;
    }
    const userId = 1; // Hardcoded for now
    try {
      const success = await addNewDisc({ ...formData, userId });
      if (success) {
        refetch(); // Refresh inventory after adding disc
        onClose();  // Close modal if successful
      } else {
        alert("Failed to add the disc.");
      }
    } catch (error) {
      console.error("Error adding disc:", error);
      alert("An error occurred while adding the disc.");
    }
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-gray-300/40 backdrop-blur-md z-50">
      <div className="bg-white w-[50%] max-h-[80vh] min-h-[70vh] rounded-lg shadow-lg p-6 flex flex-col overflow-y-auto">
        
        {/* Header */}
        <div className="flex justify-between items-center border-b pb-2">
          <h2 className="text-xl font-bold">Add New Disc</h2>
          <button 
            onClick={onClose}
            className="text-red-500 font-bold text-3xl transition-transform transform hover:scale-105 hover:text-red-700"
          >
            &times;
          </button>
        </div>
  
        {/* Error Message */}
        {errorMessage && <p className="text-red-500 text-center mt-2">{errorMessage}</p>}
  
        {/* Disc Search Input */}
        <div className="mt-4 relative">
            <label className="block text-gray-700 mb-1">Search for a Disc:</label>
            <input
                type="text"
                // If we are currently editing, show tempSearchQuery; otherwise show selectedDisc's name or an empty string
                value={isEditingSearch ? tempSearchQuery : (selectedDisc ? selectedDisc.name : searchQuery)}
                onFocus={() => {
                // If a disc is already selected, we copy its name into tempSearchQuery
                // so that the user can start editing. If no disc, we just keep the typed content.
                setTempSearchQuery(selectedDisc ? selectedDisc.name : searchQuery);
                setIsEditingSearch(true);
                }}
                onChange={(e) => {
                setTempSearchQuery(e.target.value);
                // You can trigger immediate searching here if you want:
                if (e.target.value.length > 1) {
                    discNameAutoFill(e.target.value)
                    .then(setSearchResults)
                    .catch(() => setSearchResults([]));
                } else {
                    setSearchResults([]);
                }
                }}
                onBlur={() => {
                // When user leaves the search bar (without picking from dropdown):
                // If a disc is already selected => revert search bar to that disc's name
                // If no disc => keep tempSearchQuery as searchQuery
                if (selectedDisc) {
                    setSearchQuery(selectedDisc.name);
                } else {
                    setSearchQuery(tempSearchQuery);
                }
                setIsEditingSearch(false);
                setSearchResults([]);
                }}
                className="border rounded px-2 py-1 w-full"
                placeholder="Enter disc name..."
            />

            {/* Only show suggestions if user is editing AND typed >= 2 chars */}
            {isEditingSearch && tempSearchQuery.length > 1 && searchResults.length > 0 && (
                <ul className="absolute bg-white border rounded w-full mt-1 shadow-md max-h-40 overflow-y-auto">
                {searchResults.map((disc) => (
                    <li
                    key={disc.id}
                    // Use onMouseDown instead of onClick, so selection happens before onBlur
                    onMouseDown={() => handleDiscSelect(disc.id)}
                    className="p-2 hover:bg-gray-200 cursor-pointer"
                    >
                    {disc.name}
                    </li>
                ))}
                </ul>
            )}
        </div>
  
        {/* Main Content */}
        <div className="flex-1 flex flex-col justify-center">
          {selectedDisc ? (
            <>
              {/* Disc Info */}
              <div className="text-center flex flex-col items-center mt-2">
                <p className="text-gray-600"><strong>Type:</strong> {selectedDisc.type}</p>
                <p className="text-gray-600"><strong>Manufacturer:</strong> {selectedDisc.manufacturerName}</p>
              </div>
  
              {/* Flight Numbers & Plastic Section in Grid */}
              <div className="grid grid-cols-2 gap-6 mt-4 place-items-center">
                
                {/* Flight Numbers (Left) */}
                <div className="flex flex-col items-center">
                  <p className="text-gray-700 text-lg font-semibold mb-2">Flight Numbers:</p>
                  <div className="flex gap-6">
                    {Object.keys(flightNumberLimits).map((field, index) => (
                      <div key={index} className="flex flex-col items-center">
                        <button
                          onClick={() => updateFlightNumber(field, 0.5)}
                          disabled={formData[field] >= flightNumberLimits[field].max}
                          className="bg-gray-200 px-3 py-1 rounded disabled:opacity-50"
                        >
                          +
                        </button>
                        <span className={`text-lg font-bold px-3 py-1 text-center w-10 ${
                          formData[field] !== selectedDisc[field.replace("custom", "").toLowerCase()]
                            ? "text-green-500"
                            : "text-gray-700"
                        }`}>
                          {formData[field]}
                        </span>
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
  
                {/* Color, Plastic, and Weight (Right) */}
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
                      value={formData.plasticId || ""}
                      onChange={(e) => setFormData({ ...formData, plasticId: parseInt(e.target.value) })}
                      className="border rounded px-2 py-1"
                    >
                      <option value="">Select Plastic</option>
                      {selectedDisc.availablePlastics.map((plastic) => (
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
                      onChange={(e) => setFormData({ ...formData, weight: parseFloat(e.target.value) })}
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
            </>
          ) : (
            // Placeholder message when no disc is selected
            <div className="flex-1 flex items-center justify-center text-gray-500 text-lg">
              Select a disc to continue
            </div>
          )}
        </div>
  
        {/* Save Button (Always at the Bottom) */}
        <div className="mt-4">
          <button 
            className="bg-blue-500 text-white px-4 py-2 rounded w-full transition-transform hover:bg-blue-700"
            onClick={handleSave}
          >
            Add Disc
          </button>
        </div>
  
      </div>
    </div>
  );
  
}
