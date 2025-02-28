"use client";

import React, { useMemo } from "react";
import * as THREE from "three";

import { Coordinates, ZoomInvariantGroup } from "@skydio/nomad";

import { useTerrainElevationAtLocation } from "hooks/useTerrainElevationAtLocation";
import { useMarkerStore } from "../store/markerStore";

// Import the marker images
import customMarkerImage from "../assets/imgs/custom_marker.png";
import customMarkerSelectedImage from "../assets/imgs/custom_marker_selected.png";

interface CustomMarkerSpriteProps {
  location: { longitude: number; latitude: number };
  scale?: number;
  id?: string;
}

const CustomMarkerSprite: React.FC<CustomMarkerSpriteProps> = ({ 
  location, 
  scale = 1, 
  id 
}) => {
  const selectMarker = useMarkerStore((state) => state.selectMarker);
  const markers = useMarkerStore((state) => state.markers);
  
  // Determine if this marker is selected
  const isSelected = id ? markers.some(marker => marker.id === id && marker.selected) : false;
  const dilatedScale = isSelected? scale * 1.3 : scale;

  const spriteMaterial = useMemo(() => {
    // Use the appropriate image based on selection state
    const textureLoader = new THREE.TextureLoader();
    const texture = textureLoader.load(isSelected ? customMarkerSelectedImage.src : customMarkerImage.src);
    
    return new THREE.SpriteMaterial({
      map: texture,
      transparent: true,
      depthTest: false,
    });
  }, [isSelected]);

  const terrainElevation = useTerrainElevationAtLocation([location.longitude, location.latitude]);

  const handleClick = () => {
    if (id) {
      if(isSelected) {
        selectMarker(null);
      } else {
        selectMarker(id);
      }
    }
  };

  return (
    <Coordinates latitude={location.latitude} longitude={location.longitude}>
      <ZoomInvariantGroup>
        <sprite
          material={spriteMaterial}
          position={[0, 0, terrainElevation ?? 0]}
          scale={[dilatedScale, dilatedScale, dilatedScale]}
          onClick={handleClick}
        />
      </ZoomInvariantGroup>
    </Coordinates>
  );
};

export default CustomMarkerSprite;
