import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function getSuggestions(bagId) {
    const url = `${import.meta.env.VITE_API_URL}/api/suggestions/suggest?bagId=${bagId}`;
  
    const res = await fetchWithAuth(url, {
      method: "GET",
    });
  
    if (!res.ok) {
      throw new Error("Failed to get suggestions");
    }
  
    return res.json();
  }
  
