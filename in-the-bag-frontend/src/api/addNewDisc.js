import { getAuthHeaders } from "../utils/authHelpers";

export async function addNewDisc(discData) {
    const apiUrl = `${import.meta.env.VITE_API_URL}/api/user-discs`;
    console.log("Adding new disc with data:", discData);

    try {
        const response = await fetch(apiUrl, {
            method: "POST",
            headers: getAuthHeaders(),
            body: JSON.stringify(discData),
        });

        if (!response.ok) {
            throw new Error(`Error: ${response.statusText}`);
        }

        console.log("New disc added successfully!");
        return true;
    } catch (error) {
        console.error("Failed to add new disc:", error);
        return false;
    }
}
