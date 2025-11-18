// Use relative URL to work on both localhost and production
const API_BASE_URL = '/email';

let emailsUploaded = false;
let attachmentUploaded = false;
let manualEmails = [];

// File input change handler
document.getElementById('fileInput').addEventListener('change', function(e) {
    const file = e.target.files[0];
    if (file) {
        document.getElementById('fileName').textContent = file.name;
        document.getElementById('fileInfo').style.display = 'flex';
        document.getElementById('uploadArea').style.display = 'none';
    }
});

// Attachment input change handler
document.getElementById('attachmentInput').addEventListener('change', function(e) {
    const file = e.target.files[0];
    if (file) {
        uploadAttachment(file);
    }
});

// Character counter
document.getElementById('message').addEventListener('input', function(e) {
    const count = e.target.value.length;
    document.getElementById('charCount').textContent = count;
});

// Manual email input - press Enter to add
document.addEventListener('DOMContentLoaded', function() {
    const manualInput = document.getElementById('manualEmailInput');
    if (manualInput) {
        manualInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                addManualEmail();
            }
        });
    }
});

/**
 * Add email manually
 */
function addManualEmail() {
    const input = document.getElementById('manualEmailInput');
    const email = input.value.trim();

    if (!email) {
        return;
    }

    // Validate email
    const emailRegex = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    if (!emailRegex.test(email)) {
        alert('Please enter a valid email address');
        return;
    }

    // Check for duplicates
    if (manualEmails.includes(email)) {
        alert('This email has already been added');
        return;
    }

    // Add to array
    manualEmails.push(email);

    // Update UI
    renderManualEmails();

    // Clear input
    input.value = '';

    // Send to backend
    syncManualEmailsToBackend();
}

/**
 * Remove manual email
 */
function removeManualEmail(email) {
    manualEmails = manualEmails.filter(e => e !== email);
    renderManualEmails();
    if (manualEmails.length > 0) {
        syncManualEmailsToBackend();
    }
}

/**
 * Render manual emails as tags
 */
function renderManualEmails() {
    const container = document.getElementById('manualEmailList');

    if (manualEmails.length === 0) {
        container.innerHTML = '';
        return;
    }

    container.innerHTML = manualEmails.map(email =>
        `<div class="email-tag">
            ${email}
            <i class="fas fa-times" onclick="removeManualEmail('${email}')"></i>
        </div>`
    ).join('');
}

/**
 * Sync manual emails to backend
 */
async function syncManualEmailsToBackend() {
    if (manualEmails.length === 0) return;

    try {
        const response = await fetch(`${API_BASE_URL}/add-emails`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(manualEmails),
            signal: AbortSignal.timeout(10000) // 10 second timeout
        });

        if (!response.ok) {
            throw new Error(`Server returned ${response.status}`);
        }

        const result = await response.json();

        if (result.success) {
            emailsUploaded = true;
            updateEmailCount(result.data);
            document.getElementById('step2').classList.add('active');
        }
    } catch (error) {
        console.error('Error syncing manual emails:', error);
        // Don't show error to user for manual email sync - they can still try sending
    }
}

/**
 * Update email count display
 */
function updateEmailCount(count) {
    const emailCount = document.getElementById('emailCount');
    emailCount.innerHTML = `<i class="fas fa-check-circle"></i> ${count} email addresses loaded and ready to send!`;
    emailCount.style.display = 'block';
}

/**
 * Remove selected email list file
 */
function removeFile() {
    document.getElementById('fileInput').value = '';
    document.getElementById('fileInfo').style.display = 'none';
    document.getElementById('uploadArea').style.display = 'flex';
    document.getElementById('fileName').textContent = '';
}

/**
 * Upload Excel/CSV file
 */
async function uploadFile() {
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];
    const uploadStatus = document.getElementById('uploadStatus');
    const emailCount = document.getElementById('emailCount');

    // Reset status
    uploadStatus.className = 'status-message';
    uploadStatus.style.display = 'none';
    emailCount.style.display = 'none';

    if (!file) {
        showStatus(uploadStatus, 'error', '❌ Please select a file first');
        return;
    }

    // Validate file type
    const fileName = file.name.toLowerCase();
    if (!fileName.endsWith('.xlsx') && !fileName.endsWith('.xls') && !fileName.endsWith('.csv')) {
        showStatus(uploadStatus, 'error', '❌ Please upload a valid Excel (.xlsx, .xls) or CSV (.csv) file');
        return;
    }

    // Create FormData
    const formData = new FormData();
    formData.append('file', file);

    try {
        // Show loading
        showStatus(uploadStatus, 'info', '⏳ Uploading and processing file...');

        const response = await fetch(`${API_BASE_URL}/upload`, {
            method: 'POST',
            body: formData,
            // Add timeout and error handling
            signal: AbortSignal.timeout(30000) // 30 second timeout
        });

        if (!response.ok) {
            throw new Error(`Server returned ${response.status}: ${response.statusText}`);
        }

        const result = await response.json();

        if (result.success) {
            showStatus(uploadStatus, 'success', '✅ ' + result.message);
            updateEmailCount(result.data);
            emailsUploaded = true;

            // Activate step 2
            document.getElementById('step2').classList.add('active');

            // Scroll to compose section
            document.getElementById('composeCard').scrollIntoView({ behavior: 'smooth', block: 'start' });
        } else {
            showStatus(uploadStatus, 'error', '❌ ' + (result.message || 'Failed to upload file'));
            emailsUploaded = false;
        }
    } catch (error) {
        console.error('Upload error:', error);
        let errorMessage = 'Error uploading file';

        if (error.name === 'AbortError' || error.name === 'TimeoutError') {
            errorMessage = 'Upload timeout - file might be too large or server is slow';
        } else if (error.message.includes('Failed to fetch')) {
            errorMessage = 'Cannot connect to server. Please check if the app is running.';
        } else {
            errorMessage = error.message;
        }

        showStatus(uploadStatus, 'error', '❌ ' + errorMessage);
        emailsUploaded = false;
    }
}

/**
 * Upload attachment file
 */
async function uploadAttachment(file) {
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch(`${API_BASE_URL}/upload-attachment`, {
            method: 'POST',
            body: formData,
            signal: AbortSignal.timeout(30000) // 30 second timeout
        });

        if (!response.ok) {
            throw new Error(`Server returned ${response.status}: ${response.statusText}`);
        }

        const result = await response.json();

        if (result.success) {
            // Show attachment info
            document.getElementById('attachmentName').textContent = result.data;
            document.getElementById('attachmentSize').textContent = formatFileSize(file.size);
            document.getElementById('attachmentInfo').style.display = 'flex';
            document.getElementById('attachmentUpload').style.display = 'none';
            attachmentUploaded = true;
        } else {
            alert('Failed to upload attachment: ' + result.message);
            document.getElementById('attachmentInput').value = '';
        }
    } catch (error) {
        console.error('Attachment upload error:', error);
        let errorMessage = 'Error uploading attachment';

        if (error.name === 'AbortError' || error.name === 'TimeoutError') {
            errorMessage = 'Upload timeout - file might be too large';
        } else if (error.message.includes('Failed to fetch')) {
            errorMessage = 'Cannot connect to server';
        } else {
            errorMessage = error.message;
        }

        alert(errorMessage);
        document.getElementById('attachmentInput').value = '';
    }
}

/**
 * Remove attachment
 */
async function removeAttachment() {
    try {
        const response = await fetch(`${API_BASE_URL}/attachment`, {
            method: 'DELETE'
        });

        if (response.ok) {
            document.getElementById('attachmentInput').value = '';
            document.getElementById('attachmentInfo').style.display = 'none';
            document.getElementById('attachmentUpload').style.display = 'block';
            attachmentUploaded = false;
        }
    } catch (error) {
        console.error('Error removing attachment:', error);
    }
}

/**
 * Format file size
 */
function formatFileSize(bytes) {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(2) + ' KB';
    return (bytes / (1024 * 1024)).toFixed(2) + ' MB';
}

/**
 * Send bulk emails
 */
async function sendEmails() {
    const subject = document.getElementById('subject').value.trim();
    const message = document.getElementById('message').value.trim();
    const sendStatus = document.getElementById('sendStatus');

    // Reset status
    sendStatus.className = 'status-message';
    sendStatus.style.display = 'none';

    // Validation
    if (!emailsUploaded) {
        showStatus(sendStatus, 'error', '❌ Please upload a file with email addresses first');
        document.getElementById('uploadCard').scrollIntoView({ behavior: 'smooth' });
        return;
    }

    if (!subject) {
        showStatus(sendStatus, 'error', '❌ Please enter email subject');
        document.getElementById('subject').focus();
        return;
    }

    if (!message) {
        showStatus(sendStatus, 'error', '❌ Please enter email message');
        document.getElementById('message').focus();
        return;
    }

    // Confirm before sending
    const attachmentText = attachmentUploaded ? ' with attachment' : '';
    if (!confirm(`Are you sure you want to send emails to all recipients${attachmentText}?`)) {
        return;
    }

    try {
        // Show loader
        showLoader('Sending emails...', 'Please wait while we send emails to all recipients');

        // Activate step 3
        document.getElementById('step3').classList.add('active');

        const response = await fetch(`${API_BASE_URL}/send`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                subject: subject,
                message: message,
                hasAttachment: attachmentUploaded
            })
        });

        const result = await response.json();

        // Hide loader
        hideLoader();

        if (response.ok && result.success) {
            // Show success modal
            showSuccessModal(result.message);

            // Clear status
            sendStatus.style.display = 'none';
        } else {
            showStatus(sendStatus, 'error', '❌ ' + (result.message || 'Failed to send emails'));
        }
    } catch (error) {
        hideLoader();
        showStatus(sendStatus, 'error', '❌ Error sending emails: ' + error.message);
    }
}

/**
 * Show loader
 */
function showLoader(title, subtitle) {
    document.getElementById('loaderText').textContent = title;
    document.getElementById('loaderSubtext').textContent = subtitle;
    document.getElementById('loaderOverlay').classList.add('show');
}

/**
 * Hide loader
 */
function hideLoader() {
    document.getElementById('loaderOverlay').classList.remove('show');
}

/**
 * Show success modal
 */
function showSuccessModal(message) {
    document.getElementById('successMessage').textContent = message;
    document.getElementById('successModal').classList.add('show');
}

/**
 * Close success modal
 */
function closeSuccessModal() {
    document.getElementById('successModal').classList.remove('show');
}

/**
 * Clear form
 */
function clearForm() {
    if (confirm('Are you sure you want to clear the form?')) {
        document.getElementById('subject').value = '';
        document.getElementById('message').value = '';
        document.getElementById('charCount').textContent = '0';

        // Clear attachment if exists
        if (attachmentUploaded) {
            removeAttachment();
        }

        document.getElementById('sendStatus').style.display = 'none';
    }
}

/**
 * Show status message
 */
function showStatus(element, type, message) {
    element.className = `status-message ${type}`;
    element.innerHTML = message.replace(/\n/g, '<br>');
    element.style.display = 'block';

    // Auto-scroll to status
    setTimeout(() => {
        element.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }, 100);
}

/**
 * Initialize
 */
document.addEventListener('DOMContentLoaded', function() {
    // Check if attachment exists on load
    fetch(`${API_BASE_URL}/attachment-info`)
        .then(response => response.json())
        .then(result => {
            if (result.success && result.data) {
                document.getElementById('attachmentName').textContent = result.data;
                document.getElementById('attachmentInfo').style.display = 'flex';
                document.getElementById('attachmentUpload').style.display = 'none';
                attachmentUploaded = true;
            }
        })
        .catch(error => console.error('Error checking attachment:', error));
});

