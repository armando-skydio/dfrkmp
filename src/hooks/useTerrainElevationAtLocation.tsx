import { useEffect, useState } from "react";

import { useMapboxElevationProbe } from "@skydio/shared_ui/components/mapbox/MapboxElevationProbe";

type Longitude = number;
type Latitude = number;
type LngLat = [Longitude, Latitude];

/**
 * Hook to get the terrain elevation at a given location using Mapbox.
 * @param location The location to get the elevation for
 * @returns The elevation in meters relative to mean sea level, or null if the elevation could not be retrieved
 */
export const useTerrainElevationAtLocation = (location?: LngLat) => {
  const terrainProbe = useMapboxElevationProbe();
  const [terrainElevation, setTerrainElevation] = useState<number | null>(null);
  useEffect(() => {
    if (location == null || terrainProbe == null) {
      return;
    }
    const [longitude, latitude] = location;

    terrainProbe
      .getElevation(longitude, latitude)
      .then(elevation => {
        setTerrainElevation(elevation);
      })
      .catch(error => {
        console.error("useTerrainElevationAtLocation.terrain_elevation_error", {
          error,
          location,
        });
      });
  }, [location, terrainProbe]);
  return terrainElevation;
};
