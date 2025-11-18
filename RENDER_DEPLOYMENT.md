# 🚀 Deploy to Render - Complete Guide

## 📋 Prerequisites
- GitHub account
- Render account (free tier works)
- Gmail App Password for email sending

---

## 🔧 Step 1: Prepare Your Repository

### Push to GitHub:
```bash
git init
git add .
git commit -m "Initial commit - Email Bulk Sender"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git
git push -u origin main
```

---

## 🌐 Step 2: Deploy on Render

### A. Create New Web Service
1. Go to [Render Dashboard](https://dashboard.render.com/)
2. Click **"New +"** → **"Web Service"**
3. Connect your GitHub repository
4. Select your `emailbulksender` repository

### B. Configure Service
Fill in these settings:

**Basic Settings:**
- **Name:** `email-bulk-sender` (or your choice)
- **Region:** Choose closest to you
- **Branch:** `main`
- **Root Directory:** Leave empty (or `.` if needed)
- **Environment:** `Docker`
- **Dockerfile Path:** `Dockerfile`

**Instance Type:**
- Select **Free** (or paid for better performance)

---

## 🔐 Step 3: Set Environment Variables

In Render dashboard, go to **Environment** tab and add:

### Required Variables:

| Key | Value | Example |
|-----|-------|---------|
| `SPRING_MAIL_HOST` | `smtp.gmail.com` | smtp.gmail.com |
| `SPRING_MAIL_PORT` | `587` or `465` | 587 |
| `SPRING_MAIL_USERNAME` | Your Gmail | yourname@gmail.com |
| `SPRING_MAIL_PASSWORD` | App Password | abcd efgh ijkl mnop |
| `PORT` | `8080` | 8080 |

**Note:** If port 587 times out, try port **465** (SSL) instead.

### How to Get Gmail App Password:
1. Go to Google Account → Security
2. Enable 2-Step Verification
3. Search "App passwords"
4. Generate password for "Mail"
5. Copy the 16-character password
6. Use it in `SPRING_MAIL_PASSWORD`

---

## 🚀 Step 4: Deploy

1. Click **"Create Web Service"**
2. Render will automatically:
   - Pull your code from GitHub
   - Build Docker image
   - Deploy the application
3. Wait 5-10 minutes for first deployment

---

## ✅ Step 5: Access Your Application

Your app will be available at:
```
https://email-bulk-sender.onrender.com
```
(Replace with your actual service name)

---

## 📝 Step 6: Test the Application

1. Open your Render URL
2. Upload an Excel/CSV file with emails
3. Or add emails manually
4. Enter subject and message
5. Click "Send Bulk Emails"
6. Check status loader

---

## ⚡ Important Notes

### Free Tier Limitations:
- App sleeps after 15 mins of inactivity
- First request after sleep takes 50+ seconds
- 750 hours/month free

### Upgrade to Paid ($7/month):
- No sleeping
- Always fast
- More resources

### Gmail Limits:
- Free Gmail: ~500 emails/day
- Be careful with bulk sending
- Consider using SendGrid or AWS SES for production

---

## 🔄 Auto-Deploy on Push

Render automatically redeploys when you push to GitHub:

```bash
git add .
git commit -m "Update feature"
git push
```

Render detects the push and rebuilds.

---

## 🐛 Troubleshooting

### Build Fails:
- Check Render logs
- Verify Dockerfile syntax
- Ensure pom.xml is correct

### App Doesn't Start:
- Check environment variables
- Verify Gmail credentials
- Check Render logs

### Email Not Sending:
- Verify Gmail App Password
- Check SMTP settings
- Enable "Less secure app access" if needed

### View Logs:
- Go to Render Dashboard
- Click your service
- Click "Logs" tab
- See real-time logs

---

## 📊 Monitor Your Application

### Render Dashboard Shows:
- ✅ Deploy status
- 📈 CPU/Memory usage
- 📝 Application logs
- 🔄 Auto-deploy history

---

## 🎉 Success!

Your email bulk sender is now live and accessible worldwide!

**Next Steps:**
1. Share the URL with users
2. Monitor usage in Render dashboard
3. Upgrade if needed for production use
4. Consider adding authentication for security

---

## 📞 Support

- Render Docs: https://render.com/docs
- Spring Boot Docs: https://spring.io/projects/spring-boot
- Apache POI Docs: https://poi.apache.org/

**Your app is production-ready! 🚀**

