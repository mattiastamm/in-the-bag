import { Loader2 } from "lucide-react"; // Or any spinner icon
import React from "react";

export default function LoadingModal() {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/70 backdrop-blur-sm p-4">
      <div className="bg-gray-700 rounded-lg p-6 max-w-md w-full shadow-lg text-center">
        <Loader2 className="animate-spin mx-auto mb-4 text-blue-600" size={48} />
        <h2 className="text-xl font-semibold text-white">
          Analyzing your bag...
        </h2>
        <p className="text-sm text-white mt-2">
          Finding the perfect additions to complete your setup.
        </p>
      </div>
    </div>
  );
}
