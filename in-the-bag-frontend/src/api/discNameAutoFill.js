export async function discNameAutoFill(query) {
    if (!query.trim()) return []; // ✅ Don't search if the input is empty

    try {
        const response = await fetch(
            `${import.meta.env.VITE_API_URL}/api/discs/search?query=${encodeURIComponent(query)}`
        );

        if (!response.ok) {
            throw new Error(`Error: ${response.statusText}`);
        }

        const data = await response.json();
        return data; // ✅ Returns [{ id: 3, name: "Berg" }, { id: 5, name: "Destroyer" }]
    } catch (error) {
        console.error("Failed to fetch discs:", error);
        return [];
    }
}
