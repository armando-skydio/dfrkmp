"use client";

import React, { useMemo } from "react";
import * as THREE from "three";

import { Coordinates } from "@skydio/nomad";

import { useTerrainElevationAtLocation } from "hooks/useTerrainElevationAtLocation";

interface UserSpriteProps {
  location: { longitude: number; latitude: number };
  scale?: number;
}

const UserSprite: React.FC<UserSpriteProps> = ({ location, scale = 1 }) => {
  const spriteMaterial = useMemo(() => {
    // Create a circular sprite texture
    const canvas = document.createElement("canvas");
    canvas.width = 64;
    canvas.height = 64;
    const context = canvas.getContext("2d");

    if (context) {
      // Draw a filled circle
      context.beginPath();
      context.arc(32, 32, 28, 0, 2 * Math.PI);
      context.fillStyle = "#0066ff"; // Blue color
      context.fill();

      // Add a slight white glow
      context.strokeStyle = "rgba(255, 255, 255, 0.5)";
      context.lineWidth = 4;
      context.stroke();
    }

    const texture = new THREE.CanvasTexture(canvas);
    return new THREE.SpriteMaterial({
      map: texture,
      transparent: true,
      depthTest: false,
    });
  }, []);

  const terrainElevation = useTerrainElevationAtLocation([location.longitude, location.latitude]);
  console.log("DEBUG: terrainElevation", terrainElevation);
  return (
    <Coordinates latitude={location.latitude} longitude={location.longitude}>
      <sprite
        material={spriteMaterial}
        position={[0, 0, terrainElevation ?? 0]}
        scale={[scale, scale, scale]}
      />
    </Coordinates>
  );
};

export default UserSprite;
