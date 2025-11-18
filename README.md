# Bulk Email Sender Pro 📧

A professional full-stack application for sending bulk emails with attachments using Spring Boot backend and modern HTML/CSS/JavaScript frontend.

## ✨ Features

- ✅ Upload Excel (.xlsx, .xls) or CSV files with email addresses
- ✅ **Add email addresses manually with a beautiful tag interface**
- ✅ Extract and validate email addresses automatically
- ✅ **Attach files to your emails**
- ✅ Send bulk emails asynchronously in the background
- ✅ **Beautiful HTML email templates with visual UI**
- ✅ **Modern, professional UI with loading animations**
- ✅ Real-time status updates and progress tracking
- ✅ Character counter for email messages
- ✅ Gmail SMTP integration
- ✅ Responsive design for all devices
- ✅ **Docker support for easy deployment**
- ✅ **Deploy to Render with one click**

## 🎨 Modern UI

- Beautiful gradient design with smooth animations
- Progress step indicator
- Email tag management system
- Drag-and-drop file upload
- Success/Error modals
- Loading spinner feedback
- Professional card-based layout
- Font Awesome icons
- Mobile-responsive design

## 📋 Prerequisites

- Java 21 or higher
- Maven
- Gmail account with App Password enabled

## 🔐 Gmail Setup

1. Go to your Google Account settings
2. Enable 2-Factor Authentication
3. Generate an App Password:
   - Go to: https://myaccount.google.com/apppasswords
   - Select "Mail" and "Other (Custom name)"
   - Click "Generate"
   - Copy the 16-character password

## ⚙️ Configuration

1. Open `src/main/resources/application.properties`
2. Update the following properties:

```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-16-char-app-password
```

## 🚀 How to Run

1. **Build the project:**
   ```bash
   mvnw clean install
   ```

2. **Run the application:**
   ```bash
   mvnw spring-boot:run
   ```

3. **Access the application:**
   - Open your browser and go to: http://localhost:8080

## 📝 How to Use

### Step 1: Upload Contact List

1. Click the upload area or drag and drop your Excel/CSV file
2. File must contain email addresses in the **first column**
3. Click "Upload Contact List" button
4. See confirmation with number of emails found

**Example format:**
```
email1@example.com
email2@example.com
email3@example.com
```

### Step 2: Compose Your Email

1. Enter email **Subject** (required)
2. Write your email **Message** (required)
3. **Optional:** Upload an attachment file
   - Click "Choose File" in the attachment section
   - Select any file to attach to emails
   - See file name and size displayed
4. Review your message

### Step 3: Send Emails

1. Click "Send Bulk Emails" button
2. Confirm the action in the popup
3. Emails will be sent in the background
4. Check console/terminal for detailed progress logs

## 🔌 API Endpoints

### 1. Upload Email File
```http
POST /email/upload
Content-Type: multipart/form-data
Parameter: file (Excel/CSV file)
```

### 2. Upload Attachment File (NEW!)
```http
POST /email/upload-attachment
Content-Type: multipart/form-data
Parameter: file (any file type)
```

### 3. Send Bulk Emails
```http
POST /email/send
Content-Type: application/json
Body:
{
  "subject": "Your Subject",
  "message": "Your Message",
  "hasAttachment": true
}
```

### 4. Get Email Count
```http
GET /email/count
```

### 5. Get Attachment Info (NEW!)
```http
GET /email/attachment-info
```

### 6. Remove Attachment (NEW!)
```http
DELETE /email/attachment
```

### 7. Clear Email List
```http
DELETE /email/clear
```

## 📂 Project Structure

```
emailbulksender/
├── src/
│   ├── main/
│   │   ├── java/com/example/emailbulksender/
│   │   │   ├── controller/
│   │   │   │   └── EmailController.java         # REST endpoints + attachment handling
│   │   │   ├── service/
│   │   │   │   ├── EmailService.java            # Async email sending with attachments
│   │   │   │   └── ExcelReaderService.java      # File parsing
│   │   │   ├── dto/
│   │   │   │   ├── EmailRequest.java            # Request DTO with attachment fields
│   │   │   │   └── ApiResponse.java             # Response DTO
│   │   │   └── EmailbulksenderApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html                   # Modern UI with Font Awesome
│   │       │   ├── styles.css                   # Professional styling
│   │       │   └── script.js                    # Enhanced JavaScript
│   │       └── application.properties
└── pom.xml                                      # Fixed Apache POI dependencies
```

## 🛠️ Technologies Used

### Backend
- Spring Boot 3.5.7
- Spring Mail (JavaMailSender with MIME support)
- Apache POI 5.2.5 (Excel reading - **FIXED**)
- Lombok
- Java 21

### Frontend
- HTML5
- CSS3 (Modern animations and transitions)
- Vanilla JavaScript (ES6+)
- Font Awesome 6.4.0 icons

## 🔧 Fixed Issues

### Apache POI ClassInitializationError - RESOLVED ✅

The `XSSFWorkbook` initialization error has been fixed by:
- Syncing Apache POI versions to 5.2.5
- Adding required dependencies:
  - `poi-ooxml-schemas`
  - `xmlbeans`
  - `commons-collections4`
  - `commons-io`

## ⚠️ Important Notes

1. **Email Sending is Asynchronous:** Emails are sent in the background using `@Async`. The API returns immediately with a success message.

2. **Gmail Rate Limits:**
   - Free Gmail: 500 emails per day
   - Google Workspace: 2,000 emails per day

3. **Attachment Storage:** 
   - Attachments are temporarily stored in `uploads/attachments/` directory
   - Files are automatically cleaned up when clearing or uploading new files

4. **File Size Limits:**
   - Contact list: 10MB max
   - Attachments: 10MB max (configurable)

5. **Email Validation:** The application validates email format before adding to the list.

6. **Supported Formats:**
   - Contact lists: .xlsx, .xls, .csv
   - Attachments: All file types

## 🎯 Production Considerations

- Use a database to store email lists and attachments
- Implement queue system (RabbitMQ/Kafka) for large volumes
- Add email templates with HTML support
- Add retry mechanism for failed emails
- Implement rate limiting and throttling
- Add authentication/authorization
- Use cloud storage for attachments (S3, Azure Blob)
- Implement email tracking (opens, clicks)
- Add scheduling feature
- Implement email campaigns management

## 🐛 Troubleshooting

### Emails not sending?
- Check your Gmail credentials in `application.properties`
- Ensure you're using App Password (not regular password)
- Check application logs for errors
- Verify internet connection

### File upload fails?
- Ensure file is .xlsx, .xls, or .csv format
- Check file size (max 10MB)
- Verify first column contains email addresses

### Excel upload not working?
- Ensure all Apache POI dependencies are installed
- Try using CSV format instead
- Check console logs for specific errors

## 🐳 Docker Deployment

### Build Docker Image
```bash
docker build -t email-bulk-sender .
```

### Run Docker Container
```bash
docker run -p 8080:8080 \
  -e SPRING_MAIL_USERNAME=your-email@gmail.com \
  -e SPRING_MAIL_PASSWORD=your-app-password \
  email-bulk-sender
```

### Docker Compose (Optional)
Create `docker-compose.yml`:
```yaml
version: '3.8'
services:
  email-sender:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_MAIL_USERNAME=your-email@gmail.com
      - SPRING_MAIL_PASSWORD=your-app-password
    volumes:
      - ./uploads:/app/uploads
```

Run: `docker-compose up`

## 🌐 Deploy to Render

### Quick Deploy (Recommended)

1. **Push to GitHub:**
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git
   git push -u origin main
   ```

2. **Create Render Web Service:**
   - Go to [Render Dashboard](https://dashboard.render.com/)
   - Click "New +" → "Web Service"
   - Connect your GitHub repository
   - Select your repo

3. **Configure:**
   - **Name:** email-bulk-sender
   - **Environment:** Docker
   - **Region:** Choose closest to you
   - **Branch:** main
   - **Instance Type:** Free (or paid)

4. **Set Environment Variables:**
   ```
   SPRING_MAIL_HOST=smtp.gmail.com
   SPRING_MAIL_PORT=587
   SPRING_MAIL_USERNAME=your-email@gmail.com
   SPRING_MAIL_PASSWORD=your-app-password
   SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true
   SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true
   PORT=8080
   ```

5. **Deploy!** 🚀

📖 **Detailed Guide:** See [RENDER_DEPLOYMENT.md](RENDER_DEPLOYMENT.md) for complete instructions.

### Your App Will Be Live At:
```
https://your-service-name.onrender.com
```

## 📄 License

This project is open source and available under the MIT License.

## 👨‍💻 Developer

Built with ❤️ using Spring Boot and modern web technologies

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

---

**⭐ Star this repo if you find it helpful!**

- Check that email addresses are in the first column
- Verify file size is under 10MB

### POI ClassInitializationError?
- **FIXED!** Dependencies have been corrected
- If issue persists, run: `mvnw clean install`

### Attachment not sending?
- Check file size (max 10MB)
- Verify attachment uploaded successfully (green message)
- Check console logs for attachment-related errors

### UI not loading properly?
- Clear browser cache (Ctrl + F5)
- Check if Font Awesome CDN is accessible
- Try incognito/private mode

## 📊 Performance Tips

1. **For large email lists (1000+):**
   - Send in batches
   - Monitor Gmail sending limits
   - Consider using email service providers (SendGrid, AWS SES)

2. **For large attachments:**
   - Compress files before attaching
   - Consider using cloud links instead
   - Stay within email size limits

3. **For better deliverability:**
   - Use valid sender email
   - Avoid spam trigger words
   - Include unsubscribe option
   - Authenticate your domain (SPF, DKIM)

## 🌟 New Features Highlights

### 1. File Attachments
- Upload any file type
- Preview file name and size
- Easy removal option
- Automatic cleanup

### 2. Enhanced UI
- Progress step indicator
- Smooth animations
- Professional gradient design
- Better status messages
- Character counter
- Responsive layout

### 3. Better User Experience
- Drag and drop file upload
- Visual file information
- Clear form button
- Auto-scroll to sections
- Confirmation dialogs

## 📸 UI Improvements

### Before:
- Basic gradient background
- Simple cards
- Text-only interface

### After:
- Modern gradient with animations
- Professional card design
- Font Awesome icons throughout
- Progress tracking
- Animated upload areas
- Better status messages
- Responsive tips section

## 🎓 Usage Tips

💡 **Tip 1:** Always test with a small list first (2-3 emails)  
💡 **Tip 2:** Check spam folder if emails don't arrive  
💡 **Tip 3:** Use descriptive subjects to avoid spam filters  
💡 **Tip 4:** Compress large attachments to reduce size  
💡 **Tip 5:** Monitor console logs for detailed progress  

## 📜 License

This project is open source and available for educational purposes.

## 🤝 Support

For issues or questions:
1. Check application logs for detailed error messages
2. Review this README for common solutions
3. Verify Gmail configuration
4. Test with sample data first

## 🎉 What's New in This Version

✨ File attachment support  
✨ Modern, professional UI redesign  
✨ Font Awesome icons  
✨ Progress step indicator  
✨ Character counter  
✨ Better error messages  
✨ Smooth animations  
✨ **Fixed Apache POI initialization error**  
✨ Enhanced user experience  
✨ Mobile responsive design  

---

**Made with ❤️ for bulk email campaigns**

**Application Status:** ✅ Running and Ready to Use!  
**Access URL:** http://localhost:8080

