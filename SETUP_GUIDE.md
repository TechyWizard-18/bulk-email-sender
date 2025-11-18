# 📧 Bulk Email Sender - Complete Setup Guide

## ✅ Application Status
Your application has been successfully created and is currently running!

## 🎯 What's Been Built

### Backend (Spring Boot)
- ✅ Email Controller with REST APIs
- ✅ Email Service with async support
- ✅ Excel Reader Service (supports .xlsx, .xls, .csv)
- ✅ DTOs (EmailRequest, ApiResponse)
- ✅ Proper project structure

### Frontend (HTML/CSS/JavaScript)
- ✅ Clean, modern UI with gradient design
- ✅ File upload functionality
- ✅ Email composition form
- ✅ Real-time status messages
- ✅ Responsive design

### Technologies Used
- Spring Boot 3.5.7
- Apache POI (Excel reading)
- JavaMailSender (Gmail SMTP)
- Lombok
- Pure HTML/CSS/JavaScript (no frameworks)

## 🚀 Quick Start

### Step 1: Configure Gmail SMTP

1. **Enable 2-Factor Authentication** on your Gmail account
   - Go to: https://myaccount.google.com/security

2. **Generate App Password**
   - Go to: https://myaccount.google.com/apppasswords
   - Select "Mail" and "Other (Custom name)"
   - Click "Generate"
   - Copy the 16-character password (remove spaces)

3. **Update Configuration**
   - Open: `src/main/resources/application.properties`
   - Replace these values:
     ```properties
     spring.mail.username=your-email@gmail.com
     spring.mail.password=your-16-char-app-password
     ```

### Step 2: Run the Application

**Option A: Using Maven Wrapper (Recommended)**
```powershell
cd "D:\email bulk sender\emailbulksender"
.\mvnw.cmd spring-boot:run
```

**Option B: Using Maven**
```powershell
cd "D:\email bulk sender\emailbulksender"
mvn spring-boot:run
```

### Step 3: Access the Application

Open your browser and go to:
```
http://localhost:8080
```

## 📝 How to Use

### 1. Prepare Your Email List

Create an Excel (.xlsx) or CSV (.csv) file with email addresses in the **first column**:

**Example (emails.xlsx or emails.csv):**
```
email@example.com
user1@domain.com
user2@company.com
test@example.org
```

💡 **Sample file included:** `sample-emails.csv`

### 2. Upload Your File

1. Click the "Choose File" button
2. Select your Excel or CSV file
3. Click "Upload File"
4. Wait for confirmation showing the number of emails found

### 3. Compose Your Email

1. Enter the email **Subject**
2. Write your email **Message**
3. Click "Send Bulk Emails"
4. Confirm the action

### 4. Monitor Progress

- Emails are sent **asynchronously** in the background
- Check the console/terminal for detailed logs
- Each email send operation is logged with success/failure status

## 🔌 API Endpoints

### 1. Upload Email File
```http
POST http://localhost:8080/email/upload
Content-Type: multipart/form-data

Parameter: file (Excel/CSV file)
```

**Response:**
```json
{
  "success": true,
  "message": "File uploaded successfully. Found 10 email addresses",
  "data": 10
}
```

### 2. Send Bulk Emails
```http
POST http://localhost:8080/email/send
Content-Type: application/json

Body:
{
  "subject": "Your Subject Here",
  "message": "Your message content here"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Emails sending started successfully to 10 recipients"
}
```

### 3. Get Email Count
```http
GET http://localhost:8080/email/count
```

**Response:**
```json
{
  "success": true,
  "message": "Email count retrieved successfully",
  "data": 10
}
```

### 4. Clear Email List
```http
DELETE http://localhost:8080/email/clear
```

**Response:**
```json
{
  "success": true,
  "message": "Email list cleared successfully"
}
```

## 📂 Project Structure

```
emailbulksender/
├── src/
│   ├── main/
│   │   ├── java/com/example/emailbulksender/
│   │   │   ├── controller/
│   │   │   │   └── EmailController.java          # REST API endpoints
│   │   │   ├── service/
│   │   │   │   ├── EmailService.java            # Async email sending
│   │   │   │   └── ExcelReaderService.java      # File reading logic
│   │   │   ├── dto/
│   │   │   │   ├── EmailRequest.java            # Request DTO
│   │   │   │   └── ApiResponse.java             # Response DTO
│   │   │   └── EmailbulksenderApplication.java  # Main application
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html                   # Frontend UI
│   │       │   ├── styles.css                   # Styling
│   │       │   └── script.js                    # JavaScript logic
│   │       └── application.properties           # Configuration
├── sample-emails.csv                            # Sample email list
├── README.md                                    # Full documentation
├── SETUP_GUIDE.md                               # This guide
└── pom.xml                                      # Maven dependencies
```

## 🔧 Testing the Application

### Test 1: Upload Sample File
```powershell
# PowerShell test
Invoke-WebRequest -Uri "http://localhost:8080/email/upload" -Method POST -Form @{file=Get-Item "sample-emails.csv"}
```

### Test 2: Check Email Count
```powershell
curl http://localhost:8080/email/count
```

### Test 3: Send Test Email (using sample data)
```powershell
$body = @{
    subject = "Test Email"
    message = "This is a test message"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/email/send" -Method POST -Body $body -ContentType "application/json"
```

## ⚠️ Important Notes

### Gmail Sending Limits
- **Free Gmail:** 500 emails per day
- **Google Workspace:** 2,000 emails per day
- Exceeding limits may result in temporary suspension

### Email Validation
- Only valid email formats are accepted
- Invalid emails are automatically filtered out
- Email format: `name@domain.com`

### File Requirements
- **Supported formats:** .xlsx, .xls, .csv
- **Max file size:** 10MB
- **Email location:** First column only
- **Encoding:** UTF-8 for CSV files

### Async Email Sending
- Emails are sent in the background
- API returns immediately with success message
- Check console logs for individual email status
- Failed emails are logged but don't stop the process

## 🐛 Troubleshooting

### Problem: Emails not sending

**Solution 1: Check Gmail credentials**
```properties
# In application.properties
spring.mail.username=your-real-email@gmail.com
spring.mail.password=your-actual-app-password
```

**Solution 2: Verify Gmail App Password**
- Must be 16 characters (no spaces)
- Generated from Google Account settings
- Not your regular Gmail password

**Solution 3: Check console logs**
```powershell
# Look for errors in the terminal where the app is running
# Errors will show: "Failed to send email to: xyz@example.com"
```

### Problem: File upload fails

**Check 1: File format**
- Must be .xlsx, .xls, or .csv
- Emails must be in the first column

**Check 2: File size**
- Maximum 10MB
- Check in application.properties:
```properties
spring.servlet.multipart.max-file-size=10MB
```

**Check 3: Email format**
- Valid format: `name@domain.com`
- No spaces or special characters

### Problem: Application won't start

**Solution 1: Check if port 8080 is available**
```powershell
netstat -ano | findstr :8080
```

**Solution 2: Change port (if needed)**
```properties
# In application.properties
server.port=9090
```

**Solution 3: Clean and rebuild**
```powershell
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

### Problem: Frontend not loading

**Check 1: Application is running**
```powershell
curl http://localhost:8080/email/count
```

**Check 2: Browser cache**
- Clear browser cache
- Try incognito/private mode
- Hard refresh: Ctrl + F5

**Check 3: CORS issues**
- Controller has `@CrossOrigin` enabled
- Check browser console for errors

## 🎨 Customization

### Change Email Sender Name
```properties
# In application.properties
spring.mail.properties.mail.smtp.from=Your Name <your-email@gmail.com>
```

### Change Port
```properties
server.port=9090
```

### Adjust File Size Limit
```properties
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB
```

### Modify Frontend Colors
Edit `src/main/resources/static/styles.css`:
```css
/* Change gradient colors */
body {
    background: linear-gradient(135deg, #your-color-1 0%, #your-color-2 100%);
}
```

## 📊 Monitoring

### Check Application Logs
Look for these log messages:
- `Starting to send X emails` - Bulk sending started
- `Email sent successfully to: email@example.com` - Individual success
- `Failed to send email to: email@example.com` - Individual failure
- `Bulk email sending completed. Success: X, Failed: Y` - Summary

### View Real-time Logs
```powershell
# Watch the terminal where the application is running
# Logs appear in real-time as emails are sent
```

## 🚀 Production Deployment

### Before Deploying to Production:

1. **Use Database for Email Lists**
   - Current: In-memory list (lost on restart)
   - Production: Store in database (MySQL/PostgreSQL)

2. **Implement Queue System**
   - Use RabbitMQ or Apache Kafka
   - Better handling of large volumes

3. **Add Authentication**
   - Implement Spring Security
   - Protect API endpoints
   - Add user management

4. **Email Templates**
   - Use Thymeleaf for HTML emails
   - Support dynamic content

5. **Error Handling & Retry**
   - Retry failed emails automatically
   - Store failed emails for review

6. **Rate Limiting**
   - Prevent abuse
   - Comply with SMTP limits

7. **Monitoring & Analytics**
   - Track sent/failed emails
   - Generate reports
   - Email open/click tracking

## 📞 Support

### Check Logs First
Most issues can be diagnosed from console logs.

### Common Log Messages

**Success:**
```
Email sent successfully to: user@example.com
Bulk email sending completed. Success: 10, Failed: 0
```

**Failure:**
```
Failed to send email to: user@example.com. Error: Authentication failed
```

### Need Help?

1. Check console logs for detailed error messages
2. Verify Gmail configuration
3. Test with sample-emails.csv first
4. Check the troubleshooting section above

## ✅ Current Status

Your application is **fully functional** and ready to use!

- ✅ Backend APIs working
- ✅ Frontend UI accessible
- ✅ File upload functional
- ✅ Email validation working
- ✅ Async sending configured

**Next Step:** Configure your Gmail credentials and start sending!

---

**Happy Email Sending! 📧**

