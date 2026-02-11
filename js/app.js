// Service Worker Registration
if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('/sw.js')
      .then(() => console.log('Service Worker registered'))
      .catch(err => console.error('SW registration failed:', err));
  });
}

// App State Management
class EventHubApp {
  constructor() {
    this.currentScreen = 'splash';
    this.currentTab = 'discover';
    this.theme = localStorage.getItem('theme') || 'light';
    this.user = JSON.parse(localStorage.getItem('user') || 'null');
    this.events = mockEvents;
    this.registeredEvents = new Set(JSON.parse(localStorage.getItem('registered') || '[]'));
    this.bookmarkedEvents = new Set(JSON.parse(localStorage.getItem('bookmarked') || '[]'));
    this.notificationsEnabled = localStorage.getItem('notifications') === 'true';
    this.currentEventIndex = 0;
    this.showThemeMenu = false;
    this.selectedEvent = null;
    
    this.init();
  }

  init() {
    this.applyTheme();
    this.showSplash();
  }

  applyTheme() {
    document.body.className = `${this.theme}-theme`;
    const metaTheme = document.querySelector('meta[name="theme-color"]');
    if (metaTheme) {
      metaTheme.content = this.theme === 'dark' ? '#1E1E1E' : '#6200EE';
    }
  }

  setTheme(theme) {
    this.theme = theme;
    localStorage.setItem('theme', theme);
    this.applyTheme();
    this.render();
  }

  showSplash() {
    this.currentScreen = 'splash';
    this.render();
    setTimeout(() => {
      if (this.user) {
        this.showMain();
      } else {
        this.showLogin();
      }
    }, 2000);
  }

  showLogin() {
    this.currentScreen = 'login';
    this.render();
  }

  showSignup() {
    this.currentScreen = 'signup';
    this.render();
  }

  showMain() {
    this.currentScreen = 'main';
    this.render();
  }

  showSettings() {
    this.currentScreen = 'settings';
    this.render();
  }

  showEventDetails(event) {
    this.selectedEvent = event;
    this.currentScreen = 'eventDetails';
    this.render();
  }

  handleLogin(email, password) {
    // Simple validation
    if (email && password) {
      this.user = {
        id: Date.now().toString(),
        name: email.split('@')[0],
        email: email,
        collegeId: 'UNIS'
      };
      localStorage.setItem('user', JSON.stringify(this.user));
      this.showMain();
    }
  }

  handleSignup(name, email, password) {
    if (name && email && password) {
      this.user = {
        id: Date.now().toString(),
        name: name,
        email: email,
        collegeId: 'UNIS'
      };
      localStorage.setItem('user', JSON.stringify(this.user));
      this.showMain();
    }
  }

  handleLogout() {
    this.user = null;
    localStorage.removeItem('user');
    this.showLogin();
  }

  toggleRegister(eventId) {
    if (this.registeredEvents.has(eventId)) {
      this.registeredEvents.delete(eventId);
      this.showNotification('Unregistered from event');
    } else {
      this.registeredEvents.add(eventId);
      this.showNotification('Successfully registered!');
      if (this.notificationsEnabled) {
        this.scheduleNotification(eventId);
      }
    }
    this.saveState();
    this.render();
  }

  toggleBookmark(eventId) {
    if (this.bookmarkedEvents.has(eventId)) {
      this.bookmarkedEvents.delete(eventId);
    } else {
      this.bookmarkedEvents.add(eventId);
    }
    this.saveState();
    this.render();
  }

  toggleNotifications() {
    this.notificationsEnabled = !this.notificationsEnabled;
    localStorage.setItem('notifications', this.notificationsEnabled);
    if (this.notificationsEnabled) {
      this.requestNotificationPermission();
    }
    this.render();
  }

  requestNotificationPermission() {
    if ('Notification' in window && Notification.permission === 'default') {
      Notification.requestPermission();
    }
  }

  scheduleNotification(eventId) {
    const event = this.events.find(e => e.id === eventId);
    if (event && 'Notification' in window && Notification.permission === 'granted') {
      // In a real app, this would schedule a notification
      console.log('Notification scheduled for:', event.title);
    }
  }

  showNotification(message) {
    // Simple toast notification
    const toast = document.createElement('div');
    toast.textContent = message;
    toast.style.cssText = `
      position: fixed;
      bottom: 100px;
      left: 50%;
      transform: translateX(-50%);
      background: ${this.theme === 'dark' ? '#333' : '#fff'};
      color: ${this.theme === 'dark' ? '#fff' : '#000'};
      padding: 1rem 1.5rem;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.3);
      z-index: 10000;
      animation: slideUp 0.3s;
    `;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 2000);
  }

  saveState() {
    localStorage.setItem('registered', JSON.stringify([...this.registeredEvents]));
    localStorage.setItem('bookmarked', JSON.stringify([...this.bookmarkedEvents]));
  }

  switchTab(tab) {
    this.currentTab = tab;
    this.render();
  }

  // Render Methods
  renderSplash() {
    return `
      <div class="splash-screen">
        <div class="splash-logo">🎓</div>
        <h1 class="splash-title">UNIS</h1>
        <p class="splash-subtitle">College Event Manager</p>
      </div>
    `;
  }

  renderLogin() {
    return `
      <div class="auth-container">
        <h1 class="auth-title">Welcome Back</h1>
        <p class="auth-subtitle">Sign in with your college email</p>
        
        <button class="btn btn-primary" onclick="app.handleGoogleSignIn()" style="margin-top: 2rem;">
          Sign in with Google
        </button>
      </div>
    `;
  }

  async handleGoogleSignIn() {
    const result = await window.AuthManager.signInWithGoogle();
    if (result.success) {
      const rollNumber = result.email.split('@')[0];
      this.user = {
        id: Date.now().toString(),
        name: rollNumber,
        email: result.email,
        collegeId: rollNumber
      };
      localStorage.setItem('user', JSON.stringify(this.user));
      this.showMain();
    } else {
      alert(result.error);
    }
  }

  renderSignup() {
    return `
      <div class="auth-container">
        <h1 class="auth-title">Create Account</h1>
        <p class="auth-subtitle">Sign up with your college email</p>
        
        <button class="btn btn-primary" onclick="app.handleGoogleSignIn()" style="margin-top: 2rem;">
          Sign up with Google
        </button>
      </div>
    `;
  }

  renderEventCard(event, showActions = true) {
    const isRegistered = this.registeredEvents.has(event.id);
    const isBookmarked = this.bookmarkedEvents.has(event.id);
    
    return `
      <div class="event-card" onclick="app.showEventDetails(${JSON.stringify(event).replace(/"/g, '&quot;')})">
        <div class="event-card-header">
          <h2 class="event-title">${event.title}</h2>
          ${showActions ? `
            <button class="icon-btn" onclick="event.stopPropagation(); app.toggleBookmark('${event.id}')">
              ${isBookmarked ? '★' : '☆'}
            </button>
          ` : ''}
        </div>
        
        <span class="category-badge">${event.category}</span>
        
        <p class="event-description">${event.description}</p>
        
        <div class="event-meta">
          <div class="event-meta-item">📅 ${event.date}</div>
          <div class="event-meta-item">🕐 ${event.time}</div>
          <div class="event-meta-item">📍 ${event.location}</div>
        </div>
        
        <div class="event-info">
          <div class="event-info-item">👥 ${event.currentParticipants}/${event.maxParticipants} participants</div>
          <div class="event-info-item">🎯 Organized by ${event.organizer}</div>
        </div>
        
        ${showActions ? `
          <button 
            class="btn ${isRegistered ? 'btn-outline' : 'btn-primary'}" 
            onclick="event.stopPropagation(); app.toggleRegister('${event.id}')"
            style="margin-top: 1rem;">
            ${isRegistered ? 'Unregister' : 'Register Now'}
          </button>
        ` : ''}
      </div>
    `;
  }

  renderDiscoverTab() {
    const availableEvents = this.events.filter(e => !this.registeredEvents.has(e.id));
    
    if (availableEvents.length === 0) {
      return `
        <div class="empty-state">
          <div class="empty-icon">🎉</div>
          <h3 class="empty-title">All Caught Up!</h3>
          <p class="empty-message">You've registered for all events</p>
        </div>
      `;
    }
    
    this.currentEventIndex = this.currentEventIndex % availableEvents.length;
    const currentEvent = availableEvents[this.currentEventIndex];
    
    setTimeout(() => this.initSwipe(), 0);
    
    const categoryIcon = {
      'Technical': '💻',
      'Cultural': '🎵',
      'Sports': '⚽',
      'Workshop': '🛠️',
      'Business': '💼',
      'Gaming': '🎮',
      'Social': '👥'
    }[currentEvent.category] || '📅';
    
    const categoryColor = {
      'Technical': 'var(--primary)',
      'Cultural': 'var(--secondary)',
      'Sports': '#FF6B6B',
      'Workshop': '#4ECDC4',
      'Business': '#FFD93D',
      'Gaming': '#A8E6CF',
      'Social': '#FF8B94'
    }[currentEvent.category] || 'var(--primary)';
    
    return `
      <div class="search-filter-bar">
        <input type="text" class="search-input" placeholder="Search events..." id="searchInput">
        <button class="filter-btn" onclick="app.toggleFilter()">🔍</button>
      </div>
      
      <div class="swipe-container">
        <div class="swipe-card" id="swipeCard" onclick="app.showEventDetails(${JSON.stringify(currentEvent).replace(/"/g, '&quot;')})">
          <div class="card-icon-header" style="background: ${categoryColor};">
            <span class="card-icon">${categoryIcon}</span>
          </div>
          <div class="swipe-card-content">
            <h2 class="event-title">${currentEvent.title}</h2>
            <p class="event-date">${currentEvent.date}</p>
            <p class="swipe-hint">Tap for details • Swipe to browse</p>
          </div>
        </div>
      </div>
    `;
  }

  initSwipe() {
    const card = document.getElementById('swipeCard');
    if (!card) return;
    
    let startX = 0, currentX = 0, isDragging = false;
    
    card.addEventListener('touchstart', (e) => {
      startX = e.touches[0].clientX;
      isDragging = true;
      card.style.transition = 'none';
    });
    
    card.addEventListener('touchmove', (e) => {
      if (!isDragging) return;
      currentX = e.touches[0].clientX - startX;
      const rotation = currentX * 0.05;
      card.style.transform = `translateX(${currentX}px) rotate(${rotation}deg)`;
    });
    
    card.addEventListener('touchend', () => {
      card.style.transition = 'transform 0.3s ease';
      const threshold = window.innerWidth * 0.3;
      
      if (currentX > threshold) {
        card.style.transform = `translateX(${window.innerWidth}px) rotate(20deg)`;
        setTimeout(() => {
          this.swipeRight();
          card.style.transform = '';
        }, 300);
      } else if (currentX < -threshold) {
        card.style.transform = `translateX(-${window.innerWidth}px) rotate(-20deg)`;
        setTimeout(() => {
          this.swipeLeft();
          card.style.transform = '';
        }, 300);
      } else {
        card.style.transform = '';
      }
      
      isDragging = false;
      currentX = 0;
    });
  }

  swipeLeft() {
    const availableEvents = this.events.filter(e => !this.registeredEvents.has(e.id));
    this.currentEventIndex = (this.currentEventIndex - 1 + availableEvents.length) % availableEvents.length;
    this.render();
  }

  swipeRight() {
    const availableEvents = this.events.filter(e => !this.registeredEvents.has(e.id));
    this.currentEventIndex = (this.currentEventIndex + 1) % availableEvents.length;
    this.render();
  }

  toggleFilter() {
    alert('Filter feature coming soon!');
  }

  renderRegisteredTab() {
    const registered = this.events.filter(e => this.registeredEvents.has(e.id));
    
    if (registered.length === 0) {
      return `
        <div class="empty-state">
          <div class="empty-icon">📝</div>
          <h3 class="empty-title">No Registered Events</h3>
          <p class="empty-message">Register for events to see them here</p>
        </div>
      `;
    }
    
    return registered.map(e => this.renderEventCard(e)).join('');
  }

  renderBookmarksTab() {
    const bookmarked = this.events.filter(e => this.bookmarkedEvents.has(e.id));
    
    if (bookmarked.length === 0) {
      return `
        <div class="empty-state">
          <div class="empty-icon">⭐</div>
          <h3 class="empty-title">No Bookmarks</h3>
          <p class="empty-message">Bookmark events to save them for later</p>
        </div>
      `;
    }
    
    return bookmarked.map(e => this.renderEventCard(e)).join('');
  }

  renderThemeMenu() {
    if (!this.showThemeMenu) return '';
    
    return `
      <div class="theme-menu" onclick="app.showThemeMenu = false; app.render();">
        <div class="theme-menu-content" onclick="event.stopPropagation();">
          <h3 class="theme-menu-title">Choose Theme</h3>
          
          <div class="theme-option ${this.theme === 'light' ? 'active' : ''}" 
               onclick="app.setTheme('light'); app.showThemeMenu = false;">
            <span class="theme-icon">☀️</span>
            <span class="theme-label">Light</span>
            ${this.theme === 'light' ? '<span>✓</span>' : ''}
          </div>
          
          <div class="theme-option ${this.theme === 'dark' ? 'active' : ''}" 
               onclick="app.setTheme('dark'); app.showThemeMenu = false;">
            <span class="theme-icon">🌙</span>
            <span class="theme-label">Dark</span>
            ${this.theme === 'dark' ? '<span>✓</span>' : ''}
          </div>
          
          <div class="theme-option ${this.theme === 'system' ? 'active' : ''}" 
               onclick="app.setTheme('system'); app.showThemeMenu = false;">
            <span class="theme-icon">📱</span>
            <span class="theme-label">System</span>
            ${this.theme === 'system' ? '<span>✓</span>' : ''}
          </div>
        </div>
      </div>
    `;
  }

  renderSettings() {
    return `
      <div class="app-bar">
        <button class="icon-btn" onclick="app.showMain()">←</button>
        <h1 class="app-bar-title">Settings</h1>
        <div></div>
      </div>
      
      <div class="settings-container">
        <div class="settings-section">
          <h3 class="settings-section-title">Appearance</h3>
          <div class="settings-item" onclick="app.showThemeMenu = true; app.render();">
            <span class="settings-label">Theme</span>
            <span>${this.theme === 'light' ? '☀️ Light' : this.theme === 'dark' ? '🌙 Dark' : '📱 System'}</span>
          </div>
        </div>
        
        <div class="settings-section">
          <h3 class="settings-section-title">Notifications</h3>
          <div class="settings-item">
            <span class="settings-label">Event Reminders</span>
            <div class="toggle-switch ${this.notificationsEnabled ? 'active' : ''}" 
                 onclick="app.toggleNotifications()">
              <div class="toggle-thumb"></div>
            </div>
          </div>
        </div>
        
        <div class="settings-section">
          <h3 class="settings-section-title">Account</h3>
          <div class="settings-item">
            <div>
              <div class="settings-label">${this.user.name}</div>
              <div style="font-size: 0.875rem; opacity: 0.6;">${this.user.email}</div>
            </div>
          </div>
          <button class="btn btn-outline" onclick="app.handleLogout()" style="margin-top: 1rem;">
            Sign Out
          </button>
        </div>
      </div>
      
      ${this.renderThemeMenu()}
    `;
  }

  renderEventDetails() {
    if (!this.selectedEvent) {
      this.showMain();
      return '';
    }
    
    const event = this.selectedEvent;
    const isRegistered = this.registeredEvents.has(event.id);
    const isBookmarked = this.bookmarkedEvents.has(event.id);
    
    return `
      <div class="app-bar">
        <button class="icon-btn" onclick="app.showMain()">←</button>
        <h1 class="app-bar-title">Event Details</h1>
        <button class="icon-btn" onclick="app.toggleBookmark('${event.id}')">
          ${isBookmarked ? '★' : '☆'}
        </button>
      </div>
      
      <div class="content">
        <div class="event-card">
          <h2 class="event-title">${event.title}</h2>
          <span class="category-badge">${event.category}</span>
          
          <p class="event-description">${event.description}</p>
          
          <div class="event-meta">
            <div class="event-meta-item">📅 ${event.date}</div>
            <div class="event-meta-item">🕐 ${event.time}</div>
            <div class="event-meta-item">📍 ${event.location}</div>
          </div>
          
          <div class="event-info">
            <div class="event-info-item">👥 ${event.currentParticipants}/${event.maxParticipants} participants</div>
            <div class="event-info-item">🎯 Organized by ${event.organizer}</div>
          </div>
          
          <button 
            class="btn ${isRegistered ? 'btn-outline' : 'btn-primary'}" 
            onclick="app.toggleRegister('${event.id}')"
            style="margin-top: 1rem;">
            ${isRegistered ? 'Unregister' : 'Register Now'}
          </button>
        </div>
      </div>
    `;
  }

  renderMain() {
    let content = '';
    
    switch (this.currentTab) {
      case 'discover':
        content = this.renderDiscoverTab();
        break;
      case 'registered':
        content = this.renderRegisteredTab();
        break;
      case 'bookmarks':
        content = this.renderBookmarksTab();
        break;
    }
    
    return `
      <div class="app-bar">
        <h1 class="app-bar-title">UNIS</h1>
        <div class="app-bar-actions">
          <button class="icon-btn" onclick="app.showThemeMenu = true; app.render()">
            ${this.theme === 'light' ? '☀️' : this.theme === 'dark' ? '🌙' : '📱'}
          </button>
          <button class="icon-btn" onclick="app.showSettings()">⚙️</button>
        </div>
      </div>
      
      <div class="content">${content}</div>
      
      <nav class="bottom-nav">
        <button class="nav-item ${this.currentTab === 'discover' ? 'active' : ''}" 
                onclick="app.switchTab('discover')">
          <span class="nav-icon">🔍</span>
          Discover
        </button>
        <button class="nav-item ${this.currentTab === 'registered' ? 'active' : ''}" 
                onclick="app.switchTab('registered')">
          <span class="nav-icon">✓</span>
          Registered
        </button>
        <button class="nav-item ${this.currentTab === 'bookmarks' ? 'active' : ''}" 
                onclick="app.switchTab('bookmarks')">
          <span class="nav-icon">★</span>
          Bookmarks
        </button>
      </nav>
      
      ${this.renderThemeMenu()}
    `;
  }

  render() {
    const app = document.getElementById('app');
    
    let html = '';
    
    switch (this.currentScreen) {
      case 'splash':
        html = this.renderSplash();
        break;
      case 'login':
        html = this.renderLogin();
        break;
      case 'signup':
        html = this.renderSignup();
        break;
      case 'main':
        html = this.renderMain();
        break;
      case 'settings':
        html = this.renderSettings();
        break;
      case 'eventDetails':
        html = this.renderEventDetails();
        break;
    }
    
    app.innerHTML = html;
  }
}

// Initialize App
const app = new EventHubApp();
