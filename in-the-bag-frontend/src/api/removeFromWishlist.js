import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function removeFromWishlist(discId) {
    const res = await fetchWithAuth(`${import.meta.env.VITE_API_URL}/api/wishlist/remove?discId=${discId}`, {
      method: "DELETE",
    });
  
    if (!res.ok) {
      throw new Error("Failed to remove disc from wishlist.");
    }
  
    return true;
  }
  