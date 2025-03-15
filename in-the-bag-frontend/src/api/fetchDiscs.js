export async function getDiscs() {
    console.log("Fetching discs from:", import.meta.env.VITE_API_URL);  // ✅ Debugging log

    try {
        const response = await fetch(`${import.meta.env.VITE_API_URL}/api/discs`);

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
