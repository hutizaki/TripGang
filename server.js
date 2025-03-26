const express = require('express');
const path = require('path');
const app = express();
const port = 3001;

// Serve static files from the public directory
app.use(express.static('public'));

// Serve the models directory for Three.js models
app.use('/models', express.static(path.join(__dirname, 'models')));

// Route for the main page - serve smexy.html instead of sexymap.html
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'smexy.html'));
});

// Start the server
app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}`);
  console.log(`Access the map at http://localhost:${port}/`);
}); 