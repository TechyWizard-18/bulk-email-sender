# 🚀 QUICK START - Bulk Email Sender

## ⚡ 3 Steps to Start Sending Emails

### 1️⃣ Configure Gmail (5 minutes)

**Get Gmail App Password:**
1. Go to: https://myaccount.google.com/apppasswords
2. Generate an App Password
3. Copy the 16-character password

**Update Configuration:**
Open: `src/main/resources/application.properties`

Replace:
```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### 2️⃣ Start Application (1 minute)

```powershell
cd "D:\email bulk sender\emailbulksender"
.\mvnw.cmd spring-boot:run
```

Wait for: `Started EmailbulksenderApplication`

### 3️⃣ Access Application

Open browser: **http://localhost:8080**

---

## 📧 Send Your First Email

1. **Prepare Email List**
   - Create Excel or CSV file
   - Put email addresses in first column
   - Save as `emails.xlsx` or `emails.csv`

2. **Upload File**
   - Click "Choose File"
   - Select your file
   - Click "Upload File"

3. **Compose & Send**
   - Enter Subject
   - Write Message
   - Click "Send Bulk Emails"

**Done!** ✅

---

## 📋 Sample Email List Format

Create a file called `my-emails.csv`:
```
email1@gmail.com
email2@yahoo.com
email3@company.com
```

Or use the included `sample-emails.csv` for testing.

---

## ❓ Common Issues

**Emails not sending?**
- Check Gmail username and app password in `application.properties`
- Make sure you're using App Password, not regular password

**Port 8080 already in use?**
- Change port in `application.properties`: `server.port=9090`

**Need help?**
- Check `SETUP_GUIDE.md` for detailed troubleshooting

---

## ✅ Application is Running!

Your application is currently **LIVE** at: http://localhost:8080

Just configure Gmail and you're ready to send emails! 🎉

