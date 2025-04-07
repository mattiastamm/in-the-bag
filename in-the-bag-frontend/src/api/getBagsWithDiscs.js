import { getAuthHeaders } from "../utils/authHelpers";

export async function getBagsWithDiscs(userId) {
  const apiUrl = `${import.meta.env.VITE_API_URL}/api/bags/${userId}/bags-with-discs`;

  try {
    const response = await fetch(apiUrl, {
      method: "GET",
      headers: getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch bags: ${response.statusText}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching bags:", error);
    throw error;
  }
}
