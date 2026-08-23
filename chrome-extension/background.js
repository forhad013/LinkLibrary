/**
 * LinkLibrary Chrome Extension - Background Service Worker
 *
 * Handles:
 * - Message passing between popup and WASM app
 * - Local storage of links using chrome.storage API
 * - Background sync with backend (future)
 * - Extension lifecycle events
 */

// Listen for messages from popup and other extension contexts
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
  console.log('Background received message:', request);

  if (request.action === "saveLink") {
    saveLink(request.data).then(sendResponse);
    return true; // Keep message channel open for async response
  }

  if (request.action === "getLinks") {
    getLinks().then(sendResponse);
    return true; // Keep message channel open for async response
  }

  if (request.action === "deleteLink") {
    deleteLink(request.linkId).then(sendResponse);
    return true; // Keep message channel open for async response
  }
});

/**
 * Save a link to chrome.storage.local
 */
async function saveLink(linkData) {
  try {
    const result = await chrome.storage.local.get(['links']);
    const links = result.links || [];

    const newLink = {
      ...linkData,
      id: crypto.randomUUID(),
      createdAt: new Date().toISOString(),
      synced: false,
      tags: linkData.tags || [],
      isFavorite: linkData.isFavorite || false
    };

    links.push(newLink);
    await chrome.storage.local.set({ links });

    console.log('Link saved successfully:', newLink);

    // Try to sync with WASM app if it's open
    try {
      const tabs = await chrome.tabs.query({url: "http://localhost:8080/*"});
      if (tabs.length > 0) {
        await chrome.tabs.sendMessage(tabs[0].id, {
          action: "addLink",
          data: newLink
        });
        console.log('Link synced to WASM app');
      }
    } catch (e) {
      console.log('WASM app not open, will sync later:', e.message);
    }

    return { success: true, link: newLink };
  } catch (error) {
    console.error('Error saving link:', error);
    return { success: false, error: error.message };
  }
}

/**
 * Get all links from chrome.storage.local
 */
async function getLinks() {
  try {
    const result = await chrome.storage.local.get(['links']);
    const links = result.links || [];
    return { success: true, links: links };
  } catch (error) {
    console.error('Error getting links:', error);
    return { success: false, error: error.message };
  }
}

/**
 * Delete a link from chrome.storage.local
 */
async function deleteLink(linkId) {
  try {
    const result = await chrome.storage.local.get(['links']);
    const links = result.links || [];
    const filteredLinks = links.filter(link => link.id !== linkId);
    await chrome.storage.local.set({ links: filteredLinks });
    return { success: true };
  } catch (error) {
    console.error('Error deleting link:', error);
    return { success: false, error: error.message };
  }
}

/**
 * Listen for extension installation
 */
chrome.runtime.onInstalled.addListener(() => {
  console.log('LinkLibrary extension installed');
  // Initialize storage if needed
  chrome.storage.local.get(['links'], (result) => {
    if (!result.links) {
      chrome.storage.local.set({ links: [] });
    }
  });
});

/**
 * Listen for network coming back online
 * (For future offline-first sync functionality)
 */
chrome.offline.addListener(() => {
  console.log('Network offline, pausing sync');
});

chrome.online.addListener(() => {
  console.log('Network online, resuming sync');
  // Future: trigger background sync here
});

// Periodic background sync (every 5 minutes)
// For future backend integration
setInterval(async () => {
  console.log('Running periodic sync...');
  // Future: Implement backend sync here
}, 5 * 60 * 1000);