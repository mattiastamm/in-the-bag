import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function getDiscDetails(userDiscId) {
  const apiUrl = `${import.meta.env.VITE_API_URL}/api/v1/user-discs/${userDiscId}/details`;
  console.log("Fetching disc details from:", apiUrl);

  try {
    const response = await fetchWithAuth(apiUrl, { method: "GET" });

    if (!response.ok) {
      throw new Error(`Error: ${response.statusText}`);
    }

    const data = await response.json();
    console.log("Fetched disc details:", data);
    return data;
  } catch (error) {
    console.error("Failed to fetch disc details:", error);
    throw error;
  }
}
