/**
 * LinkLibrary Chrome Extension - Popup Interface
 *
 * Handles:
 * - Getting current tab information
 * - Displaying link preview
 * - Saving links with optional metadata
 * - Opening the main LinkLibrary app
 * - Displaying link count
 */

// Initialize popup
document.addEventListener('DOMContentLoaded', async () => {
  try {
    // Get current tab information
    const [tab] = await chrome.tabs.query({active: true, currentWindow: true});

    // Display page information
    document.getElementById('page-title').textContent = tab.title || 'Untitled';
    document.getElementById('page-url').textContent = tab.url;

    // Update link count
    updateLinkCount();

    // Set up save button
    document.getElementById('save-btn').addEventListener('click', () => saveLink(tab));

    // Set up open app button
    document.getElementById('open-app-btn').addEventListener('click', openLinkLibraryApp);

  } catch (error) {
    console.error('Error initializing popup:', error);
    showError('Failed to load page information');
  }
});

/**
 * Save the current link to LinkLibrary
 */
async function saveLink(tab) {
  const saveBtn = document.getElementById('save-btn');
  const status = document.getElementById('status');

  // Clear previous status
  status.innerHTML = '';

  // Disable button and show loading
  saveBtn.disabled = true;
  saveBtn.textContent = 'Saving...';

  try {
    // Get optional metadata from form
    const description = document.getElementById('description').value.trim();
    const isFavorite = document.getElementById('is-favorite').checked;

    // Prepare link data
    const linkData = {
      url: tab.url,
      title: tab.title || 'Untitled',
      description: description,
      tags: [], // Future: add tag selection
      isFavorite: isFavorite,
      faviconUrl: tab.favIconUrl || null
    };

    // Send message to background script
    const response = await chrome.runtime.sendMessage({
      action: "saveLink",
      data: linkData
    });

    if (response.success) {
      // Show success message
      status.innerHTML = '<div class="success">✓ Link saved successfully!</div>';
      saveBtn.textContent = 'Saved!';

      // Reset form
      document.getElementById('description').value = '';
      document.getElementById('is-favorite').checked = false;

      // Update link count
      updateLinkCount();

      // Re-enable button after delay
      setTimeout(() => {
        saveBtn.disabled = false;
        saveBtn.textContent = 'Save Link';
      }, 2000);

    } else {
      throw new Error(response.error || 'Failed to save link');
    }

  } catch (error) {
    console.error('Error saving link:', error);
    showError('Failed to save link: ' + error.message);

    // Re-enable button
    saveBtn.disabled = false;
    saveBtn.textContent = 'Save Link';
  }
}

/**
 * Open the main LinkLibrary app
 */
async function openLinkLibraryApp() {
  try {
    // Try to find existing LinkLibrary tab
    const tabs = await chrome.tabs.query({url: "http://localhost:8080/*"});

    if (tabs.length > 0) {
      // Focus existing tab
      await chrome.tabs.update(tabs[0].id, {active: true});
      await chrome.windows.update(tabs[0].windowId, {focused: true});
    } else {
      // Open new tab with LinkLibrary app
      await chrome.tabs.create({url: 'http://localhost:8080'});
    }

    // Close popup
    window.close();

  } catch (error) {
    console.error('Error opening app:', error);
    showError('Failed to open LinkLibrary app');
  }
}

/**
 * Update the total link count
 */
async function updateLinkCount() {
  try {
    const result = await chrome.storage.local.get(['links']);
    const links = result.links || [];
    const count = links.length;

    document.getElementById('link-count').textContent =
      `${count} link${count !== 1 ? 's' : ''} saved`;

  } catch (error) {
    console.error('Error getting link count:', error);
  }
}

/**
 * Show error message
 */
function showError(message) {
  const status = document.getElementById('status');
  status.innerHTML = `<div class="error">${message}</div>`;

  // Auto-hide error after 5 seconds
  setTimeout(() => {
    if (status.innerHTML.includes('error')) {
      status.innerHTML = '';
    }
  }, 5000);
}

/**
 * Handle keyboard shortcuts
 */
document.addEventListener('keydown', (event) => {
  // Press Enter to save (when not in text input)
  if (event.key === 'Enter' && event.target.tagName !== 'INPUT') {
    document.getElementById('save-btn').click();
  }

  // Press Escape to close popup
  if (event.key === 'Escape') {
    window.close();
  }
});