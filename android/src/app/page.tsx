"use client";

import { useEffect, useRef, useState } from "react";

import { NomadMap, useNomadStore } from "@skydio/nomad";

import { DetailModal } from "components/DetailModal";
import { EmptyClickHandler } from "components/EmptyClickHandler";
import MapMarkers from "components/MapMarkers";
import PictureInPicture from "components/PictureInPicture";
import { RequestSupportButton } from "components/RequestSupportButton";
import UserSprite from "components/UserSprite";
import { sendLocation } from "utils/sendLocation";

import type { GeolocationPlugin, Position } from "@capacitor/geolocation";

const initialLocation = { latitude: 37.53425472499197, longitude: -122.33120252238274 };

export default function Page() {
  const mapboxAccessToken = process.env.NEXT_PUBLIC_MAPBOX_ACCESS_TOKEN;
  const mapboxHost = process.env.NEXT_PUBLIC_MAPBOX_HOST;
  const [userLocation, setUserLocationState] = useState<Position | null>(null);
  const [requestingSupport, setRequestingSupportState] = useState<boolean>(false);
  const userLocationRef = useRef(userLocation);
  const requestingSupportRef = useRef(requestingSupport);
  const initializedLocationRef = useRef(false);
  const map = useNomadStore(state => state.mapboxMap);
  const [pipPosition, setPipPosition] = useState({ x: 20, y: 300 });

  const setRequestingSupport = (r: boolean) => {
    setRequestingSupportState(r);
    requestingSupportRef.current = r;
  };

  const setUserLocation = (location: Position | null) => {
    setUserLocationState(location);
    userLocationRef.current = location;
  };

  // Start watching the user's location on load
  useEffect(() => {
    let watchId: string;
    let Geolocation: GeolocationPlugin;
    const startWatching = async () => {
      // We need to import the plugin at runtime to avoid a build error where window is called during SSR
      const geolocationImport = await import("@capacitor/geolocation");
      Geolocation = geolocationImport.Geolocation;
      if (!Geolocation) return;
      watchId = await Geolocation.watchPosition(
        {
          enableHighAccuracy: true,
          timeout: 10000,
        },
        setUserLocation
      );
    };
    startWatching();
    return () => {
      if (watchId && Geolocation) {
        Geolocation.clearWatch({ id: watchId });
      }
    };
  }, []);

  // Fly to the user's current location when acquiring it
  useEffect(() => {
    if (map && userLocation && !initializedLocationRef.current) {
      initializedLocationRef.current = true;
      map.flyTo({
        center: [userLocation.coords.longitude, userLocation.coords.latitude],
        zoom: 16,
        pitch: 0,
      });
    }
  }, [userLocation, map]);

  // Update PiP position based on window height when component mounts
  useEffect(() => {
    setPipPosition({ x: 20, y: window.innerHeight - 220 });
  }, []);
  // Send user location to the server every second
  const shareLocationIntervalRef = useRef<NodeJS.Timeout | null>(null);
  useEffect(() => {
    if (userLocation && !shareLocationIntervalRef.current) {
      shareLocationIntervalRef.current = setInterval(() => {
        console.log("will send location", userLocationRef.current, requestingSupportRef.current);
        sendLocation(userLocationRef.current, requestingSupportRef.current);
      }, 1000);
    }
  }, [userLocation]);

  // Stop sending location on unmount
  useEffect(() => {
    return () => {
      if (shareLocationIntervalRef.current) {
        clearInterval(shareLocationIntervalRef.current);
      }
    };
  }, []);

  return (
    <div className="h-full w-full relative">
      <NomadMap
        className="h-full w-full"
        initialViewState={{
          center: [initialLocation.longitude, initialLocation.latitude],
          zoom: 16,
          pitch: 0,
        }}
        accessToken={mapboxAccessToken}
        host={mapboxHost}
      >
        <MapMarkers />
        <EmptyClickHandler />
        {userLocation && <UserSprite location={userLocation.coords} scale={500} />}
      </NomadMap>
      <DetailModal />
      <div className="absolute top-10 right-0 m-4">
        <RequestSupportButton
          requestingSupport={requestingSupport}
          setRequestingSupport={setRequestingSupport}
        />
      </div>

      {/* Always visible PictureInPicture */}
      <PictureInPicture
        // src="https://www.youtube.com/embed/8C2C1E7vpMM?rel=0&modestbranding=1&autoplay=1&controls=1"
        src="https://cloud.patrollinkhack-etienne-dupont.direct.coder.dev.skyd.io/shared_link/SfT7soYn_6Sfrpeq31EA9beTOtrJWQkbt5tmPCxKAlo/live/4uspvlr967mydjan"
        title="Live Feed"
        isOpen={true}
        initialPosition={pipPosition}
        initialWidth={320}
        initialHeight={180}
      />
    </div>
  );
}
