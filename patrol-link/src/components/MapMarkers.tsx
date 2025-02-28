"use client";

import React from "react";
import { useMarkerStore } from "../store/markerStore";
import CustomMarkerSprite from "./CustomMarkerSprite";

interface MapMarkersProps {
  // Add any additional props here if needed
}

const MapMarkers: React.FC<MapMarkersProps> = () => {
  const markers = useMarkerStore((state) => state.markers);

  return (
    <>
      {markers.map((marker) => (
        <CustomMarkerSprite
          key={marker.id}
          id={marker.id}
          location={{ longitude: marker.longitude, latitude: marker.latitude }}
          scale={700}
        />
      ))}
    </>
  );
};

export default MapMarkers;
