import { getAuthHeaders } from "../utils/authHelpers";

export async function discNameAutoFill(query) {
    if (!query.trim()) return [];

    try {
        const response = await fetch(
            `${import.meta.env.VITE_API_URL}/api/discs/search?query=${encodeURIComponent(query)}`,
            {
                method: "GET",
                headers: getAuthHeaders(),
            }
        );

        if (!response.ok) {
            throw new Error(`Error: ${response.statusText}`);
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Failed to fetch discs:", error);
        return [];
    }
}
