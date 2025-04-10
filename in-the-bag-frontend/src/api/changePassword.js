import { fetchWithAuth } from "../utils/fetchWithAuth";

export async function changePassword({ currentPassword, newPassword }) {
  const response = await fetchWithAuth(
    `${import.meta.env.VITE_API_URL}/api/users/change-password`,
    {
      method: "PATCH",
      body: JSON.stringify({ currentPassword, newPassword }),
    }
  );

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || "Change password failed");
  }

  return true;
}
