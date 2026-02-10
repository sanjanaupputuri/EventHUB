if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('/sw.js')
      .then(() => console.log('Service Worker registered'))
      .catch(err => console.error('SW registration failed:', err));
  });
}

let deferredPrompt;
window.addEventListener('beforeinstallprompt', (e) => {
  e.preventDefault();
  deferredPrompt = e;
  showInstallButton();
});

function showInstallButton() {
  const installBtn = document.createElement('button');
  installBtn.textContent = 'Install App';
  installBtn.style.cssText = 'position:fixed;bottom:20px;right:20px;padding:12px 24px;background:#2196F3;color:white;border:none;border-radius:8px;cursor:pointer;box-shadow:0 4px 8px rgba(0,0,0,0.2);z-index:1000';
  installBtn.onclick = async () => {
    if (deferredPrompt) {
      deferredPrompt.prompt();
      await deferredPrompt.userChoice;
      deferredPrompt = null;
      installBtn.remove();
    }
  };
  document.body.appendChild(installBtn);
}

document.getElementById('content').innerHTML = `
  <div style="text-align: center; padding: 2rem;">
    <h2>Welcome to Event Manager</h2>
    <p>Your college event management PWA</p>
  </div>
`;
