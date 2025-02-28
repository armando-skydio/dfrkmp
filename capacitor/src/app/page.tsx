import { Coordinates, NomadMap } from "@skydio/nomad";

import UserSprite from "components/UserSprite";

const initialLocation = { latitude: 37.53425472499197, longitude: -122.33120252238274 };

export default function Page() {
  const mapboxAccessToken =
    "pk.eyJ1Ijoic2t5ZGlvLXRlYW0iLCJhIjoiY2tkamdtajh6MDE4eDMzcGVvMGp2MG80MyJ9.fPQ-OIzIffSOW1gznhiZlQ"; //TODO(Nacho): place this in env vars (it's safe to commit though!)
  const mapboxHost = "https://api.mapbox.com"; //TODO(Nacho): place this in env vars (it's safe to commit though!)
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
        <UserSprite location={initialLocation} scale={0.5} />
      </NomadMap>
    </div>
  );
}
