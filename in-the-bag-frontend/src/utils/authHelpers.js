export function saveAuthData({ token, userId }) {
    localStorage.setItem("token", token);
    localStorage.setItem("userId", userId);
}

export function isTokenExpired(token) {
    try {
        const payload = JSON.parse(atob(token.split(".")[1]));
        const exp = payload.exp;
        return exp * 1000 < Date.now(); // `exp` is in seconds
    } catch (err) {
        return true;
    }
}
  
export function isAuthenticated() {
    const token = localStorage.getItem("token");
    if (!token) return false;
    return !isTokenExpired(token);
}

export function getToken() {
    return localStorage.getItem("token");
}

export function getUserId() {
    return localStorage.getItem("userId");
}

export function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    window.location.href = "/auth"; // Redirects & reloads
}

export function getAuthHeaders() {
    const token = localStorage.getItem("token");
    console.log("🔍 Token from localStorage:", token);

    return {
        "Content-Type": "application/json",
        ...(token && { Authorization: `Bearer ${token}` }),
    };
}

  
  