# 🧪 Testing Guide - Bulk Email Sender

## ✅ Application Status Check

### Test if Application is Running
```powershell
curl http://localhost:8080/email/count
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Email count retrieved successfully",
  "data": 0
}
```

---

## 📤 API Testing with PowerShell

### 1. Test File Upload

**Using Sample CSV:**
```powershell
$file = Get-Item "D:\email bulk sender\emailbulksender\sample-emails.csv"
Invoke-WebRequest -Uri "http://localhost:8080/email/upload" -Method POST -Form @{file=$file}
```

**Expected Response:**
```json
{
  "success": true,
  "message": "File uploaded successfully. Found 6 email addresses",
  "data": 6
}
```

### 2. Test Email Count

```powershell
curl http://localhost:8080/email/count
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Email count retrieved successfully",
  "data": 6
}
```

### 3. Test Send Emails

```powershell
$body = @{
    subject = "Test Email Subject"
    message = "This is a test email message from the bulk email sender."
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/email/send" -Method POST -Body $body -ContentType "application/json"
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Emails sending started successfully to 6 recipients"
}
```

### 4. Test Clear Email List

```powershell
Invoke-WebRequest -Uri "http://localhost:8080/email/clear" -Method DELETE
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Email list cleared successfully"
}
```

---

## 🌐 Frontend Testing

### Manual Testing Steps

1. **Open Application**
   - Navigate to: http://localhost:8080
   - Verify page loads with proper styling

2. **Test File Upload**
   - Click "Choose File"
   - Select `sample-emails.csv`
   - Click "Upload File"
   - Verify success message appears
   - Verify email count displays

3. **Test Email Sending**
   - Enter subject: "Test Email"
   - Enter message: "This is a test message"
   - Click "Send Bulk Emails"
   - Confirm the action
   - Verify success message appears

4. **Check Console Logs**
   - Look at terminal where application is running
   - Should see: "Starting to send X emails"
   - Should see individual email statuses
   - Should see: "Bulk email sending completed"

---

## 📝 Creating Test Files

### Create CSV Test File

**test-emails.csv:**
```csv
test1@example.com
test2@example.com
test3@example.com
```

**PowerShell Command:**
```powershell
@"
test1@example.com
test2@example.com
test3@example.com
"@ | Out-File -FilePath "test-emails.csv" -Encoding UTF8
```

### Create Excel Test File (Manual)

1. Open Excel
2. Enter emails in Column A:
   - A1: test1@example.com
   - A2: test2@example.com
   - A3: test3@example.com
3. Save as: `test-emails.xlsx`

---

## 🔍 Validation Tests

### Test Invalid File Format

```powershell
# Create a text file
"test@example.com" | Out-File "test.txt"
$file = Get-Item "test.txt"
Invoke-WebRequest -Uri "http://localhost:8080/email/upload" -Method POST -Form @{file=$file}
```

**Expected Response:**
```json
{
  "success": false,
  "message": "Unsupported file format. Please upload .xlsx or .csv file"
}
```

### Test Empty File

```powershell
# Create empty CSV
"" | Out-File "empty.csv"
$file = Get-Item "empty.csv"
Invoke-WebRequest -Uri "http://localhost:8080/email/upload" -Method POST -Form @{file=$file}
```

**Expected Response:**
```json
{
  "success": false,
  "message": "No valid email addresses found in the file"
}
```

### Test Invalid Emails

**invalid-emails.csv:**
```
not-an-email
invalid@
@missing.com
spaces in email@test.com
```

These should be filtered out automatically.

### Test Missing Subject

```powershell
$body = @{
    subject = ""
    message = "Test message"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/email/send" -Method POST -Body $body -ContentType "application/json"
```

**Expected Response:**
```json
{
  "success": false,
  "message": "Email subject is required"
}
```

### Test Missing Message

```powershell
$body = @{
    subject = "Test Subject"
    message = ""
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/email/send" -Method POST -Body $body -ContentType "application/json"
```

**Expected Response:**
```json
{
  "success": false,
  "message": "Email message is required"
}
```

### Test Send Without Upload

```powershell
# First clear the list
Invoke-WebRequest -Uri "http://localhost:8080/email/clear" -Method DELETE

# Then try to send
$body = @{
    subject = "Test"
    message = "Test"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/email/send" -Method POST -Body $body -ContentType "application/json"
```

**Expected Response:**
```json
{
  "success": false,
  "message": "Please upload a file with email addresses first"
}
```

---

## 🎯 Complete Test Scenario

### Full Workflow Test

```powershell
# 1. Clear any existing data
Write-Host "Step 1: Clearing email list..."
Invoke-WebRequest -Uri "http://localhost:8080/email/clear" -Method DELETE

# 2. Upload sample file
Write-Host "Step 2: Uploading email list..."
$file = Get-Item "D:\email bulk sender\emailbulksender\sample-emails.csv"
Invoke-WebRequest -Uri "http://localhost:8080/email/upload" -Method POST -Form @{file=$file}

# 3. Check count
Write-Host "Step 3: Checking email count..."
curl http://localhost:8080/email/count

# 4. Send emails
Write-Host "Step 4: Sending bulk emails..."
$body = @{
    subject = "Automated Test Email"
    message = "This is an automated test email from the bulk email sender application."
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/email/send" -Method POST -Body $body -ContentType "application/json"

Write-Host "Test completed! Check application logs for details."
```

---

## 📊 Performance Testing

### Test with Multiple Emails

**Create large email list:**
```powershell
1..100 | ForEach-Object { "test$_@example.com" } | Out-File "large-list.csv" -Encoding UTF8
```

**Upload and send:**
```powershell
$file = Get-Item "large-list.csv"
Invoke-WebRequest -Uri "http://localhost:8080/email/upload" -Method POST -Form @{file=$file}

$body = @{
    subject = "Bulk Test"
    message = "Testing with 100 emails"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/email/send" -Method POST -Body $body -ContentType "application/json"
```

**Monitor logs:**
- Watch terminal for progress
- Check success/failure count
- Verify async processing

---

## 🐛 Debugging Tests

### Enable Debug Logging

Add to `application.properties`:
```properties
logging.level.com.example.emailbulksender=DEBUG
logging.level.org.springframework.mail=DEBUG
```

### Test Gmail Connection

```powershell
# Try sending to a single email first
# This tests Gmail credentials without bulk sending
```

### Check Port Availability

```powershell
netstat -ano | findstr :8080
```

---

## ✅ Test Checklist

- [ ] Application starts without errors
- [ ] Can access http://localhost:8080
- [ ] Can upload CSV file
- [ ] Can upload Excel file
- [ ] Invalid files are rejected
- [ ] Email validation works
- [ ] Empty subject rejected
- [ ] Empty message rejected
- [ ] Send without upload rejected
- [ ] Emails send asynchronously
- [ ] Success messages display
- [ ] Error messages display
- [ ] Console logs show details
- [ ] Can clear email list
- [ ] Can check email count

---

## 📧 Gmail Testing Notes

**IMPORTANT:** Before testing actual email sending:

1. Configure Gmail credentials in `application.properties`
2. Use App Password (not regular password)
3. Start with test emails you control
4. Monitor Gmail sending limits
5. Check spam folder for test emails

**Test Email Setup:**
```properties
spring.mail.username=your-test-email@gmail.com
spring.mail.password=your-app-password
```

**Send to yourself first:**
Create `my-email.csv`:
```
your-email@gmail.com
```

---

## 🎉 Test Results

### Expected Behavior

✅ File upload: < 1 second  
✅ Email validation: Instant  
✅ API response: < 100ms  
✅ Async sending: Background  
✅ UI response: Instant  

### Success Indicators

- Green success messages
- Proper email count display
- Console logs show progress
- No errors in terminal
- Emails appear in recipient inboxes

---

**Happy Testing! 🧪✨**

