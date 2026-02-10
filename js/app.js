if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('/sw.js')
      .then(() => console.log('Service Worker registered'))
      .catch(err => console.error('SW registration failed:', err));
  });
}

class EventHubApp {
  constructor() {
    this.events = mockEvents;
    this.registeredEvents = new Set(JSON.parse(localStorage.getItem('registered') || '[]'));
    this.bookmarkedEvents = new Set(JSON.parse(localStorage.getItem('bookmarked') || '[]'));
    this.currentTab = 'discover';
    this.init();
  }

  init() {
    this.render();
  }

  saveState() {
    localStorage.setItem('registered', JSON.stringify([...this.registeredEvents]));
    localStorage.setItem('bookmarked', JSON.stringify([...this.bookmarkedEvents]));
  }

  toggleRegister(eventId) {
    if (this.registeredEvents.has(eventId)) {
      this.registeredEvents.delete(eventId);
    } else {
      this.registeredEvents.add(eventId);
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

  renderEvent(event) {
    const isRegistered = this.registeredEvents.has(event.id);
    const isBookmarked = this.bookmarkedEvents.has(event.id);
    
    return `
      <div class="event-card">
        <div style="display: flex; justify-content: space-between; align-items: start;">
          <h2>${event.title}</h2>
          <button class="icon-btn" onclick="app.toggleBookmark('${event.id}')">
            ${isBookmarked ? '★' : '☆'}
          </button>
        </div>
        <span class="category-badge">${event.category}</span>
        <p style="margin: 1rem 0; color: #666;">${event.description}</p>
        <div class="event-meta">
          <div class="event-meta-item">📅 ${event.date}</div>
          <div class="event-meta-item">🕐 ${event.time}</div>
          <div class="event-meta-item">📍 ${event.location}</div>
        </div>
        <div style="margin: 1rem 0; color: #666;">
          <div>👥 ${event.currentParticipants}/${event.maxParticipants} participants</div>
          <div>🎯 Organized by ${event.organizer}</div>
        </div>
        <button 
          class="btn ${isRegistered ? 'btn-outline' : 'btn-primary'}" 
          onclick="app.toggleRegister('${event.id}')"
          style="width: 100%;">
          ${isRegistered ? 'Unregister' : 'Register'}
        </button>
      </div>
    `;
  }

  render() {
    const app = document.getElementById('app');
    
    let content = '';
    if (this.currentTab === 'discover') {
      content = this.events.map(e => this.renderEvent(e)).join('');
    } else if (this.currentTab === 'registered') {
      const registered = this.events.filter(e => this.registeredEvents.has(e.id));
      content = registered.length ? registered.map(e => this.renderEvent(e)).join('') : 
        '<div style="text-align: center; padding: 2rem; color: #666;">No registered events</div>';
    } else if (this.currentTab === 'bookmarks') {
      const bookmarked = this.events.filter(e => this.bookmarkedEvents.has(e.id));
      content = bookmarked.length ? bookmarked.map(e => this.renderEvent(e)).join('') : 
        '<div style="text-align: center; padding: 2rem; color: #666;">No bookmarked events</div>';
    }

    app.innerHTML = `
      <div class="app-bar">
        <h1>EventHub</h1>
        <button class="icon-btn">⚙️</button>
      </div>
      <div class="content">${content}</div>
      <nav class="bottom-nav">
        <button class="nav-item ${this.currentTab === 'discover' ? 'active' : ''}" onclick="app.switchTab('discover')">
          <span>🔍</span>
          Discover
        </button>
        <button class="nav-item ${this.currentTab === 'registered' ? 'active' : ''}" onclick="app.switchTab('registered')">
          <span>✓</span>
          Registered
        </button>
        <button class="nav-item ${this.currentTab === 'bookmarks' ? 'active' : ''}" onclick="app.switchTab('bookmarks')">
          <span>★</span>
          Bookmarks
        </button>
      </nav>
    `;
  }

  switchTab(tab) {
    this.currentTab = tab;
    this.render();
  }
}

const app = new EventHubApp();
