import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function createBag({ title, comment }) {
  const apiUrl = `${import.meta.env.VITE_API_URL}/api/bags`;

  try {
    const response = await fetchWithAuth(apiUrl, {
      method: "POST",
      body: JSON.stringify({ title, comment }),
    });

    if (!response.ok) throw new Error("Failed to create bag");

    return await response.json(); // newly created bag
  } catch (error) {
    console.error("Create bag failed:", error);
    return null;
  }
}
