import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function updateDisc(userDiscId, updatedData) {
  const apiUrl = `${import.meta.env.VITE_API_URL}/api/user-discs/${userDiscId}`;
  console.log("Updating disc at:", apiUrl);

  try {
    const response = await fetchWithAuth(apiUrl, {
      method: "PATCH",
      body: JSON.stringify(updatedData),
    });

    if (response.status === 204) {
      console.log("Disc updated successfully!");
      return true;
    } else if (response.status === 404) {
      console.error("Error: Disc not found or does not belong to the user.");
      return false;
    } else {
      throw new Error(`Unexpected error: ${response.statusText}`);
    }
  } catch (error) {
    console.error("Failed to update disc:", error);
    return false;
  }
}
