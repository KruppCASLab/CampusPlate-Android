# Campus Plate - Android Client

## About
The Android client is currently built in Java and using Views. In the future, we plan on migrating to Kotlin and using Compose..

## Building
To build the project, you do need Android Studio installed. Once you clone the repository, you should be able to open the project with Android Studio and run the project on an emulator.

## Building Android APK
To build the Android application APK file, go to Build -> Generate Signed Bundle or APK. Then check APK. Select the keystore path and cpkey0 as the key. If needed, generate a new key but you should use the existing. Then choose release as the build type. The APK will be created, take the APK and upload to server.

## Adding Endpoint
If you are deploying Campus Plate at your university and would like your university's web service endpoint added to the application, you can submit a pull request containing the change.
