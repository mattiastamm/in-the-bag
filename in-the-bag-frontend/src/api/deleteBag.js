import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function deleteBag(bagId) {
  const apiUrl = `${import.meta.env.VITE_API_URL}/api/bags/delete?bagId=${bagId}`;

  try {
    const response = await fetchWithAuth(apiUrl, {
      method: "DELETE",
    });

    return response.ok;
  } catch (error) {
    console.error("Failed to delete bag:", error);
    return false;
  }
}
