import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function addDiscsToWishlist(suggestionIds) {
  const res = await fetchWithAuth(`${import.meta.env.VITE_API_URL}/api/v1/wishlist`, {
    method: "POST",
    body: JSON.stringify({ 
      suggestionIds: suggestionIds 
    }),
  });

  if (res.status !== 201) {
    throw new Error("Failed to add suggestions to wishlist");
  }
}