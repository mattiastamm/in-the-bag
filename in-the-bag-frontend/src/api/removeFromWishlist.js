import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function removeFromWishlist(suggestionId) {
  const res = await fetchWithAuth(
    `${import.meta.env.VITE_API_URL}/api/v1/wishlist/${suggestionId}`,
    {
      method: "DELETE",
    }
  );

  if (!res.ok) {
    throw new Error("Failed to remove disc from wishlist.");
  }

  return true;
}

  