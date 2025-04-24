import WishlistCard from "../components/WishlistCard";
import { useQuery } from "@tanstack/react-query";
import { getWishlist } from "../api/getWishlist";

import { Swiper, SwiperSlide } from "swiper/react";
import { Autoplay } from "swiper/modules";
import "swiper/css";

export default function Wishlist() {
  const { data: wishlistDiscs, isLoading, error } = useQuery({
    queryKey: ["wishlistDiscs"],
    queryFn: getWishlist,
  });

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>Failed to load wishlist.</p>;

  return (
    <div>
      {/* Header */}
      <header className="flex justify-between items-center py-4">
      <h1 className="text-3xl font-bold">My Wishlist</h1>
      <button className="invisible text-2xl px-4 py-2 rounded">+ Add Disc</button>
      </header>

      <div className="flex flex-col items-center justify-center min-h-[calc(100vh-300px)] mx-14 py-8">
        {/* Carousel with fixed, responsive heights */}
        <div
          className="
            flex  
            items-center justify-center
            overflow-hidden
            h-[420px]
            w-full
          "
        >
          {wishlistDiscs.length > 0 ? (
            <Swiper
              modules={[Autoplay]}
              autoplay={{ delay: 5000, disableOnInteraction: false, pauseOnMouseEnter: true }}
              spaceBetween={50}
              slidesPerView={1}
              breakpoints={{
                640:  { slidesPerView: 2 },
                1360: { slidesPerView: 4 },
                1600: { slidesPerView: 5 },
              }}
              loop
              grabCursor
              className="h-full w-full"
            >
              {wishlistDiscs.map((disc) => (
                <SwiperSlide key={disc.suggestionId} className="h-full flex items-center justify-center p-3">
                  <WishlistCard {...disc} />
                </SwiperSlide>
              ))}
            </Swiper>
          ) : (
            <p className="text-center text-gray-600 text-lg">
              You haven't added any discs to your wishlist yet.
            </p>
          )}
        </div>
      </div>
    </div>
  );
}