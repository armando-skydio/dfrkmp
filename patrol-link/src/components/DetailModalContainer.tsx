import { forwardRef } from "react";

interface Props {
  children: React.ReactElement | (React.ReactElement | null | undefined)[] | null | undefined;
}

export const DetailModalContainer = forwardRef<HTMLDivElement, Props>(({ children }, ref) => {
  return (
    <div
      ref={ref}
      className="z-[1000] inline-flex w-full flex-col items-center justify-center border border-gray-100 bg-white dark:border-gray-800 dark:bg-gray-900 dark:text-white"
    >
      {children}
    </div>
  );
});

DetailModalContainer.displayName = "DetailModalContainer";
