import { useCallback, useRef } from 'react';
import { ThreeEvent } from '@react-three/fiber';

type EventType = React.MouseEvent | React.TouchEvent | ThreeEvent<MouseEvent>;

interface LongPressOptions<T extends EventType> {
  onLongPress: (event: T) => void;
  onClick?: (event: T) => void;
  longPressThreshold?: number; // in milliseconds
  movementThreshold?: number; // in pixels
}

export function useLongPress<T extends EventType>({
  onLongPress,
  onClick,
  longPressThreshold = 500,
  movementThreshold = 10, // Default movement threshold of 10px
}: LongPressOptions<T>) {
  const timerRef = useRef<NodeJS.Timeout | null>(null);
  const isLongPress = useRef(false);
  const startPosition = useRef<{ x: number; y: number } | null>(null);
  const hasMoved = useRef(false);

  const getEventPosition = (event: T) => {
    // Handle different event types
    if ('clientX' in event && 'clientY' in event) {
      return { x: event.clientX, y: event.clientY };
    } else if ('touches' in event && event.touches.length > 0) {
      return { x: event.touches[0].clientX, y: event.touches[0].clientY };
    }
    return null;
  };

  const startPressTimer = useCallback(
    (event: T) => {
      isLongPress.current = false;
      hasMoved.current = false;
      startPosition.current = getEventPosition(event);
      
      timerRef.current = setTimeout(() => {
        if (!hasMoved.current) {
          isLongPress.current = true;
          onLongPress(event);
        }
      }, longPressThreshold);
    },
    [onLongPress, longPressThreshold]
  );

  const handleMove = useCallback(
    (event: T) => {
      if (!startPosition.current) return;
      
      const currentPosition = getEventPosition(event);
      if (!currentPosition) {
        return;
      }
      
      const deltaX = Math.abs(currentPosition.x - startPosition.current.x);
      const deltaY = Math.abs(currentPosition.y - startPosition.current.y);
      
      // Check if movement exceeds threshold
      if (deltaX > movementThreshold || deltaY > movementThreshold) {
        hasMoved.current = true;
        clearTimer();
      }
    },
    [movementThreshold]
  );

  const handleOnClick = useCallback(
    (event: T) => {
      if (isLongPress.current || hasMoved.current) {
        return;
      }
      if (onClick) {
        onClick(event);
      }
    },
    [onClick]
  );

  const clearTimer = useCallback(() => {
    if (timerRef.current) {
      clearTimeout(timerRef.current);
      timerRef.current = null;
    }
    startPosition.current = null;
  }, []);

  return {
    onMouseDown: startPressTimer,
    onMouseMove: handleMove,
    onMouseUp: clearTimer,
    onMouseLeave: clearTimer,
    onTouchStart: startPressTimer,
    onTouchMove: handleMove,
    onTouchEnd: clearTimer,
    onClick: handleOnClick,
  };
}
