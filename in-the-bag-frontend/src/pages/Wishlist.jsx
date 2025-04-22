import { useQuery } from "@tanstack/react-query";
import { getWishlist } from "../api/getWishlist";
import WishlistCard from "../components/WishlistCard";
import WishlistOptionsModal from "../components/WishlistOptionsModal";
import { useState } from "react";

const discTypeOrder = ["Putt & Approach", "Midrange", "Fairway Driver", "Distance Driver"];

export default function Wishlist() {
  const { data: wishlistDiscs, isLoading, error, refetch } = useQuery({
    queryKey: ["wishlistDiscs"],
    queryFn: getWishlist,
  });

  const [selectedDisc, setSelectedDisc] = useState(null);

  if (isLoading) return <p className="text-center mt-10">Loading wishlist...</p>;
  if (error) return <p className="text-center mt-10 text-red-500">Failed to load wishlist</p>;

  // Organize discs by type
  const categorizedDiscs = wishlistDiscs.reduce((acc, disc) => {
    const type = disc.type;
    if (!acc[type]) acc[type] = [];
    acc[type].push(disc);
    return acc;
  }, {});

  return (
    <div>
      <div className="flex justify-between items-center mt-3 mb-9">
        <h1 className="text-3xl font-bold pl-1">My Wishlist</h1>
        <button 
          className="text-2xl px-4 py-2 rounded invisible"
        >
          + Add Disc
        </button>
      </div>

      {discTypeOrder.map((type) =>
        categorizedDiscs[type] && categorizedDiscs[type].length > 0 ? (
          <div key={type} className="mb-10">
            <h2 className="text-2xl font-semibold -mx-6 mb-8 py-1 pl-7 bg-gray-200">{type}</h2>
            <div className="custom-grid gap-10 auto-rows-fr pl-8">
              {categorizedDiscs[type].map((disc) => (
                <div 
                  key={disc.id} 
                  className="cursor-pointer"
                  onClick={() => setSelectedDisc(disc)}
                >
                  <WishlistCard
                    key={disc.id}
                    name={disc.name}
                    manufacturer={disc.manufacturer}
                    speed={disc.speed}
                    glide={disc.glide}
                    turn={disc.turn}
                    fade={disc.fade}
                    type={disc.type}
                  />
                </div>
              ))}
            </div>
          </div>
        ) : null
      )}

      {wishlistDiscs.length === 0 && (
        <p className="text-center text-gray-600 text-lg mt-16">
          You haven't added any discs to your wishlist yet.
        </p>
      )}

      {selectedDisc && (
        <WishlistOptionsModal 
          disc={selectedDisc} 
          onClose={() => setSelectedDisc(null)}
          refetch={refetch}
        />
      )}
    </div>
  );
}
