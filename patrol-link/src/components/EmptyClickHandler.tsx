"use client";

import { useCallback  } from "react";
import { Vector2 } from "three";

import {
  raycastMapboxSceneFromPointer,
  ThreeClickInterceptor,
  threeClickInterceptorPlaneObject3DName,
  useNomadStore,
} from "@skydio/nomad";

import type { ThreeEvent } from "@react-three/fiber";
import type { Map } from "mapbox-gl-new";
import { useMarkerStore } from "../store/markerStore";
import { useLongPress } from "../hooks/useLongPress";

const LONG_PRESS_THRESHOLD_MS = 500;

function getMapIntersection(e: ThreeEvent<MouseEvent>, map?: Map) {
  // If map is not ready or there are intersections with other objects, return
  if (
    !map ||
    e.intersections.length !== 1 ||
    e.intersections[0].object.name !== threeClickInterceptorPlaneObject3DName
  ) {
    return null;
  }

  // Check we intersected with the terrain
  return raycastMapboxSceneFromPointer({
    pointer: new Vector2(e.nativeEvent.offsetX, e.nativeEvent.offsetY),
    mapboxMap: map,
  });
}

/**
 * A click handler that reset the selected entity when clicking on an empty space and creates
 * a fly to POI point when right clicking.
 */
export const EmptyClickHandler = () => {
  const map = useNomadStore((state) => state.mapboxMap);
  const addMarker = useMarkerStore((state) => state.addMarker);
  const selectMarker = useMarkerStore((state) => state.selectMarker);

  const handleLongPress = useCallback((e: ThreeEvent<MouseEvent>) => {
    const intersection = getMapIntersection(e, map ?? undefined);
    if (intersection) {
      const location = {
        lat: intersection[0].lat,
        lng: intersection[0].lng
      };
      console.log("Long press detected at:", intersection[0]);
      
      // Add marker to the store
      addMarker(location.lat, location.lng, "New marker");
    }
  }, [map, addMarker]);

  const handleClick = useCallback((e: ThreeEvent<MouseEvent>) => {
    // Regular click handling if needed
    console.log("Regular click detected");
    selectMarker(null);
  }, []);

  const longPressHandlers = useLongPress<ThreeEvent<MouseEvent>>({
    onLongPress: handleLongPress,
    onClick: handleClick,
    longPressThreshold: LONG_PRESS_THRESHOLD_MS,
  });

  return <ThreeClickInterceptor 
    onPointerDown={longPressHandlers.onMouseDown}
    onPointerMove={longPressHandlers.onMouseMove}
    onPointerUp={longPressHandlers.onMouseUp}
    onPointerLeave={longPressHandlers.onMouseLeave}
    onClick={longPressHandlers.onClick}
  />;
};
