export default function WishlistCard({
  name,
  manufacturer,
  speed,
  glide,
  turn,
  fade,
  stability,
  category,
  onAdd,
  onRemove,
}) {
  return (
    <div className="w-full border-2 bg-gray-700 border-gray-600 shadow-md rounded-lg h-full flex flex-col justify-between transition-transform transform hover:scale-105 hover:shadow-lg hover:border-gray-500 hover:bg-gray-600 pb-3 pt-2">
      
      {/* Manufacturer & Name */}
      <div className="text-center mb-3">
        <p className="text-lg text-gray-300">{manufacturer}</p>
        <h3 className="text-2xl font-bold">{name}</h3>
      </div>

      {/* Bigger disc visual */}
      <div className="flex-grow flex items-center justify-center mb-2">
        <div
          className="w-full aspect-square rounded-full border border-gray-300 max-w-[10rem]"
          style={{ backgroundColor: "#89CFF0" }}
        />
      </div>

      {/* Stability + Flight Numbers */}
      <div className="text-center mb-4">
        <div className="text-base mb-2 text-white">
          {speed} | {glide} | {turn} | {fade}
        </div>
        <div className="text-sm font-medium text-gray-300 mb-1">
          <p> Category: {category} </p>
          <p> Stability: {stability} </p>
        </div>
      </div>

      {/* Horizontal buttons */}
      <div className="flex gap-2 justify-center">
        <button
          onClick={onAdd}
          className="w-32 bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700 transition text-sm cursor-pointer"
        >
          Add to Inv
        </button>
        <button
          onClick={onRemove}
          className="w-32 bg-red-500 text-white py-2 px-4 rounded hover:bg-red-600 transition text-sm cursor-pointer"
        >
          Remove
        </button>
      </div>
    </div>
  );
}
