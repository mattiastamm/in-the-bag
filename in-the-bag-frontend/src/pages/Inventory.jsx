import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { getDiscs } from "../api/fetchDiscs";
import { getDiscDetails } from "../api/fetchDiscDetails";
import DiscCard from "../components/DiscCard";
import DiscDetailsModal from "../components/DiscDetailsModal";
import AddNewUserDiscModal from "../components/AddNewUserDiscModal";


export default function Inventory() {
  const userId = 1; // ✅ Hardcoded for now, needs to be replaced later!

  const { data: userDiscs, error, isLoading, refetch } = useQuery({
    queryKey: ["discs", userId],
    queryFn: () => getDiscs(userId),
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
          className="bg-green-500 text-white text-2xl px-4 py-2 rounded transition-transform transform hover:bg-green-700"
        >
          + Add Disc
        </button>
      </div>
      
      {/* Render each category in predefined order */}
      {discTypeOrder.map((type) => (
        categorizedDiscs[type] && categorizedDiscs[type].length > 0 && (
          <div key={type} className="mb-9">
            <h2 className="text-2xl font-semibold -mx-6 mb-8 pl-7 bg-gray-200">{type}</h2>
            <div className="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-7 grid-cols-custom 2xl:grid-cols-9 gap-10 auto-rows-fr pl-8">
              {categorizedDiscs[type].map((userDisc) => (
                <div 
                  key={userDisc.userDiscId} 
                  onClick={() => fetchDiscDetails(userDisc.userDiscId)}
                  className="cursor-pointer"
                >
                  <DiscCard
                    name={userDisc.name}
                    customSpeed={userDisc.customSpeed}
                    customGlide={userDisc.customGlide}
                    customTurn={userDisc.customTurn}
                    customFade={userDisc.customFade}
                    color={userDisc.color}
                    plasticName={userDisc.plasticName}
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
      ))}

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
          onClose={() => setIsAddingDisc(false)}
          refetch={refetch}
        />
      )}

    </div>
  );
}
