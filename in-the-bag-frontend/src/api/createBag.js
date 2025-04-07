import { getAuthHeaders } from "../utils/authHelpers";

export async function createBag({ userId, title, comment }) {
    const apiUrl = `${import.meta.env.VITE_API_URL}/api/bags/create`;
  
    try {
      const response = await fetch(apiUrl, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify({ userId, title, comment }),
      });
  
      if (!response.ok) throw new Error("Failed to create bag");
  
      return await response.json(); // returns newly created bag
    } catch (error) {
      console.error("Create bag failed:", error);
      return null;
    }
}
  