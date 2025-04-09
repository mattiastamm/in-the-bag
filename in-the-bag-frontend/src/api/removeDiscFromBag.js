import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function removeDiscFromBag(userDiscId, bagId) {
  const apiUrl = `${import.meta.env.VITE_API_URL}/api/bags/remove-disc?userDiscId=${userDiscId}&bagId=${bagId}`;

  try {
    const response = await fetchWithAuth(apiUrl, {
      method: "DELETE",
    });

    if (!response.ok) {
      throw new Error("Failed to remove disc from bag");
    }

    return true;
  } catch (error) {
    console.error("Error removing disc:", error);
    return false;
  }
}
