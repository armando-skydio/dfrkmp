"use client";

import React, { useState, useRef, useEffect, useCallback } from "react";
import { createPortal } from "react-dom";
import IframeEmbed from "./IframeEmbed";

interface PictureInPictureProps {
  src: string;
  title?: string;
  initialWidth?: number;
  initialHeight?: number;
  initialPosition?: { x: number; y: number };
  minWidth?: number;
  minHeight?: number;
  isOpen?: boolean;
}

export const PictureInPicture: React.FC<PictureInPictureProps> = ({
  src,
  title = "Embedded content",
  initialWidth = 320,
  initialHeight = 180,
  initialPosition = { x: 20, y: 20 },
  minWidth = 240,
  minHeight = 135,
  isOpen = true,
}) => {
  const [position, setPosition] = useState(initialPosition);
  const [size, setSize] = useState({ width: initialWidth, height: initialHeight });
  const [isDragging, setIsDragging] = useState(false);
  const [isResizing, setIsResizing] = useState(false);
  const [dragOffset, setDragOffset] = useState({ x: 0, y: 0 });
  const [isMinimized, setIsMinimized] = useState(false);
  const [isMounted, setIsMounted] = useState(false);
  
  const containerRef = useRef<HTMLDivElement>(null);
  const headerRef = useRef<HTMLDivElement>(null);
  const resizeHandleRef = useRef<HTMLDivElement>(null);

  // Handle client-side mounting
  useEffect(() => {
    setIsMounted(true);
    return () => setIsMounted(false);
  }, []);

  // Handle header touch start for dragging (for touch devices)
  const handleHeaderTouchStart = (e: React.TouchEvent<HTMLDivElement>) => {
    if (!headerRef.current || e.touches.length !== 1) return;
    console.log("TOUCH START");
    
    setIsDragging(true);
    const rect = containerRef.current?.getBoundingClientRect();
    console.log("rect", rect);
    const dragOffsetVal = {
      x: e.touches[0].clientX - (rect?.left ?? 0),
      y: e.touches[0].clientY - (rect?.top ?? 0),
    };
    if (rect) {
      setDragOffset(dragOffsetVal);
    }

    // Add document-level event listeners
    document.addEventListener("touchmove", handleTouchMove);
    document.addEventListener("touchend", handleTouchEnd);
  };

  // Handle touch move for dragging
  const handleTouchMove = useCallback((e: TouchEvent) => {
    if (e.touches.length !== 1) return;
    
    // Calculate new position
    const newX = e.touches[0].clientX - dragOffset.x;
    const newY = e.touches[0].clientY - dragOffset.y;
    
    // Get window dimensions
    const windowWidth = window.innerWidth;
    const windowHeight = window.innerHeight;
    
    // Ensure the PiP stays within the window bounds
    const maxX = windowWidth - size.width;
    const maxY = windowHeight - size.height;
    
    setPosition({
      x: Math.max(0, Math.min(newX, maxX)),
      y: Math.max(0, Math.min(newY, maxY)),
    });
  }, [dragOffset, size])

  // Handle touch end to stop dragging
  const handleTouchEnd = (e: TouchEvent) => {
    setIsDragging(false);
    
    // Remove document-level event listeners
    document.removeEventListener("touchmove", handleTouchMove);
    document.removeEventListener("touchend", handleTouchEnd);
  };

  // Handle resize handle mouse down
  const handleResizeMouseDown = (e: React.MouseEvent<HTMLDivElement>) => {
    e.preventDefault();
    e.stopPropagation();
    setIsResizing(true);
  };

  // Handle mouse move for resizing
  useEffect(() => {
    const handleResizeMouseMove = (e: globalThis.MouseEvent) => {
      e.preventDefault();
      
      if (isResizing && containerRef.current) {
        // Calculate new size
        const rect = containerRef.current.getBoundingClientRect();
        const newWidth = Math.max(minWidth, e.clientX - rect.left);
        const newHeight = Math.max(minHeight, e.clientY - rect.top);
        
        setSize({
          width: newWidth,
          height: newHeight,
        });
      }
    };

    const handleResizeMouseUp = (e: globalThis.MouseEvent) => {
      e.preventDefault();
      setIsResizing(false);
    };

    if (isResizing) {
      document.addEventListener("mousemove", handleResizeMouseMove);
      document.addEventListener("mouseup", handleResizeMouseUp);
    }

    return () => {
      document.removeEventListener("mousemove", handleResizeMouseMove);
      document.removeEventListener("mouseup", handleResizeMouseUp);
    };
  }, [isResizing, minWidth, minHeight]);

  const toggleMinimize = () => {
    setIsMinimized(!isMinimized);
  };

  if (!isMounted || !isOpen) return null;

  // Create portal to render at the document body level
  return createPortal(
    <div
      ref={containerRef}
      className="pip-container"
      style={{
        position: "fixed",
        top: `${position.y}px`,
        left: `${position.x}px`,
        width: `${size.width}px`,
        height: isMinimized ? "36px" : `${size.height}px`,
        backgroundColor: "#000",
        borderRadius: "6px",
        boxShadow: "0 4px 12px rgba(0, 0, 0, 0.15)",
        overflow: "hidden",
        zIndex: 1000,
        transition: "height 0.2s ease-in-out",
        display: "flex",
        flexDirection: "column",
      }}
    >
      {/* Header */}
      <div
        ref={headerRef}
        className="pip-header"
        style={{
          height: "36px",
          backgroundColor: "#1a1a1a",
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
          padding: "0 10px",
          cursor: "move",
          userSelect: "none",
          touchAction: "none", // Prevent default touch actions
        }}
        onTouchStart={handleHeaderTouchStart}
      >
        <div className="pip-title" style={{ color: "#fff", fontSize: "14px" }}>
          {title}
        </div>
        <div className="pip-controls" style={{ display: "flex", gap: "8px" }}>
          <button
            onClick={(e) => {
              e.stopPropagation(); // Prevent event from bubbling to header
              toggleMinimize();
            }}
            style={{
              background: "none",
              border: "none",
              color: "#fff",
              cursor: "pointer",
              padding: "4px",
              fontSize: "14px",
            }}
          >
            {isMinimized ? "+" : "-"}
          </button>
        </div>
      </div>

      {/* Content */}
      {!isMinimized && (
        <div
          className="pip-content"
          style={{
            flex: 1,
            position: "relative",
          }}
        >
          <IframeEmbed
            src={src}
            title={title}
            allowFullscreen={true}
          />
        </div>
      )}

      {/* Resize handle */}
      {!isMinimized && (
        <div
          ref={resizeHandleRef}
          className="pip-resize-handle"
          style={{
            position: "absolute",
            bottom: "0",
            right: "0",
            width: "16px",
            height: "16px",
            cursor: "nwse-resize",
            background: "transparent",
          }}
          onMouseDown={handleResizeMouseDown}
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 16 16"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M14 14L8 14L14 8L14 14Z"
              fill="white"
              fillOpacity="0.5"
            />
          </svg>
        </div>
      )}
    </div>,
    document.body
  );
};

export default PictureInPicture;
