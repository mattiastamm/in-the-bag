import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function getDiscs(userId) {
    const apiUrl = `${import.meta.env.VITE_API_URL}/api/user-discs/${userId}`;
    console.log("Fetching discs from:", apiUrl);

    try {
        const response = await fetchWithAuth(apiUrl, {
            method: "GET",
        });

        if (!response || !response.ok) {
            throw new Error(`Error: ${response?.statusText || "Unknown error"}`);
        }

        const data = await response.json();
        console.log("Fetched data:", data);
        return data;
    } catch (error) {
        console.error("Failed to fetch discs:", error);
        throw error;
    }
}
