import { getAuthHeaders } from "../utils/authHelpers";

export async function deleteBag(bagId) {
    try {
      const response = await fetch(`${import.meta.env.VITE_API_URL}/api/bags/delete?bagId=${bagId}`, {
        method: "DELETE",
        headers: getAuthHeaders(),
      });
  
      return response.ok;
    } catch (error) {
      console.error("Failed to delete bag:", error);
      return false;
    }
  }
  