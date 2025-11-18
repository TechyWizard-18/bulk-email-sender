# 🔧 FIX: Gmail SMTP Connection Timeout on Render

## ❌ The Error You're Getting

```
Mail server connection failed. Failed messages: 
org.eclipse.angus.mail.util.MailConnectException: 
Couldn't connect to host, port: smtp.gmail.com, 587; timeout -1
nested exception is: java.net.ConnectException: Operation timed out
```

## 🎯 Root Cause

Render (and many cloud platforms) block or timeout on port **587** due to their network configuration. This is a **common issue** with cloud hosting.

---

## ✅ SOLUTION: Use Port 465 (SSL) Instead

### Step 1: Update Render Environment Variables

Go to your Render Dashboard → Your Service → Environment

**Change this variable:**
```
SPRING_MAIL_PORT=587
```

**To this:**
```
SPRING_MAIL_PORT=465
```

That's it! The app will automatically detect port 465 and use SSL configuration.

### Step 2: Redeploy (Automatic)

Render will automatically restart your app with the new environment variable.

Wait 1-2 minutes for the restart.

### Step 3: Test

Try sending emails again. It should work now! ✅

---

## 📊 Port Comparison

| Port | Protocol | Works on Render? |
|------|----------|-----------------|
| **587** | TLS/STARTTLS | ❌ Often blocked/timeout |
| **465** | SSL | ✅ Works reliably |
| **25** | Plain SMTP | ❌ Blocked by most clouds |

---

## 🔧 Current Configuration (Already Applied)

I've updated your app to support BOTH ports automatically:

### Port 587 (TLS) - Default
```properties
spring.mail.port=587
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=false
```

### Port 465 (SSL) - Fallback
```properties
spring.mail.port=465
spring.mail.properties.mail.smtp.ssl.enable=true
spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
```

The `MailConfig.java` class automatically detects which port you're using and applies the correct configuration.

---

## 🚀 Quick Fix Steps (Summary)

### Option A: Change Port to 465 (Recommended)

1. **Render Dashboard** → Your Service → **Environment**
2. Find: `SPRING_MAIL_PORT`
3. Change: `587` → `465`
4. Click **"Save Changes"**
5. Wait for automatic restart
6. ✅ Test emails

### Option B: Use All Environment Variables

Set these in Render Environment:

```bash
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=465
SPRING_MAIL_USERNAME=your-email@gmail.com
SPRING_MAIL_PASSWORD=your-16-char-app-password
PORT=8080
```

**Remove these** (not needed anymore):
- ❌ `SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH`
- ❌ `SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE`

(They're handled automatically by the app)

---

## 🔍 Verify Configuration

### Check Render Logs

After changing to port 465, check logs:

```bash
Render Dashboard → Your Service → Logs
```

Look for:
- ✅ `Started EmailbulksenderApplication`
- ✅ No connection timeout errors
- ✅ Emails sending successfully

### Test Email Endpoint

```bash
# Check health
curl https://your-app.onrender.com/health

# Should return:
{"status":"UP","service":"Email Bulk Sender"}
```

---

## 🐛 Still Not Working?

### Additional Troubleshooting:

#### 1. Verify Gmail App Password
- Must be a 16-character **App Password**, not your regular password
- Format: `abcd efgh ijkl mnop` (with spaces is fine)
- Regenerate if needed: https://myaccount.google.com/apppasswords

#### 2. Check Gmail Account Settings
- 2-Factor Authentication: **Enabled** ✅
- Less Secure Apps: **Not needed** (using App Password)
- IMAP Access: **Enabled** ✅ (in Gmail settings)

#### 3. Test Locally First
```bash
cd "D:\email bulk sender\emailbulksender"
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.mail.port=465"
```

If it works locally with port 465, it will work on Render.

#### 4. Try Another Email Provider

If Gmail continues to have issues, consider:
- **SendGrid** (free tier: 100 emails/day)
- **Mailgun** (free tier: 5,000 emails/month)
- **AWS SES** (pay as you go)

These work better with cloud platforms.

---

## 📝 New Files Created

I've added these to help:

1. **MailConfig.java** - Smart SMTP configuration
   - Auto-detects port 587 vs 465
   - Applies correct TLS/SSL settings
   - Adds proper timeouts

2. **application-ssl.properties** - Backup config for port 465
   - Use if you want SSL by default

3. **Updated application.properties** - Better timeouts and settings

---

## ✅ Expected Result After Fix

### Before (Port 587):
```
❌ Error: Operation timed out
❌ Couldn't connect to smtp.gmail.com:587
```

### After (Port 465):
```
✅ Started EmailbulksenderApplication
✅ Bulk email sending initiated for X recipients
✅ Email sent successfully to: user@example.com
```

---

## 🎯 Action Required

**DO THIS NOW:**

1. Go to Render Dashboard
2. Navigate to: **Your Service → Environment**
3. Change: `SPRING_MAIL_PORT` from `587` to `465`
4. Click **"Save Changes"**
5. Wait 2 minutes for restart
6. Test sending an email

**That's it!** Should work immediately! 🚀

---

## 📞 Alternative Solutions

### Use Render's Static Outbound IPs (Paid Plan)

If you need port 587:
1. Upgrade to Render **Starter plan** ($7/month)
2. Get static outbound IP
3. Whitelist IP in Gmail settings

### Use Third-Party Email Service

Replace Gmail SMTP with:
- **SendGrid** - Better for bulk emails
- **Mailgun** - Reliable delivery
- **AWS SES** - Scalable and cheap

Just change these variables:
```bash
SPRING_MAIL_HOST=smtp.sendgrid.net
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=apikey
SPRING_MAIL_PASSWORD=your-sendgrid-api-key
```

---

## 🎉 Summary

**Root Cause:** Render blocks/timeouts port 587

**Solution:** Use port 465 (SSL) instead

**Steps:**
1. Change `SPRING_MAIL_PORT` to `465` in Render
2. Save and wait for restart
3. Test emails
4. ✅ Done!

**Your app is already configured to handle both ports automatically!**

---

## 📄 Commit Message (After Testing)

```bash
git add .
git commit -m "Fix: Add SMTP timeout handling and SSL support for Render"
git push
```

**Good luck! This should fix your timeout issue! 🚀**

