import { initializeApp } from 'https://www.gstatic.com/firebasejs/10.8.0/firebase-app.js';
import { getAuth, GoogleAuthProvider, signInWithPopup, signOut } from 'https://www.gstatic.com/firebasejs/10.8.0/firebase-auth.js';

const firebaseConfig = {
  apiKey: "AIzaSyAKQrLL6N9WipKyv9nG0Arojnlszydnd-s",
  authDomain: "unis-84b5a.firebaseapp.com",
  projectId: "unis-84b5a",
  storageBucket: "unis-84b5a.firebasestorage.app",
  messagingSenderId: "136657022894",
  appId: "1:136657022894:android:03ed6314d22a315564623f"
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const provider = new GoogleAuthProvider();

window.AuthManager = {
  isLoggedIn: () => auth.currentUser !== null,
  
  getCurrentUserEmail: () => auth.currentUser?.email,
  
  signInWithGoogle: async () => {
    try {
      const result = await signInWithPopup(auth, provider);
      const email = result.user.email;
      
      if (!email.endsWith('@bvrithyderabad.edu.in')) {
        await signOut(auth);
        throw new Error('Please use your college email ID');
      }
      
      return { success: true, email };
    } catch (error) {
      await signOut(auth).catch(() => {});
      return { success: false, error: error.message };
    }
  },
  
  signOut: async () => {
    await signOut(auth);
  }
};
