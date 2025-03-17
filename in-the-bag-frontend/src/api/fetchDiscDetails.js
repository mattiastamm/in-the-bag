export async function getDiscDetails(userDiscId) {
    const apiUrl = `${import.meta.env.VITE_API_URL}/api/user-discs/details/${userDiscId}`;
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
