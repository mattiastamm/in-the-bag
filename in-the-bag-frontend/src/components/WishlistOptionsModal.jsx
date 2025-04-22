import React, { useState } from "react";
import AddNewUserDiscModal from "./AddNewUserDiscModal";
import { removeFromWishlist } from "../api/removeFromWishlist";

export default function WishlistOptionsModal({ disc, onClose, refetch }) {
  const [showTransferModal, setShowTransferModal] = useState(false);

  const handleAddToInventory = () => {
    setShowTransferModal(true);
  };

  const handleCloseTransfer = async (wasDiscAdded = false) => {
    if (wasDiscAdded) {
      try {
        // Only remove from wishlist if disc was added to inventory
        const success = await removeFromWishlist(disc.id);
        if (!success) {
          console.error("Failed to remove disc from wishlist after transfer");
        }
        await refetch(); // Refresh wishlist
      } catch (error) {
        console.error("Error removing from wishlist after transfer:", error);
      }
    }
    
    setShowTransferModal(false);
    onClose(); // Close options modal after transfer
  };

  const handleDelete = async () => {
    const confirmed = window.confirm(`Are you sure you want to delete ${disc.name} from your wishlist?`);
    if (!confirmed) return;

    try {
      const success = await removeFromWishlist(disc.id);
      if (success) {
        await refetch(); // Refresh wishlist
        onClose(); // Close modal
      } else {
        alert("Failed to delete disc.");
      }
    } catch (error) {
      console.error("Delete error:", error);
      alert("An error occurred while deleting the disc.");
    }
  };

  return (
    <>
      {!showTransferModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm p-4">
          <div className="bg-white rounded-lg p-6 max-w-sm w-full shadow-lg">
            <h2 className="text-xl font-bold mb-4 text-center">Wishlist Options</h2>

            <div className="space-y-4">
              <button
                onClick={handleAddToInventory}
                className="w-full bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded cursor-pointer"
              >
                Add to Inventory
              </button>

              <button
                onClick={handleDelete}
                className="w-full bg-red-600 hover:bg-red-700 text-white py-2 rounded cursor-pointer"
              >
                Delete Disc
              </button>

              <button
                onClick={() => onClose()}
                className="w-full bg-gray-300 hover:bg-gray-400 text-gray-800 py-2 px-4 rounded cursor-pointer"
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}

      {showTransferModal && (
        <AddNewUserDiscModal
          preSelectedDiscId={disc.id}
          onClose={(wasAdded = false) => handleCloseTransfer(wasAdded)}
          refetch={refetch}
        />
      )}
    </>
  );
}
