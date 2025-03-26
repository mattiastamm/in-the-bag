export async function updateBagDiscs(bagId, userDiscIds) {
    const apiUrl = `${import.meta.env.VITE_API_URL}/api/bags/update-discs`;
  
    const body = {
      bagId,
      userDiscIds,
    };
  
    try {
      const response = await fetch(apiUrl, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
      });
  
      if (!response.ok) {
        throw new Error(`Failed to update bag. Status: ${response.status}`);
      }
  
      return true;
    } catch (error) {
      console.error("Error updating bag discs:", error);
      return false;
    }
  }
  