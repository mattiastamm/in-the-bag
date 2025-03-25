export async function fetchBagsWithDiscs(userId) {
    const apiUrl = `${import.meta.env.VITE_API_URL}/api/bags/${userId}/bags-with-discs`;
  
    try {
      const response = await fetch(apiUrl);
      if (!response.ok) {
        throw new Error(`Failed to fetch bags: ${response.statusText}`);
      }
  
      const data = await response.json();
      return data; // Should be an array of BagWithDiscsDto objects with discs inside
    } catch (error) {
      console.error("Error fetching bags:", error);
      throw error;
    }
  }
  