import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function addDiscsToWishlist(discIds) {
  const res = await fetchWithAuth(`${import.meta.env.VITE_API_URL}/api/wishlist/add`, {
    method: "POST",
    body: JSON.stringify({ 
      discIds: discIds  // Make sure discIds is properly nested in the request body
    }),
  });

  if (res.status !== 201) {
    throw new Error("Failed to add disc to wishlist");
  }
}