"use client";

// These styles apply to every route in the application
import { NomadStoreProvider } from "@skydio/nomad";

import "./globals.css";
import "../styles/global.css";

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en" className="h-full">
      <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, viewport-fit=cover" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
      </head>
      <NomadStoreProvider
        initialStoreProps={{
          mapStyle: "mapbox://styles/filmfan222/cm0cl3c6d012l01o06ger4788",
          projectionMode: "orthographic",
        }}
      >
        <body className="h-full m-0 p-0 overflow-hidden">
          <main className="fixed bg-[#ffffff] text-black size-full">{children}</main>
        </body>
      </NomadStoreProvider>
    </html>
  );
}
