export async function updateDisc(userId, discId, updatedData) {
    const apiUrl = `${import.meta.env.VITE_API_URL}/api/discs/${userId}/${discId}`;
    console.log("Updating disc at:", apiUrl); // ✅ Debugging log

    try {
        const response = await fetch(apiUrl, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(updatedData),
        });

        if (response.status === 204) {
            console.log("Disc updated successfully!"); // ✅ Debugging log
            return true; // ✅ Successful update
        } else if (response.status === 404) {
            console.error("Error: Disc not found or does not belong to the user.");
            return false; // ❌ Disc not found
        } else {
            throw new Error(`Unexpected error: ${response.statusText}`);
        }
    } catch (error) {
        console.error("Failed to update disc:", error);
        return false; // ❌ Return false if there's an error
    }
}
