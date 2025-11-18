# 🚀 Deployment Checklist for Render

Use this checklist to ensure smooth deployment to Render.

---

## ✅ Pre-Deployment Steps

- [ ] **Gmail App Password Created**
  - Go to https://myaccount.google.com/apppasswords
  - Enable 2FA first if not enabled
  - Generate app password for "Mail"
  - Save the 16-character password safely

- [ ] **Code Pushed to GitHub**
  ```bash
  git init
  git add .
  git commit -m "Initial deployment"
  git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git
  git push -u origin main
  ```

- [ ] **Sensitive Data Removed**
  - Check `application.properties` uses environment variables
  - No hardcoded passwords in code
  - `.env` file is in `.gitignore`

- [ ] **Test Locally First**
  ```bash
  mvn clean package
  java -jar target/emailbulksender-0.0.1-SNAPSHOT.jar
  ```
  - Test at http://localhost:8080
  - Upload CSV/Excel file
  - Add manual emails
  - Send test emails

---

## 🌐 Render Deployment Steps

### Step 1: Create Web Service
- [ ] Go to https://dashboard.render.com/
- [ ] Click "New +" → "Web Service"
- [ ] Connect GitHub repository
- [ ] Select your repository

### Step 2: Configure Service
- [ ] **Name:** `email-bulk-sender` (or your choice)
- [ ] **Region:** Select closest to you
- [ ] **Branch:** `main`
- [ ] **Root Directory:** Leave empty
- [ ] **Environment:** `Docker`
- [ ] **Dockerfile Path:** `Dockerfile`
- [ ] **Instance Type:** Free (or Starter $7/month)

### Step 3: Environment Variables
Add these in the "Environment" section:

- [ ] `SPRING_MAIL_HOST` = `smtp.gmail.com`
- [ ] `SPRING_MAIL_PORT` = `587`
- [ ] `SPRING_MAIL_USERNAME` = `your-email@gmail.com`
- [ ] `SPRING_MAIL_PASSWORD` = `your-app-password`
- [ ] `SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH` = `true`
- [ ] `SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE` = `true`
- [ ] `PORT` = `8080`

### Step 4: Deploy
- [ ] Click "Create Web Service"
- [ ] Wait 5-10 minutes for first build
- [ ] Check build logs for errors
- [ ] Note your app URL: `https://your-app.onrender.com`

---

## ✅ Post-Deployment Testing

- [ ] **Access the Application**
  - Open: `https://your-app.onrender.com`
  - Page loads correctly
  - UI looks good

- [ ] **Test File Upload**
  - Upload sample CSV file
  - Upload sample Excel file
  - Check success message
  - Verify email count

- [ ] **Test Manual Email Input**
  - Type email address
  - Click "Add" button
  - Tag appears correctly
  - Click X to remove

- [ ] **Test Email Sending**
  - Add test email addresses
  - Enter subject and message
  - Upload attachment (optional)
  - Click "Send Bulk Emails"
  - Loader appears
  - Success modal shows
  - Check recipient inbox

- [ ] **Check Logs**
  - Go to Render dashboard
  - Click "Logs" tab
  - Verify no errors
  - Check email sending logs

---

## 🔧 Troubleshooting

### Build Failed
- [ ] Check Dockerfile syntax
- [ ] Verify pom.xml is correct
- [ ] Check Render build logs
- [ ] Ensure Java 21 is specified

### App Won't Start
- [ ] Verify all environment variables are set
- [ ] Check Gmail credentials
- [ ] Review application logs in Render
- [ ] Ensure port 8080 is exposed

### Excel Upload Fails
- [ ] Check Apache POI version in pom.xml (should be 5.3.0)
- [ ] Verify commons-io version (should be 2.18.0)
- [ ] Check application logs for specific error

### Emails Not Sending
- [ ] Verify Gmail App Password is correct
- [ ] Check SMTP settings
- [ ] Review email service logs
- [ ] Test with one email first

### App Sleeps (Free Tier)
- [ ] This is normal on free tier
- [ ] First request takes 50+ seconds
- [ ] Consider upgrading to Starter plan ($7/month)
- [ ] Or use a cron job to ping every 14 minutes

---

## 📊 Monitoring

- [ ] **Set Up Health Checks**
  - Render automatically pings your app
  - Check "Health & Alerts" tab

- [ ] **Monitor Usage**
  - Check CPU/Memory usage
  - Monitor request logs
  - Track error rates

- [ ] **Set Up Alerts**
  - Email notifications for failures
  - Slack integration (optional)

---

## 🎉 Success Criteria

Your deployment is successful when:

✅ App loads at your Render URL  
✅ CSV and Excel uploads work  
✅ Manual email input works  
✅ Emails send successfully  
✅ No errors in logs  
✅ UI looks professional  
✅ Loader and modals work  
✅ Attachments can be uploaded  

---

## 📞 Need Help?

- **Render Docs:** https://render.com/docs
- **Render Community:** https://community.render.com/
- **Check Logs:** Render Dashboard → Your Service → Logs

---

**🎊 Congratulations on deploying your app!**

