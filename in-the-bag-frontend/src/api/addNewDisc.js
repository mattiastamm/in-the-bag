import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function addNewDisc(discData) {
  const apiUrl = `${import.meta.env.VITE_API_URL}/api/v1/user-discs`;
  console.log("Adding new disc with data:", discData);

  const response = await fetchWithAuth(apiUrl, {
    method: "POST",
    body: JSON.stringify(discData),
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || "Failed to add new disc");
  }

  console.log("New disc added successfully!");
  return true;
}
