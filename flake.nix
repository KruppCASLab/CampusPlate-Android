{
  description = "Android Development Flake";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          config = {
            android_sdk.accept_license = true;
            allowUnfree = true;
            programs.adb.enable = true;
          };
        };

        androidSdk = pkgs.androidenv.composeAndroidPackages {
          platformVersions = [ "36" "35" "34" "33" ];
          buildToolsVersions = [ "36.0.0" "35.0.0" "34.0.0" "33.0.2" ];
          abiVersions = [ "x86_64" "arm64-v8a" ];
          includeEmulator = true;
          includeSystemImages = true;
          useGoogleAPIs = true;
          includeNDK = false;
        };
      in {
        devShells.default = pkgs.mkShell {
          ANDROID_SDK_ROOT = "${androidSdk.androidsdk}/libexec/android-sdk";
          ANDROID_HOME     = "${androidSdk.androidsdk}/libexec/android-sdk";
          JAVA_HOME        = pkgs.jdk21.home;

          packages = with pkgs; [
            # Core
            jdk21
            kotlin
            git
            unzip
            which

            # Android
            androidSdk.androidsdk
            android-tools        # adb, fastboot
            android-studio
          ];

          shellHook = ''
            export ANDROID_USER_HOME="$PWD/.android"
            export ANDROID_AVD_HOME=$PWD/.android/avd
            mkdir -p "$ANDROID_USER_HOME"
            export GRADLE_USER_HOME=$PWD/.gradle
            export PATH="$GRADLE_HOME:$ANDROID_HOME/emulator:$ANDROID_HOME/platform-tools:$PATH"
            export GRADLE_OPTS="-Dorg.gradle.daemon=false"
            export GRADLE_OPTS="$GRADLE_OPTS -Dorg.gradle.project.android.aapt2FromMavenOverride=$ANDROID_SDK_ROOT/build-tools/36.0.0/aapt2"

            export LD_LIBRARY_PATH="''${LD_LIBRARY_PATH}''${LD_LIBRARY_PATH:+:}${pkgs.stdenv.cc.cc.lib}/lib"
            export LD_LIBRARY_PATH="''${LD_LIBRARY_PATH}''${LD_LIBRARY_PATH:+:}/run/opengl-driver/lib:/run/opengl-driver-32/lib"

            echo "Android Studio + SDK ready"
            echo "SDK: $ANDROID_HOME"
          '';
        };
      }
    );
}
