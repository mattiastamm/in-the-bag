export async function discCreationAutoFill(discId) {
    try {
        const response = await fetch(
            `${import.meta.env.VITE_API_URL}/api/discs/${discId}/details`
        );

        if (!response.ok) {
            throw new Error(`Error: ${response.statusText}`);
        }

        const data = await response.json();
        return data; // ✅ Returns { id, name, type, manufacturer, speed, glide, turn, fade, availablePlastics }
    } catch (error) {
        console.error("Failed to fetch disc details:", error);
        return null;
    }
}
