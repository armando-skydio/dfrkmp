import { v4 as uuidv4 } from "uuid";
import { create } from "zustand";
import { persist } from "zustand/middleware";

export interface Marker {
  id: string;
  latitude: number;
  longitude: number;
  description: string;
  createdAt: string;
  selected: boolean;
}

interface MarkerState {
  markers: Marker[];
  addMarker: (latitude: number, longitude: number, description: string) => void;
  updateMarkerDescription: (id: string, description: string) => void;
  removeMarker: (id: string) => void;
  clearMarkers: () => void;
  selectMarker: (id: string | null) => void;
}

export const useMarkerStore = create<MarkerState>()(
  persist(
    set => ({
      markers: [],

      addMarker: (latitude, longitude, description) => {
        // Create a custom marker via the API
        const options = {
          method: "POST",
          headers: {
            accept: "application/json",
            "content-type": "application/json",
            // Authorization: process?.env?.NEXT_MARKER_API_TOKEN ?? "",
            Authorization: "24aff6281af2ef7d24a314c0b3e542ed6d3428c73713d3d59e8dc9d9f669f41b",
          },
          body: JSON.stringify({
            marker_details: {
              createdByName: "Jean-Michel",
              description: description,
              latitude: latitude,
              longitude: longitude,
            },
            type: "CUSTOM",
            description: description,
            event_time: new Date().toISOString(),
            latitude: latitude,
            longitude: longitude,
          }),
        };

        fetch(`${process?.env?.NEXT_PUBLIC_API_URL ?? ""}/api/v0/marker`, options)
          .then(res => res.json())
          .then(res => {
            set(state => ({
              markers: [
                ...state.markers.map(m => ({ ...m, selected: false })),
                {
                  id: uuidv4(),
                  latitude,
                  longitude,
                  description,
                  createdAt: new Date().toISOString(),
                  selected: true,
                },
              ],
            }));
          })
          .catch(err => console.error(err));
      },

      updateMarkerDescription: (id, description) => {
        set(state => ({
          markers: state.markers.map(marker =>
            marker.id === id ? { ...marker, description } : marker
          ),
        }));
      },

      removeMarker: id => {
        set(state => ({
          markers: state.markers.filter(marker => marker.id !== id),
        }));
      },

      clearMarkers: () => {
        set({ markers: [] });
      },

      selectMarker: id => {
        set(state => ({
          markers: state.markers.map(marker =>
            marker.id === id ? { ...marker, selected: true } : { ...marker, selected: false }
          ),
        }));
      },
    }),
    {
      name: "marker-storage", // name of the item in the storage (must be unique)
      // Optional: specify storage method
      // Default is localStorage
    }
  )
);
