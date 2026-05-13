# CodeMagic Environment Variables Setup Guide

To enable Google Play publishing and signing in CodeMagic, follow these steps:

## 1. Google Play Service Account Credentials

### Create Service Account:
1. Go to [Google Play Console](https://play.google.com/console)
2. Navigate to **Settings** → **API and Services** → **Service Accounts**
3. Click **Create Service Account**
4. Complete the setup and create a new key (JSON format)
5. Download the JSON file

### Add to CodeMagic:
1. In CodeMagic, go to **Settings** → **Environment variables**
2. Click **Create new**
3. Variable name: `GCLOUD_SERVICE_ACCOUNT_CREDENTIALS`
4. Open the downloaded JSON file, copy its entire contents
5. Paste into the value field
6. Make it a **Secure** variable
7. Save

## 2. Android Keystore for Signing (Release Builds)

### Generate Keystore:
```bash
keytool -genkey -v -keystore release.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias music_player_key
```

### Convert to Base64:
```bash
base64 release.keystore | tr -d '\n' | pbcopy
```

### Add to CodeMagic:
1. In CodeMagic, go to **Settings** → **Environment variables**
2. Create these variables:
   - `KEYSTORE_BASE64`: (paste the base64 output)
   - `KEYSTORE_PASSWORD`: (your keystore password)
   - `KEYSTORE_KEY_ALIAS`: `music_player_key`
   - `KEYSTORE_KEY_PASSWORD`: (your key password)
3. Make all as **Secure** variables
4. Save

## 3. Build Workflow

### Development Builds (Automatic on push/PR):
- Builds debug and release APKs
- Runs unit tests
- Runs lint checks
- Sends email notifications

### Release Builds (Automatic on git tag):
- Triggered by tags matching `v1.0.0` format
- Builds signed APK and App Bundle
- Publishes to Google Play production
- Sends email notifications

## 4. Trigger a Build

### Development Build:
```bash
git push origin main
```

### Release Build:
```bash
git tag v1.0.0
git push origin v1.0.0
```

## 📝 Notes:
- Keep keystore and credentials secure
- Don't commit credentials to version control
- Use environment variables for all sensitive data
- Email notifications require SMTP configuration in CodeMagic

## 🚀 Once Setup Complete:
Your CI/CD pipeline will automatically:
1. Build on every push/PR
2. Run tests automatically
3. Generate APK and App Bundle
4. Publish to Google Play on tagged releases
