export async function getDiscDetails(userId, discId) {
    const apiUrl = `${import.meta.env.VITE_API_URL}/api/discs/${userId}/${discId}`;
    console.log("Fetching disc details from:", apiUrl);  // ✅ Debugging log

    try {
        const response = await fetch(apiUrl);

        if (!response.ok) {
            throw new Error(`Error: ${response.statusText}`);
        }

        const data = await response.json();
        console.log("Fetched disc details:", data);  // ✅ Debugging log
        return data;
    } catch (error) {
        console.error("Failed to fetch disc details:", error);
        throw error;
    }
}
