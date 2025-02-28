"use client";

import React, { useMemo } from "react";
import * as THREE from "three";

import { Coordinates, ZoomInvariantGroup } from "@skydio/nomad";

import { useTerrainElevationAtLocation } from "hooks/useTerrainElevationAtLocation";

interface UserSpriteProps {
  location: { longitude: number; latitude: number };
  scale?: number;
}

const UserSprite: React.FC<UserSpriteProps> = ({ location, scale = 1 }) => {
  const spriteMaterial = useMemo(() => {
    // Create a high-resolution sprite texture
    const canvas = document.createElement("canvas");
    canvas.width = 128;
    canvas.height = 128;
    const context = canvas.getContext("2d");

    if (context) {
      // Draw a filled circle at the center
      const center = canvas.width / 2;
      const radius = canvas.width * 0.45;
      context.beginPath();
      context.arc(center, center, radius, 0, 2 * Math.PI);
      context.fillStyle = "#003bff"; // Blue color
      context.fill();

      // Add a slight white glow
      context.strokeStyle = "rgba(255, 255, 255, 0.5)";
      context.lineWidth = canvas.width * 0.05;
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

  return (
    <Coordinates latitude={location.latitude} longitude={location.longitude}>
      <ZoomInvariantGroup>
        <sprite
          material={spriteMaterial}
          position={[0, 0, terrainElevation ?? 0]}
          scale={[scale, scale, scale]}
        />
      </ZoomInvariantGroup>
    </Coordinates>
  );
};

export default UserSprite;
