import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { getUserProfile } from "../api/getUserProfile";
import DiscTypePieChart from "../components/DiscTypePieChart";
import ChangePasswordModal from "../components/ChangePasswordModal";
import DeleteAccountModal from "../components/DeleteAccountModal";

export default function Profile() {
  const { data: profile, isLoading, error } = useQuery({
    queryKey: ["userProfile"],
    queryFn: getUserProfile,
  });

  const [showChangePassword, setShowChangePassword] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);

  if (isLoading)
    return <p className="text-center text-gray-500">Loading profile...</p>;
  if (error)
    return <p className="text-center text-red-500">Failed to load profile</p>;

  const discTypeData = [
    { name: "Putters", value: profile.putters },
    { name: "Midranges", value: profile.midranges },
    { name: "Fairways", value: profile.fairways },
    { name: "Drivers", value: profile.drivers },
  ].filter((entry) => entry.value > 0); // 👈 Filter out 0% entries

  return (
    <div className="max-w-4xl mx-auto p-6 mt-20">
      <h1 className="text-3xl font-bold text-center mb-8">Your Profile</h1>

      <div className="bg-white rounded-lg shadow-md p-6 grid md:grid-cols-2 gap-6">
        {/* User info */}
        <div className="h-full flex flex-col justify-center items-center space-y-4 text-center">
          <p className="text-lg">
            <strong>Email:</strong> {profile.email}
          </p>
          <p className="text-lg">
            <strong>Member since:</strong> {new Date(profile.createdAt).toLocaleDateString()}
          </p>
          <p className="text-lg">
            <strong>Total Discs:</strong> {profile.totalDiscs}
          </p>
          <p className="text-lg">
            <strong>Total Bags:</strong> {profile.totalBags}
          </p>
          <p className="text-lg">
            <strong>Discs in Bags:</strong> {profile.discsInUse}
          </p>
        </div>

        {/* Chart */}
        <DiscTypePieChart data={discTypeData} />
      </div>

      <div className="mt-6 grid grid-cols-2 gap-4">
        <button
          className="w-full bg-yellow-500 hover:bg-yellow-600 text-white text-lg py-4 px-4 rounded cursor-pointer"
          onClick={() => setShowChangePassword(true)}
        >
          Change Password
        </button>
        <button
          className="w-full bg-red-600 hover:bg-red-700 text-white text-lg py-4 px-4 rounded cursor-pointer"
          onClick={() => setShowDeleteModal(true)}
        >
          Delete Account
        </button>
      </div>

      {showChangePassword && (
        <ChangePasswordModal onClose={() => setShowChangePassword(false)} />
      )}
      {showDeleteModal && (
        <DeleteAccountModal onClose={() => setShowDeleteModal(false)} />
      )}
    </div>
  );
}