if ('serviceWorker' in navigator) {
  navigator.serviceWorker.register('/sw.js')
    .then(() => console.log('Service Worker registered'))
    .catch(err => console.error('SW registration failed:', err));
}

document.getElementById('content').innerHTML = `
  <div style="text-align: center; padding: 2rem;">
    <h2>Welcome to Event Manager</h2>
    <p>Your college event management PWA</p>
  </div>
`;
