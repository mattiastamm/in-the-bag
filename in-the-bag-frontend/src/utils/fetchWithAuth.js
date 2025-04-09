import { logout } from "./authHelpers";

export async function fetchWithAuth(url, options = {}) {
  const token = localStorage.getItem("token");

  // Set default headers
  const headers = {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
    ...options.headers,
  };

  try {
    const response = await fetch(url, {
      ...options,
      headers,
    });

    if (response.status === 401) {
      // Optional: Parse response for more detailed message
      const errorText = await response.text();
      if (errorText.includes("expired")) {
        alert("Your session has expired. Please log in again.");
      } else {
        alert("You are not authorized. Please log in.");
      }

      logout(); // Clear localStorage
      window.location.href = "/auth"; // Redirect to login
      return; // Stop execution
    }

    return response;
  } catch (error) {
    console.error("Network error:", error);
    throw error;
  }
}
