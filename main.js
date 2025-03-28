const { app, BrowserWindow } = require('electron');
const path = require('path');


// Keep a global reference of the window object to prevent garbage collection
let mainWindow;

// Check if we're in development mode
const isDev = process.env.NODE_ENV === 'development';

function createWindow() {
  // Create the browser window
  mainWindow = new BrowserWindow({
    width: 1280,
    height: 800,
    webPreferences: {
      nodeIntegration: false, // Security: No Node.js APIs in renderer
      contextIsolation: true,
      webSecurity: !isDev // Disable webSecurity in dev mode for easier local development
    },
    icon: path.join(__dirname, isDev ? 'public/app-icon.png' : 'public/app-icon.png'), // Use appropriate icon based on mode
    show: true
  });

  // Prevent window from being minimized to dock on close
  mainWindow.on('close', (event) => {
    // This will force the window to close fully rather than minimizing
    app.quit();
  });

  // Load the HTML file
  mainWindow.loadFile(path.join(__dirname, 'public/smexy.html'));

  // Open DevTools in development
  if (isDev) {
    mainWindow.webContents.openDevTools();
    console.log('Running in development mode');
  }

  // Handle window closed
  mainWindow.on('closed', () => {
    mainWindow = null;
  });
}

// Create window when Electron is ready
app.whenReady().then(createWindow);

// Quit when all windows are closed (always, even on macOS)
app.on('window-all-closed', () => {
  app.quit();
});

// On macOS, re-create window when dock icon is clicked
app.on('activate', () => {
  if (mainWindow === null) {
    createWindow();
  }
}); 