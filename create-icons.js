const fs = require('fs');
const path = require('path');
const { createCanvas } = require('canvas');

// Create a 1024x1024 canvas for our icon
const size = 1024;
const canvas = createCanvas(size, size);
const ctx = canvas.getContext('2d');

// Draw background
ctx.fillStyle = '#2998ff'; // Use the primary color from the app
ctx.fillRect(0, 0, size, size);

// Draw a simple map icon
ctx.fillStyle = 'white';
ctx.beginPath();
ctx.arc(size/2, size/2, size/3, 0, Math.PI * 2);
ctx.fill();

// Draw a location pin
ctx.fillStyle = '#e74c3c'; // Accent color
ctx.beginPath();
ctx.arc(size/2, size/2, size/6, 0, Math.PI * 2);
ctx.fill();

// Draw a pulsing circle
ctx.strokeStyle = '#2998ff';
ctx.lineWidth = size/20;
ctx.beginPath();
ctx.arc(size/2, size/2, size/4, 0, Math.PI * 2);
ctx.stroke();

// Save the icon
const buffer = canvas.toBuffer('image/png');
fs.writeFileSync(path.join(__dirname, 'public/app-icon.png'), buffer);

console.log('Icon created successfully!'); 