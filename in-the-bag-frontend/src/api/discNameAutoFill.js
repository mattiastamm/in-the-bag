import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function discNameAutoFill(name) {
  if (!name.trim()) return [];

  try {
    const response = await fetchWithAuth(
      `${import.meta.env.VITE_API_URL}/api/v1/discs?name=${encodeURIComponent(name)}`,
      { method: "GET" }
    );

    if (!response.ok) {
      throw new Error(`Error: ${response.statusText}`);
    }

    return await response.json();
  } catch (error) {
    console.error("Failed to fetch discs:", error);
    return [];
  }
}

