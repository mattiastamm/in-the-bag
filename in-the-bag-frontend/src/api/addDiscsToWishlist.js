import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function addDiscsToWishlist(discId) {
  const res = await fetchWithAuth(`${import.meta.env.VITE_API_URL}/api/wishlist/add`, {
    method: "POST",
    body: JSON.stringify({ discId }),
  });

  if (res.status !== 201) {
    throw new Error("Failed to add disc to wishlist");
  }
}
