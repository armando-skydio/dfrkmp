"use client";

import { faBat } from "@fortawesome/pro-regular-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import { AsyncToggleButton } from "@skydio/rivet-ui/src/incubator/async-toggle-button";

export const RequestSupportButton = ({
  requestingSupport,
  setRequestingSupport,
}: {
  requestingSupport: boolean;
  setRequestingSupport: (r: boolean) => void;
}) => {
  return (
    <AsyncToggleButton
      className="size-8"
      isToggleActive={requestingSupport}
      isLoading={false}
      onToggle={() => setRequestingSupport(!requestingSupport)}
      theme="blue"
      removeFocusOnToggle
      size="lg"
    >
      <FontAwesomeIcon icon={faBat} size="xl" />
    </AsyncToggleButton>
  );
};
