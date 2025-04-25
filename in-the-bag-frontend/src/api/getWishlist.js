import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function getWishlist() {
  const res = await fetchWithAuth(`${import.meta.env.VITE_API_URL}/api/v1/wishlist`, {
    method: "GET",
  });

  if (!res.ok) {
    throw new Error("Failed to fetch wishlist.");
  }

  return res.json(); // Returns array of discs or wishlist items
}
