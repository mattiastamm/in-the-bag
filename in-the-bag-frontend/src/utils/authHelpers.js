// ✅ Save only the JWT token
export function saveAuthData({ token }) {
    localStorage.setItem("token", token);
}

// ✅ Get the raw JWT token
export function getToken() {
    return localStorage.getItem("token");
}

// ✅ Logout function clears token only
export function logout() {
    localStorage.removeItem("token");
    window.location.href = "/auth";
}

// ✅ Headers with token (used in fetch requests)
export function getAuthHeaders() {
    const token = getToken();
    return {
        "Content-Type": "application/json",
        ...(token && { Authorization: `Bearer ${token}` }),
    };
}

export function isAuthenticated() {
    const token = localStorage.getItem("token");
  
    if (!token) return false;
  
    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      const exp = payload.exp;
      const isExpired = exp * 1000 < Date.now(); // `exp` is in seconds
  
      return !isExpired;
    } catch (err) {
      console.error("Failed to parse token:", err);
      return false;
    }
}
  