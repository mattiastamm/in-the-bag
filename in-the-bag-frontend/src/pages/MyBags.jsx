import { useQuery } from "@tanstack/react-query";
import { getBagsWithDiscs } from "../api/getBagsWithDiscs";
import { useState } from "react";
import BagDiscList from "../components/BagDiscList";
import { removeDiscFromBag } from "../api/removeDiscFromBag";
import { useEffect } from "react";
import EditBagModal from "../components/EditBagModal";
import AddNewBagModal from "../components/AddNewBagModal";
import { deleteBag } from "../api/deleteBag";
import StabilityChart from "../components/StabilityChart";


export default function MyBags() {
  const { data: bags, isLoading, error, refetch } = useQuery({
    queryKey: ["bags"],
    queryFn: () => getBagsWithDiscs(),
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

  const handleDeleteBag = async () => {
    const confirmed = window.confirm("Are you sure you want to delete this bag?");
    if (!confirmed) return;
  
    const success = await deleteBag(selectedBagId);
    if (success) {
      const { data: updatedBags } = await refetch(); // ✅ bags is automatically updated
  
      // 🔁 Update selected bag after deletion
      if (updatedBags.length > 0) {
        setSelectedBagId(updatedBags[0].id);
      } else {
        setSelectedBagId(null);
      }
    } else {
      alert("Failed to delete the bag.");
    }
  };

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>Error fetching bags</p>;

  return (
    <div className="min-h-screen flex flex-col">
      {/* Sub-Header */}
      <div className="flex flex-wrap items-center justify-between gap-4 mt-3 pb-8">
        
        {/* Left: My Bags Title */}
        <div className="flex-shrink-0">
          <h1 className="text-3xl font-bold pl-1">My Bags</h1>
        </div>

        {/* Middle: Bag Buttons */}
        <div className="flex-1 flex flex-wrap gap-4 mx-4">
          {bags.map((bag) => (
            <button
              key={bag.id}
              onClick={() => setSelectedBagId(bag.id)}
              className={`bg-blue-500 text-white text-xl px-4 py-2 rounded hover:bg-blue-600 cursor-pointer ${
                selectedBagId === bag.id ? "ring-2 ring-blue-300" : ""
              }`}
            >
              {bag.title}
            </button>
          ))}
        </div>

        {/* Right: Add Bag Button */}
        <div className="flex-shrink-0">
          <button
            onClick={() => {
              if (bags.length >= 5) {
                alert("You can only have up to 5 bags.");
                return;
              }
              setIsAddingBag(true);
            }}
            className="bg-green-500 text-white text-2xl px-4 py-2 rounded transition-transform transform hover:bg-green-700 cursor-pointer"
          >
            + Add Bag
          </button>
        </div>
      </div>

      {/* Divider Line */}
      <div className="-mx-6 border-b-3 border-gray-900" />

      {/* Split Layout: Left = Disc List, Right = Future Content */}
      <div className="flex flex-col lg:flex-row flex-1 gap-6 -ml-6">
        {/* Left Half - becomes full width on smaller screens */}
        <div className="w-full lg:w-1/2 overflow-y-auto max-h-[90vh] pr-2 -pl-6">
          {bags.length === 0 ? (
            <div className="flex items-center justify-center h-full">
              <p className="text-gray-600 text-lg italic text-center px-4">
                No bags found. To create your first bag, press the{" "}
                <span className="font-semibold">Add Bag</span> button.
              </p>
            </div>
          ) : (
            selectedBagId && (
              <BagDiscList
                discs={bags.find((b) => b.id === selectedBagId)?.discs || []}
                bagId={selectedBagId}
                onRemoveDisc={handleRemoveDisc}
                onEditBag={() => setIsEditingBag(true)}
                onDeleteBag={handleDeleteBag}
              />
            )
          )}
        </div>

        {/* Right Half - becomes full width and moves below on smaller screens */}
        <div className="w-full lg:w-1/2 border-t lg:border-t-0 lg:border-l-3 flex flex-col justify-start border-gray-900 p-4 text-gray-500 text-lg">
          <StabilityChart
            discs={bags.find((b) => b.id === selectedBagId)?.discs || []}
          />
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

      {isAddingBag && (
        <AddNewBagModal
          onClose={() => setIsAddingBag(false)}
          refetch={refetch}
        />
      )}

    </div>
  );
}
