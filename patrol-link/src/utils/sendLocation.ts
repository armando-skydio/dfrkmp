import type { Position } from "@capacitor/geolocation";

const LOCATION_MARKER_UUID = "bbe51199-c1cf-44ac-8496-95eb9b2145f8";

export const sendLocation = async (location: Position | null, requestSupport: boolean = false) => {
  if (!location) return;
  const options = {
    method: "POST",
    headers: {
      accept: "application/json",
      "content-type": "application/json",
      Authorization: "24aff6281af2ef7d24a314c0b3e542ed6d3428c73713d3d59e8dc9d9f669f41b",
    },
    body: JSON.stringify({
      marker_details: {
        baro_pressure: 1.0,
        number_satellites: 1,
        distance_accuracy: 1.0,
        speed_accuracy: 1.0,
        requesting_support: requestSupport,
      },
      type: "PATROL_LINK",
      description: "test",
      event_time: "2022-05-03T03:10:52.503+00:00",
      latitude: location.coords.latitude,
      longitude: location.coords.longitude,
      altitude: location.coords.altitude,
      uuid: LOCATION_MARKER_UUID,
    }),
  };

  try {
    const response = await fetch(
      `${process?.env?.NEXT_PUBLIC_API_URL ?? ""}/api/v0/marker`,
      options
    );
    const data = await response.json();
    console.log("Location sent successfully:", data);
  } catch (error) {
    console.error("Error sending location:", error);
  }
};
