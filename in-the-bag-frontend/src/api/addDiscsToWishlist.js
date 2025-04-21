import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function addDiscsToWishlist(discIds) {
    const res = await fetchWithAuth(`${import.meta.env.VITE_API_URL}/api/wishlist/add`, {
      method: "POST",
      body: JSON.stringify({ discIds }), // expects { discIds: [123, 456] }
    });
  
    if (!res.ok) {
      throw new Error("Failed to add discs to wishlist.");
    }
  
    return res.json(); // optional - depends on what your backend returns
  }
  