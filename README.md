# Patrol Link App

## Running the app

- Make sure to have all the dependencies installed:

```bash
yarn install
```

### Running with static webapp (no hot reload)

- Build the webapp:

```bash
yarn run build
```

- Sync and run the app with capacitor with:

```bash
yarn run:ios # on iOS
yarn run:android # on Android
```

### Running with hot reload

- Start the NextJS dev server:

```bash
yarn dev
```

- On a different terminal, start the capacitor app with:

```bash
yarn run:ios # on iOS
yarn run:android # on Android
```

Note: if you're going to use a real device instead of a simulator, you'll need to modify the `capacitor.config.json` file and set the server url to match your IP address:

```json
    "server": {
        "url": "http://192.168.1.52:3000", // replace with your IP address
        "cleartext": true
    }
```
