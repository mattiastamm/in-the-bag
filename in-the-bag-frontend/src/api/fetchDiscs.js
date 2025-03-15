export async function getDiscs(userId) {
    const apiUrl = `${import.meta.env.VITE_API_URL}/api/discs/${userId}`;
    console.log("Fetching discs from:", apiUrl);  // ✅ Debugging log

    try {
        const response = await fetch(apiUrl);

        if (!response.ok) {
            throw new Error(`Error: ${response.statusText}`);
        }

        const data = await response.json();
        console.log("Fetched data:", data);  // ✅ Debugging log
        return data;
    } catch (error) {
        console.error("Failed to fetch discs:", error);
        throw error;
    }
}
