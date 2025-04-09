import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function updateBagDiscs(bagId, userDiscIds) {
  const apiUrl = `${import.meta.env.VITE_API_URL}/api/bags/update-discs`;

  try {
    const response = await fetchWithAuth(apiUrl, {
      method: "PUT",
      body: JSON.stringify({ bagId, userDiscIds }),
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
