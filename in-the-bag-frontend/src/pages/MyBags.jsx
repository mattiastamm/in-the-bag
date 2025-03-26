import { useQuery } from "@tanstack/react-query";
import { fetchBagsWithDiscs } from "../api/fetchBagsWithDiscs";
import { useState } from "react";
import BagDiscList from "../components/BagDiscList";
import { removeDiscFromBag } from "../api/removeDiscFromBag";
import { useEffect } from "react";
import EditBagModal from "../components/EditBagModal";

export default function MyBags() {
  const userId = 1;

  const { data: bags, isLoading, error, refetch } = useQuery({
    queryKey: ["bags", userId],
    queryFn: () => fetchBagsWithDiscs(userId),
  });

  const [selectedBagId, setSelectedBagId] = useState(null);
  const [isAddingBag, setIsAddingBag] = useState(false);
  const [isEditingBag, setIsEditingBag] = useState(false);

  useEffect(() => {
    if (bags?.length > 0 && selectedBagId === null) {
      setSelectedBagId(bags[0].id);
    }
  }, [bags, selectedBagId]);

  const handleRemoveDisc = async (userDiscId, bagId) => {
    const confirmed = window.confirm("Are you sure you want to remove this disc from the bag?");
    if (!confirmed) return;

    const success = await removeDiscFromBag(userDiscId, bagId);
    if (success) {
      refetch(); // 🔄 Refresh bag/disc list
    } else {
      alert("Failed to remove disc from bag.");
    }
  };

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>Error fetching bags</p>;

  return (
    <div className="px-6">
      {/* Sub-Header */}
      <div className="flex justify-between items-center mt-3 mb-9">
        {/* Left: My Bags Title */}
        <div className="w-[15%]">
          <h1 className="text-3xl font-bold">My Bags</h1>
        </div>

        {/* Middle: Bag Buttons or Empty State Message */}
        <div className="w-[70%] flex items-center justify-start">
          {bags.length > 0 ? (
            <div className="flex flex-wrap gap-4">
              {bags.map((bag) => (
                <button
                  key={bag.id}
                  onClick={() => setSelectedBagId(bag.id)}
                  className={`bg-blue-500 text-white text-2xl px-4 py-2 rounded hover:bg-blue-600 cursor-pointer ${
                    selectedBagId === bag.id ? "ring-4 ring-blue-300" : ""
                  }`}
                >
                  {bag.title}
                </button>
              ))}
            </div>
          ) : (
            <p className="text-gray-600 text-lg italic">
              No bags found. To create your first bag, press the <span className="font-semibold">Add Bag</span> button.
            </p>
          )}
        </div>

        {/* Right: Add Bag Button */}
        <div className="w-[15%] flex justify-end">
          <button
            onClick={() => setIsAddingBag(true)}
            className="bg-green-500 text-white text-2xl px-4 py-2 rounded transition-transform transform hover:bg-green-700 cursor-pointer"
          >
            + Add Bag
          </button>
        </div>
      </div>

      {/* Split Layout: Left = Disc List, Right = Future Content */}
      <div className="flex gap-6">
        {/* Left Half */}
        <div className="w-1/2">
          {selectedBagId && (
            <BagDiscList
              discs={bags.find((b) => b.id === selectedBagId)?.discs || []}
              bagId={selectedBagId}
              onRemoveDisc={handleRemoveDisc}
              onEditBag={() => setIsEditingBag(true)}
            />
          )}
        </div>

        {/* Right Half - Placeholder */}
        <div className="w-1/2 border-l border-gray-300 p-4 text-gray-500 text-lg">
          {selectedBagId ? (
            <p>Select a disc to view details or perform actions.</p>
          ) : (
            <p>Select a bag to view its contents.</p>
          )}
        </div>
      </div>

      {isEditingBag && (
        <EditBagModal
          bagId={selectedBagId}
          initialSelectedDiscIds={bags.find(b => b.id === selectedBagId)?.discs.map(d => d.userDiscId) || []}
          onClose={() => setIsEditingBag(false)}
          refetch={refetch}
        />
      )}
    </div>
  );
}
