import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { getDiscs } from "../api/getDiscs";
import { getDiscDetails } from "../api/getDiscDetails";
import InventoryCard from "../components/InventoryCard";
import DiscDetailsModal from "../components/DiscDetailsModal";
import AddNewUserDiscModal from "../components/AddNewUserDiscModal";


export default function Inventory() {
  const { data: userDiscs, error, isLoading, refetch } = useQuery({
    queryKey: ["discs"],
    queryFn: () => getDiscs(),
  });

  const [selectedDisc, setSelectedDisc] = useState(null);
  const [isAddingDisc, setIsAddingDisc] = useState(false);
  const [isLoadingDetails, setIsLoadingDetails] = useState(false);
  const [errorDetails, setErrorDetails] = useState(null);

  const fetchDiscDetails = async (userDiscId) => {
    setIsLoadingDetails(true);
    setErrorDetails(null);
    try {
      const discDetails = await getDiscDetails(userDiscId);
      setSelectedDisc(discDetails);  // ✅ Pass the full object
    } catch (error) {
      setErrorDetails("Failed to load disc details.");
    } finally {
      setIsLoadingDetails(false);
    }
  };

  if (isLoading) return <p>Loading discs...</p>;
  if (error) return <p>Error fetching discs: {error.message}</p>;

  // Predefined order of disc types
  const discTypeOrder = ["Putt & Approach", "Midrange", "Fairway Driver", "Distance Driver"];

  // Organize discs by type
  const categorizedDiscs = userDiscs.reduce((acc, userDisc) => {
    const type = userDisc.type;
    if (!acc[type]) acc[type] = [];
    acc[type].push(userDisc);
    return acc;
  }, {});

  return (
    <div>
      {/* Header with "My Inventory" + "Add Disc" Button */}
      <div className="flex justify-between items-center mt-3 mb-9">
        <h1 className="text-3xl font-bold pl-1">My Inventory</h1>
        <button 
          onClick={() => setIsAddingDisc(true)}
          className="bg-green-500 text-2xl px-4 py-2 rounded transition-transform transform hover:bg-green-700 cursor-pointer"
        >
          + Add Disc
        </button>
      </div>
      
      {/* Show either empty state message or disc grid */}
      {userDiscs.length === 0 ? (
        <div className="flex items-center justify-center h-[50vh]">
          <p className="text-lg italic text-center px-4">
            No discs found. To add your first disc, press the{" "}
            <span className="font-semibold">Add Disc</span> button.
          </p>
        </div>
      ) : (
        discTypeOrder.map((type) => (
          categorizedDiscs[type] && categorizedDiscs[type].length > 0 && (
            <div key={type} className="mb-9">
              <h2 className="text-2xl font-semibold -mx-6 mb-8 py-1.5 pl-7 bg-gray-900">{type}</h2>
              <div className="inventory-grid gap-10 auto-rows-fr pl-8">
                {categorizedDiscs[type]
                  .slice()
                  .sort((a, b) => a.customSpeed - b.customSpeed)
                  .map((userDisc) => (
                    <div 
                      key={userDisc.userDiscId} 
                      onClick={() => fetchDiscDetails(userDisc.userDiscId)}
                      className="cursor-pointer transform transition-transform hover:scale-107"
                    >
                      <InventoryCard
                        name={userDisc.name}
                        customSpeed={userDisc.customSpeed}
                        customGlide={userDisc.customGlide}
                        customTurn={userDisc.customTurn}
                        customFade={userDisc.customFade}
                        color={userDisc.color}
                        plasticName={userDisc.plasticName}
                        customPlastic={userDisc.customPlastic}
                        speed={userDisc.speed}
                        glide={userDisc.glide}
                        turn={userDisc.turn}
                        fade={userDisc.fade}
                      />
                    </div>
                  ))}
              </div>
            </div>
          )
        ))
      )}

      {/* ✅ Render Modal when a disc is selected */}
      {selectedDisc && (
        <DiscDetailsModal 
          disc={selectedDisc} 
          onClose={() => setSelectedDisc(null)} 
          isLoading={isLoadingDetails}
          error={errorDetails}
          refetch={refetch}
        />
      )}

      {/* ✅ Render AddNewUserDiscModal when clicking "Add Disc" */}
      {isAddingDisc && (
        <AddNewUserDiscModal 
          preSelectedDiscId={null} // The user will search for discs to add, nothing is preselected
          onClose={() => {
            setIsAddingDisc(false); // Just close the modal, ignore the boolean
          }}
          refetch={refetch}
        />
      )}

    </div>
  );
}
