// src/components/DiscTypePieChart.jsx
import { PieChart, Pie, Cell, ResponsiveContainer } from "recharts";

const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042"];
const RADIAN = Math.PI / 180;

const renderCustomizedLabel = ({
  cx,
  cy,
  midAngle,
  innerRadius,
  outerRadius,
  percent,
}) => {
  const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
  const x = cx + radius * Math.cos(-midAngle * RADIAN);
  const y = cy + radius * Math.sin(-midAngle * RADIAN);

  return (
    <text
      x={x}
      y={y}
      fill="white"
      textAnchor="middle"
      dominantBaseline="central"
      fontWeight="bold"
    >
      {`${(percent * 100).toFixed(0)}%`}
    </text>
  );
};

export default function DiscTypePieChart({ data }) {
  return (
    <div className="flex flex-col items-center">
        <h2 className="text-lg font-semibold mb-2">Discs by Category</h2>
        <ResponsiveContainer width="100%" height={220}>
            <PieChart>
            <Pie
                data={data}
                cx="50%"
                cy="50%"
                outerRadius={80}
                labelLine={false}
                label={renderCustomizedLabel}
                dataKey="value"
            >
                {data.map((entry, index) => (
                <Cell
                    key={`cell-${index}`}
                    fill={COLORS[index % COLORS.length]}
                />
                ))}
            </Pie>
            </PieChart>
        </ResponsiveContainer>

        {/* 👇 Reduce margin and gap */}
        <ul className="mt-2 flex flex-wrap justify-center gap-3">
            {data.map((entry, index) => (
            <li
                key={index}
                className="flex items-center gap-2 text-sm text-gray-700"
            >
                <span
                className="inline-block w-3 h-3 rounded-full"
                style={{ backgroundColor: COLORS[index % COLORS.length] }}
                />
                {entry.name}
            </li>
            ))}
        </ul>
    </div>
  );
}
