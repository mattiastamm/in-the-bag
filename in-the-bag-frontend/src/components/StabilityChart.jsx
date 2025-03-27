import React from "react";
import {
  ScatterChart,
  CartesianGrid,
  XAxis,
  YAxis,
  Scatter,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

export default function StabilityChart({ discs }) {
  // Group by unique (stability, speed)
  const grouped = {};

  discs.forEach((disc) => {
    const stability = disc.customTurn + disc.customFade;
    const speed = disc.customSpeed;
    const key = `${stability}-${speed}`;

    if (!grouped[key]) {
      grouped[key] = {
        x: stability,
        y: speed,
        discs: [],
      };
    }

    grouped[key].discs.push({
      name: disc.name,
      color: disc.color,
    });
  });

  const chartData = Object.values(grouped);

  return (
    <div className="w-full flex-1 bg-gray-900 rounded-lg p-4">
      <ResponsiveContainer width="100%" height="100%">
        <ScatterChart margin={{ top: 20, right: 20, bottom: 20, left: 20 }}>
          <CartesianGrid stroke="#444" strokeDasharray="3 3" />

          <XAxis
            type="number"
            dataKey="x"
            domain={[-5, 6]}
            tickCount={12}
            label={{ value: "Stability (turn + fade)", position: "bottom", fill: "#ccc" }}
            stroke="#ccc"
          />

          <YAxis
            type="number"
            dataKey="y"
            domain={[0, 14]}
            ticks={[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14]}
            interval={0}
            label={{ value: "Speed", angle: -90, position: "insideLeft", fill: "#ccc" }}
            stroke="#ccc"
          />

          <Tooltip
            content={({ active, payload }) => {
              if (active && payload?.length) {
                const discList = payload[0].payload.discs;
                return (
                  <div className="bg-white p-2 border rounded shadow text-sm">
                    <ul className="list-disc ml-4">
                      {discList.map((d, i) => (
                        <li key={i}>{d.name}</li>
                      ))}
                    </ul>
                  </div>
                );
              }
              return null;
            }}
          />

          <Scatter
            data={chartData}
            shape={({ cx, cy, payload }) => {
              if (payload.discs.length === 1) {
                return (
                  <circle
                    cx={cx}
                    cy={cy}
                    r={6}
                    fill={payload.discs[0].color}
                    stroke="#222"
                    strokeWidth={1}
                  />
                );
              } else {
                return (
                  <g>
                    <circle cx={cx} cy={cy} r={10} fill="white" stroke="#333" strokeWidth={1} />
                    <text
                      x={cx}
                      y={cy + 4}
                      textAnchor="middle"
                      fontSize="10"
                      fill="black"
                      fontWeight="bold"
                    >
                      {payload.discs.length}
                    </text>
                  </g>
                );
              }
            }}
          />
        </ScatterChart>
      </ResponsiveContainer>
    </div>
  );
}
