import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { getDiscs } from "../api/fetchDiscs";
import { getDiscDetails } from "../api/fetchDiscDetails";
import DiscCard from "../components/DiscCard";
import DiscDetailsModal from "../components/DiscDetailsModal";

export default function Inventory() {
  const { data: userDiscs, error, isLoading, refetch } = useQuery({
    queryKey: ["discs"],
    queryFn: getDiscs,
  });

  const [selectedDisc, setSelectedDisc] = useState(null);
  const [isLoadingDetails, setIsLoadingDetails] = useState(false);
  const [errorDetails, setErrorDetails] = useState(null);

  const fetchDiscDetails = async (userId, discId) => {
    setIsLoadingDetails(true);
    setErrorDetails(null);
    try {
      const discDetails = await getDiscDetails(userId, discId);
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
  const discTypeOrder = ["Distance Driver", "Fairway Driver", "Midrange", "Putt & Approach"];

  // Organize discs by type
  const categorizedDiscs = userDiscs.reduce((acc, userDisc) => {
    const type = userDisc.type;
    if (!acc[type]) acc[type] = [];
    acc[type].push(userDisc);
    return acc;
  }, {});

  return (
    <div>
      <h1 className="text-3xl font-bold mt-3 mb-9 pl-1">My Inventory</h1>
      
      {/* Render each category in predefined order */}
      {discTypeOrder.map((type) => (
        categorizedDiscs[type] && categorizedDiscs[type].length > 0 && (
          <div key={type} className="mb-9">
            <h2 className="text-2xl font-semibold -mx-6 mb-8 pl-7 bg-gray-200">{type}</h2>
            <div className="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-7 2xl:grid-cols-9 gap-10 auto-rows-fr pl-8">
              {categorizedDiscs[type].map((userDisc) => (
                <div 
                  key={userDisc.id} 
                  onClick={() => fetchDiscDetails(1, userDisc.id)} // USER_ID NEEDS TO BE FETCHED FROM LOCAL STORAGE OR COOKIE IN THE FUTURE!!!
                  className="cursor-pointer"
                >
                  <DiscCard
                    name={userDisc.name}
                    type={userDisc.type}
                    customSpeed={userDisc.customSpeed}
                    customGlide={userDisc.customGlide}
                    customTurn={userDisc.customTurn}
                    customFade={userDisc.customFade}
                    color={userDisc.color}
                    plasticName={userDisc.plasticName}
                    manufacturer={userDisc.manufacturerName}
                    speed={userDisc.speed}
                    glide={userDisc.glide}
                    turn={userDisc.turn}
                    fade={userDisc.fade}
                    inUse={userDisc.inUse}
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
    </div>
  );
}
