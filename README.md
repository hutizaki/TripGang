# SmexyMap - GPS Visualization Tool

A GPS visualization application built with Deck.GL and Electron.

## Setup

1. Install dependencies:
```
npm install
```

2. Run the application:
```
npm start
```

## Packaging for Distribution

To create distributable packages for macOS and Windows:
```
npm run package
```

This will create executables in the `dist` folder.

## Project Structure

- `main.js` - Electron main process
- `public/smexy.html` - Main HTML file
- All external libraries are loaded via CDN

## Requirements

- Node.js 14+
- npm 6+
