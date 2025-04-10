import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function deleteAccount(password) {
  const response = await fetchWithAuth(
    `${import.meta.env.VITE_API_URL}/api/users`,
    {
      method: "DELETE",
      body: JSON.stringify({ password }),
    }
  );

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || "Delete failed");
  }

  return true;
}
