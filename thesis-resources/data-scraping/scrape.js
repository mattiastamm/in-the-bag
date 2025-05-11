import fetch from 'node-fetch';
import { createObjectCsvWriter } from 'csv-writer';

// API Endpoint
const GRAPHQL_URL = 'https://ajmb38a8f1.execute-api.us-east-1.amazonaws.com/pub/graphql';

// CSV Writer Setup
const csvWriter = createObjectCsvWriter({
  path: 'discs.csv',
  header: [
    { id: 'id', title: 'ID' },
    { id: 'name', title: 'Name' },
    { id: 'manufacturer', title: 'Manufacturer' },
    { id: 'speed', title: 'Speed' },
    { id: 'glide', title: 'Glide' },
    { id: 'turn', title: 'Turn' },
    { id: 'fade', title: 'Fade' }
  ]
});

// GraphQL Query (Fetching Only Required Fields)
const GRAPHQL_QUERY = `
  query ($limit: Int!, $offset: Int!, $filters: JSON, $orderBy: String, $orderDir: String) {
    getDiscMatrix(
      limit: $limit
      offset: $offset
      filters: $filters
      order_by: $orderBy
      order_dir: $orderDir
    ) {
      id
      name
      manufacturer
      speed
      glide
      turn
      fade
    }
  }
`;

// Function to Fetch All Discs with Pagination
async function fetchAllDiscs() {
  let allDiscs = [];
  let offset = 0;
  const limit = 40; // Fetch 40 at a time
  let moreData = true;

  while (moreData) {
    console.log(`Fetching discs with offset ${offset}...`);

    const variables = {
      limit,
      offset,
      filters: JSON.stringify({
        primary_use: [],
        stability: { min: -4, max: 7, step: 3 },
        manufacturer: [],
        manufacturer_slug: [],
        speed: { min: 1, max: 15, step: 4 },
        glide: { min: 0, max: 9, step: 2 },
        turn: { min: -5, max: 2, step: 2 },
        fade: { min: 0, max: 6, step: 2 },
        diameter: { min: 21, max: 27.6, step: 12 },
        height: { min: 1.3, max: 3.8, step: 5 },
        rim_depth: { min: 1.1, max: 2.3, step: 2 },
        rim_thickness: { min: 0.3, max: 4.1, step: 7 },
        inside_rim_diameter: { min: 14.5, max: 26.3, step: 20 },
        rim_diameter_ratio: { min: 5, max: 11.8, step: 12 },
        rim_configuration: { min: 2, max: 112.75, step: 185 },
        bead: [],
        include: [],
      }),
      orderBy: "speed",
      orderDir: "ASC",
    };

    try {
      const response = await fetch(GRAPHQL_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ query: GRAPHQL_QUERY, variables }),
      });

      const json = await response.json();

      if (!json.data || !json.data.getDiscMatrix) {
        console.error("\n❌ API did not return expected data.");
        break;
      }

      const discs = json.data.getDiscMatrix;

      if (discs.length > 0) {
        allDiscs = allDiscs.concat(discs);
        offset += limit; // Move to the next batch
      } else {
        moreData = false; // Stop if no more discs
      }

      // Add a delay to avoid hammering the API
      await new Promise(resolve => setTimeout(resolve, 500));

    } catch (error) {
      console.error("\n🚨 Fetch error:", error);
      break;
    }
  }

  console.log(`\n🎯 Successfully fetched ${allDiscs.length} discs.`);
  return allDiscs;
}

// Function to Save Data to CSV
async function saveToCsv(data) {
  if (data.length === 0) {
    console.log("No data to write.");
    return;
  }

  await csvWriter.writeRecords(data);
  console.log('✅ Data successfully saved to data.csv');
}

// Main Execution
(async () => {
  const discData = await fetchAllDiscs();
  await saveToCsv(discData);
})();
