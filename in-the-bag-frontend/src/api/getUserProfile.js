import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function getUserProfile() {
  const apiUrl = `${import.meta.env.VITE_API_URL}/api/users/profile`;

  try {
    const response = await fetchWithAuth(apiUrl, {
      method: "GET",
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch user profile: ${response.statusText}`);
    }

    const data = await response.json();
    return data; // Expected to contain: email, createdAt, totalDiscs, totalBags, etc.
  } catch (error) {
    console.error("Error fetching user profile:", error);
    throw error;
  }
}
