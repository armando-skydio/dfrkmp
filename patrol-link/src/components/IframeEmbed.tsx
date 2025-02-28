"use client";

import React, { useRef, useState, useEffect } from "react";

interface IframeEmbedProps {
  src: string;
  title?: string;
  width?: number | string;
  height?: number | string;
  allowFullscreen?: boolean;
  className?: string;
  style?: React.CSSProperties;
  onReady?: () => void;
  allow?: string;
  frameBorder?: string;
}

export const IframeEmbed: React.FC<IframeEmbedProps> = ({
  src,
  title = "Embedded content",
  width = "100%",
  height = "100%",
  allowFullscreen = true,
  className = "",
  style = {},
  onReady,
  allow = "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture",
  frameBorder = "0",
}) => {
  const iframeRef = useRef<HTMLIFrameElement>(null);
  const [isLoaded, setIsLoaded] = useState(false);

  // Handle iframe load event
  const handleIframeLoad = () => {
    setIsLoaded(true);
    if (onReady) {
      onReady();
    }
  };

  useEffect(() => {
    // Add event listener when component mounts
    const iframe = iframeRef.current;
    if (iframe) {
      iframe.addEventListener("load", handleIframeLoad);
    }

    // Clean up event listener when component unmounts
    return () => {
      if (iframe) {
        iframe.removeEventListener("load", handleIframeLoad);
      }
    };
  }, []);

  return (
    <div
      className={`iframe-embed-container ${className}`}
      style={{
        position: "relative",
        width,
        height,
        overflow: "hidden",
        ...style,
      }}
    >
      {!isLoaded && (
        <div
          style={{
            position: "absolute",
            top: 0,
            left: 0,
            width: "100%",
            height: "100%",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            backgroundColor: "#000",
          }}
        >
          <div className="loading">Loading...</div>
        </div>
      )}
      <iframe
        ref={iframeRef}
        width="100%"
        height="100%"
        src={src}
        title={title}
        frameBorder={frameBorder}
        allow={allow}
        allowFullScreen={allowFullscreen}
        style={{
          position: "absolute",
          top: 0,
          left: 0,
          width: "100%",
          height: "100%",
          border: "none",
        }}
      />
    </div>
  );
};

export default IframeEmbed;
