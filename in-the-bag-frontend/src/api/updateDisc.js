import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function updateDisc(userDiscId, updatedData) {
  const apiUrl = `${import.meta.env.VITE_API_URL}/api/v1/user-discs/${userDiscId}`;
  
  const response = await fetchWithAuth(apiUrl, {
    method: "PATCH",
    body: JSON.stringify(updatedData),
  });

  if (response.status === 204) {
    return true;
  }

  const errorData = await response.json();
  throw new Error(errorData.message || "Failed to update disc");
}