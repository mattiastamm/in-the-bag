import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function updateBagDiscs(bagId, userDiscIds) {
  const apiUrl = `${import.meta.env.VITE_API_URL}/api/v1/bags/${bagId}`;

  try {
    const response = await fetchWithAuth(apiUrl, {
      method: "PUT",
      body: JSON.stringify({ userDiscIds }), 
    });

    if (!response.ok) {
      throw new Error(`Failed to update bag. Status: ${response.status}`);
    }

    return true;
  } catch (error) {
    console.error("Error updating bag discs:", error);
    return false;
  }
}
