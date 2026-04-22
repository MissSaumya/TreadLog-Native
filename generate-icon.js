import sharp from 'sharp';
import fs from 'fs';

const svgBuffer = Buffer.from(
  `<svg width="1024" height="1024" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
    <!-- Solid Dark Navy Background -->
    <rect width="1024" height="1024" fill="#0f172a" rx="200" />
    
    <!-- Outer Glow / Ring -->
    <circle cx="512" cy="512" r="380" fill="none" stroke="#1e293b" stroke-width="20" />
    <circle cx="512" cy="512" r="420" fill="none" stroke="#2563eb" stroke-width="4" opacity="0.5" />

    <!-- Treadmill Gradient -->
    <defs>
      <linearGradient id="blueGlow" x1="0%" y1="0%" x2="100%" y2="100%">
        <stop offset="0%" stop-color="#3b82f6" />
        <stop offset="100%" stop-color="#2563eb" />
      </linearGradient>
      <linearGradient id="orangeGlow" x1="0%" y1="0%" x2="100%" y2="100%">
        <stop offset="0%" stop-color="#f97316" />
        <stop offset="100%" stop-color="#ea580c" />
      </linearGradient>
    </defs>

    <!-- Main Figure & Treadmill - scaled up from 24x24 app logo to 1024x1024 (Scale factor ~25) -->
    <g transform="translate(262, 262) scale(20.8)">
      <!-- Treadmill Base -->
      <path d="M2 20s4-.5 10 0 10 0 10 0" fill="none" stroke="url(#blueGlow)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M4 20v2" fill="none" stroke="url(#blueGlow)" stroke-width="2" stroke-linecap="round" />
      <path d="M20 20v2" fill="none" stroke="url(#blueGlow)" stroke-width="2" stroke-linecap="round" />
      <!-- Console Handle -->
      <path d="M18 20L15 9l-3-2" fill="none" stroke="url(#blueGlow)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      <!-- Person Head -->
      <path d="M11.5 5.5c.5-.5 1.5-.5 2 0s.5 1.5 0 2-1.5.5-2 0-.5-1.5 0-2z" fill="none" stroke="url(#orangeGlow)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
      <!-- Body -->
      <path d="M12.5 7.5c-1 1-2.5 3-2.5 5" fill="none" stroke="url(#orangeGlow)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
      <!-- Arms -->
      <path d="M10 10l3 1.5" fill="none" stroke="url(#orangeGlow)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
      <path d="M10 10L7 9.5" fill="none" stroke="url(#orangeGlow)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
      <!-- Legs -->
      <path d="M10 12.5l2 3.5-1.5 3" fill="none" stroke="url(#orangeGlow)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
      <path d="M10 12.5L7 14l-.5 2" fill="none" stroke="url(#orangeGlow)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
    </g>
  </svg>`
);

async function generate() {
  try {
    if (!fs.existsSync('assets')) {
      fs.mkdirSync('assets');
    }
    
    await sharp(svgBuffer)
      .png()
      .toFile('assets/logo.png');
      
    console.log('Successfully generated assets/logo.png');
  } catch (error) {
    console.error('Error:', error);
  }
}

generate();
