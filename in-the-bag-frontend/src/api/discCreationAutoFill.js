import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function discCreationAutoFill(discId) {
  try {
    const response = await fetchWithAuth(
      `${import.meta.env.VITE_API_URL}/api/v1/discs/${discId}/details`,
      { method: "GET" }
    );

    if (!response.ok) {
      throw new Error(`Error: ${response.statusText}`);
    }

    return await response.json();
  } catch (error) {
    console.error("Failed to fetch disc details:", error);
    return null;
  }
}
