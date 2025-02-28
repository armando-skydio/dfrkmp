import { Coordinates, NomadMap } from "@skydio/nomad";

import UserSprite from "components/UserSprite";

const initialLocation = { latitude: 37.53425472499197, longitude: -122.33120252238274 };

export default function Page() {
  const mapboxAccessToken = process.env.NEXT_PUBLIC_MAPBOX_ACCESS_TOKEN;
  const mapboxHost = process.env.NEXT_PUBLIC_MAPBOX_HOST;
  return (
    <div className="h-full w-full">
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
        <UserSprite location={initialLocation} scale={500} />
      </NomadMap>
    </div>
  );
}
