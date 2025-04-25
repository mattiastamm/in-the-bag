import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function getBagsWithDiscs() {
  const apiUrl = `${import.meta.env.VITE_API_URL}/api/v1/bags`;

  try {
    const response = await fetchWithAuth(apiUrl, { method: "GET" });

    if (!response.ok) {
      throw new Error(`Failed to fetch bags: ${response.statusText}`);
    }

    return await response.json();
  } catch (error) {
    console.error("Error fetching bags:", error);
    throw error;
  }
}
