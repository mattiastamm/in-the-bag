import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function deleteDisc(userDiscId) {
  const apiUrl = `${import.meta.env.VITE_API_URL}/api/v1/user-discs/${userDiscId}`;
  console.log(`Deleting disc at: ${apiUrl}`);

  try {
    const response = await fetchWithAuth(apiUrl, {
      method: "DELETE",
    });

    if (response.status === 204) {
      console.log("Disc deleted successfully!");
      return true;
    } else if (response.status === 404) {
      console.error("Error: Disc not found.");
      return false;
    } else {
      throw new Error(`Unexpected error: ${response.statusText}`);
    }
  } catch (error) {
    console.error("Failed to delete disc:", error);
    return false;
  }
}
