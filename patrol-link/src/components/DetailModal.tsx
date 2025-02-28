import { useEffect, useState } from "react";

import { Button } from "@skydio/rivet-ui";

import { useMarkerStore } from "../store/markerStore";
import { DetailModalContainer } from "./DetailModalContainer";

// Simple KeyValueDetail component similar to the one in IncidentDetailModal
const KeyValueDetail = ({ keyName, value }: { keyName: string; value: string | JSX.Element }) => {
  return (
    <div className="flex w-full flex-row items-center justify-between gap-2">
      <div className="w-1/3 truncate text-sm font-medium leading-none font-['SF_Pro_Display']">
        {keyName}
      </div>
      <div className="w-2/3 text-right text-sm leading-normal font-['SF_Pro_Display']">{value}</div>
    </div>
  );
};

export const DetailModal = () => {
  const markers = useMarkerStore(state => state.markers);
  const selectedMarker = markers.find(marker => marker.selected);
  const updateMarkerDescription = useMarkerStore(state => state.updateMarkerDescription);
  const removeMarker = useMarkerStore(state => state.removeMarker);
  const selectMarker = useMarkerStore(state => state.selectMarker);

  const [isEditing, setIsEditing] = useState(false);
  const [description, setDescription] = useState("");
  const [isVisible, setIsVisible] = useState(false);
  const [shouldRender, setShouldRender] = useState(false);

  // Use useEffect to update description when isEditing changes
  useEffect(() => {
    if (isEditing) {
      setDescription(selectedMarker?.description ?? "");
    }
  }, [isEditing, selectedMarker]);

  // Animation effect when modal appears or disappears
  useEffect(() => {
    if (selectedMarker) {
      // First make the component render
      setShouldRender(true);
      // Small delay to ensure the DOM is ready for the animation
      const timer = setTimeout(() => {
        setIsVisible(true);
      }, 10);
      return () => clearTimeout(timer);
    } else {
      // First hide the modal with animation
      setIsVisible(false);
      // Then remove it from DOM after animation completes
      const timer = setTimeout(() => {
        setShouldRender(false);
      }, 300); // This should match the duration in the CSS transition
      return () => clearTimeout(timer);
    }
  }, [selectedMarker]);

  if (!shouldRender) {
    return null;
  }

  const handleDelete = () => {
    removeMarker(selectedMarker?.id ?? "");
    selectMarker(null);
  };

  const handleEdit = () => {
    setIsEditing(true);
  };

  const handleSave = () => {
    updateMarkerDescription(selectedMarker?.id ?? "", description);
    setIsEditing(false);
  };

  const handleCancel = () => {
    setDescription(selectedMarker?.description ?? "");
    setIsEditing(false);
  };

  return (
    <div className="absolute bottom-0 w-full max-w-full z-[1000]">
      <div
        className={`transform transition-transform duration-300 ease-out ${isVisible ? "-translate-y-0" : "translate-y-full"}`}
      >
        <DetailModalContainer>
          <div className="w-full font-['SF_Pro_Display'] max-w-full">
            {/* Title section */}
            <div className="p-4 w-full">
              <h2 className="text-lg font-medium leading-none">Marker Details</h2>
            </div>

            {/* Divider */}
            <div className="flex h-px w-full bg-gray-200" />

            {/* Content section */}
            <div className="flex w-full flex-col gap-4 p-4">
              {/* Description section */}
              <div className="flex flex-col gap-2">
                {isEditing ? (
                  <div className="flex flex-col space-y-2">
                    <KeyValueDetail
                      keyName="Description"
                      value={
                        <textarea
                          className="w-full p-2 border rounded text-sm font-medium font-['SF_Pro_Display']"
                          value={description}
                          onChange={e => setDescription(e.target.value)}
                          rows={3}
                        />
                      }
                    />
                    <div className="flex space-x-2">
                      <Button variant="primary" onPress={handleSave}>
                        Save
                      </Button>
                      <Button variant="default" onPress={handleCancel}>
                        Cancel
                      </Button>
                    </div>
                  </div>
                ) : (
                  <div className="flex flex-col gap-2">
                    <KeyValueDetail
                      keyName="Description"
                      value={selectedMarker?.description || "No description provided"}
                    />
                  </div>
                )}
              </div>
            </div>

            {/* Divider */}
            <div className="flex h-px w-full bg-gray-200" />

            {/* Actions section */}
            {!isEditing && (
              <div className="flex w-full gap-4 p-4">
                <Button variant="primary" onPress={handleEdit} className="flex-1">
                  Edit Description
                </Button>
                <Button variant="default" onPress={handleDelete} className="flex-1">
                  Delete Marker
                </Button>
              </div>
            )}
          </div>
        </DetailModalContainer>
      </div>
    </div>
  );
};
