import { logout } from "./authHelpers";

export async function fetchWithAuth(url, options = {}) {
  const token = localStorage.getItem("token");

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

    // Handle 401 errors by analyzing the error type
    if (response.status === 401) {
      const contentType = response.headers.get("content-type");

      // Try to parse the JSON body if possible
      if (contentType && contentType.includes("application/json")) {
        const errorData = await response.json();

        // ONLY log the user out if token is expired or invalid
        if (errorData.error === "TokenExpired" || errorData.error === "InvalidToken") {
          alert(errorData.message || "Session expired");
          logout();
          window.location.href = "/auth";
          return;
        } else {
          // Let the caller handle normal 401s (e.g. wrong password)
          throw new Error(errorData.message || "Unauthorized");
        }
      } else {
        throw new Error("Unauthorized access");
      }
    }

    return response;
  } catch (error) {
    console.error("Fetch error:", error);
    throw error;
  }
}
