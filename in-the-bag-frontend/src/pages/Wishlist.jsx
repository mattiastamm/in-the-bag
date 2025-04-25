import WishlistCard from "../components/WishlistCard";
import { useQuery } from "@tanstack/react-query";
import { getWishlist } from "../api/getWishlist";
import { useState, useEffect } from "react";
import { removeFromWishlist } from "../api/removeFromWishlist";
import AddNewUserDiscModal from "../components/AddNewUserDiscModal";

import { Swiper, SwiperSlide } from "swiper/react";
import { Autoplay, Navigation } from "swiper/modules";
import "swiper/css";
import 'swiper/css/navigation';


export default function Wishlist() {
  const { data: wishlistDiscs, isLoading, error, refetch } = useQuery({
    queryKey: ["wishlistDiscs"],
    queryFn: getWishlist,
  });

  // function to calculate current slidesPerView
  const getCurrentSlidesPerView = () => {
    const width = window.innerWidth;
    if (width >= 1600) return 4;
    if (width >= 1200) return 3;
    if (width >= 820) return 2;
    return 1;
  };

  const [currentSlide, setCurrentSlide] = useState(1);
  const [slidesPerView, setSlidesPerView] = useState(getCurrentSlidesPerView());
  const [transferDisc, setTransferDisc] = useState(null);

  // Add useEffect to update slidesPerView on window resize
  useEffect(() => {
    const handleResize = () => {
      setSlidesPerView(getCurrentSlidesPerView());
    };

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>Failed to load wishlist.</p>;


  const handleAddToInventory = (disc) => {
    setTransferDisc(disc); // full disc object
  };

  const handleRemove = async (disc) => {
    const confirmed = window.confirm(`Are you sure you want to remove ${disc.name} from your wishlist?`);
    if (!confirmed) return;

    try {
      const success = await removeFromWishlist(disc.suggestionId);
      if (!success) throw new Error();
      await refetch();
    } catch (error) {
      console.error("Failed to remove disc:", error);
      alert("Something went wrong when removing the disc.");
    }
  };

  const handleCloseTransferModal = async (wasAdded) => {
    if (wasAdded && transferDisc) {
      try {
        await removeFromWishlist(transferDisc.suggestionId);
        await refetch();
      } catch (error) {
        console.error("Failed to remove after transfer:", error);
      }
    }
    setTransferDisc(null);
  };


  return (
    <div className="flex flex-col items-center justify-center min-h-[calc(100vh-300px)] py-8">
      {/* Text about the page */}
      {wishlistDiscs.length > 0 && (
        <div className="pb-10">
          <p className="text-center text-gray-700 text-xl max-w-3xl mx-auto mb-8">
            You have <span className="font-semibold">{wishlistDiscs.length}</span> disc{wishlistDiscs.length !== 1 && "s"} in your Wishlist. 
            Press <span className="font-medium">"Add to Inv"</span> to add them directly to your Inventory and
            <span className="font-medium"> "Remove"</span> to remove them from your Wishlist.
          </p>
        </div>
      )}

      {/* Carousel with fixed, responsive heights */}
      <div
        className="
          flex  
          items-center justify-center
          h-[420px]
          w-full
        "
      >
        {wishlistDiscs.length > 0 ? (
          <Swiper
            style={{
              padding: "0 50px",  // Add padding for arrow space
              position: "relative", // Ensure proper stacking
            }}
            modules={[Autoplay, Navigation]}
            navigation={ true }
            autoplay={{ delay: 5000, disableOnInteraction: false, pauseOnMouseEnter: true }}
            spaceBetween={40}
            slidesPerView={1}
            breakpoints={{
              820:  { slidesPerView: 2 },
              1200: { slidesPerView: 3 },
              1600: { slidesPerView: 4 },
            }}
            loop
            grabCursor
            className="h-full w-full"
            onSlideChange={(swiper) => {
              // Add 1 because slide index is 0-based
              setCurrentSlide(swiper.realIndex + 1);
            }}
          >
            {wishlistDiscs.map((disc) => (
              <SwiperSlide key={disc.suggestionId} className="h-full flex items-center justify-center p-4 cursor-default">
                <WishlistCard
                  {...disc}
                  onAdd={() => handleAddToInventory(disc)}
                  onRemove={() => handleRemove(disc)}
                />
              </SwiperSlide>
            ))}
          </Swiper>
        ) : (
          <p className="text-center text-gray-600 text-lg">
            You haven't added any discs to your wishlist yet.
          </p>
        )}
      </div>

      {/* Page Index */}
      {wishlistDiscs.length > 0 && wishlistDiscs.length > slidesPerView && (
        <div className="mt-8 text-gray-600 text-lg font-medium">
          Page {currentSlide} of {wishlistDiscs.length}
        </div>
      )}

      {transferDisc && (
        <AddNewUserDiscModal
          preSelectedDiscId={transferDisc.discId}
          onClose={(wasAdded = false) => handleCloseTransferModal(wasAdded)}
          refetch={refetch}
        />
      )}
    </div>
  );
}