#!/bin/bash -e

# Include variables
. ./include.sh

# Export variables for p4a
export ANDROIDSDK="$SDK_DIR/android-sdk-linux"
export ANDROID_HOME=$ANDROIDSDK
export ANDROID_SDK_ROOT=$ANDROIDSDK
export ANDROIDNDK="$SDK_DIR/android-ndk-${v_ndk}"
export ANDROIDAPI="$v_target_sdk"
export NDKAPI="$v_minimum_sdk"

# Check if directories exist
if [ ! -d "$ANDROIDSDK" ] || [ ! -d "$ANDROIDNDK" ]; then
    echo "Android SDK or NDK not found. Try running download.sh."
    exit 1
fi

# Run python-for-android
p4a create --dist_name=python-android \
--requirements=python3,aniparse \
--blacklist-requirements=sqlite3,android,libffi,openssl \
--arch=x86_64 --arch=arm64-v8a

# Check if distribution was built correctly
if [ ! -d "$HOME/.local/share/python-for-android/dists/python-android" ]; then
    echo "python-for-android distribution not found."
    exit 1
fi

ARCHS="x86_64 arm64-v8a"

for arch in $ARCHS; do
    P4A_BUILD_DIR="$HOME/.local/share/python-for-android/build/other_builds/python3/${arch}__ndk_target_21/python3"
    P4A_DIST_DIR="$HOME/.local/share/python-for-android/dists/python-android/_python_bundle__${arch}/_python_bundle"
    mkdir -p "../src/main/jniLibs/$arch"
    mkdir -p "../py-includes/$arch"
    mkdir -p "../src/main/assets/python/${arch}"
    cp "$P4A_BUILD_DIR/android-build/libpython3.9.so" "../src/main/jniLibs/${arch}/libpython3.9.so"
    cp -r "$P4A_BUILD_DIR/Include" "../py-includes/$arch"
    cp "$P4A_DIST_DIR/stdlib.zip" "../src/main/assets/python/${arch}/stdlib.zip"
    cp -r "$P4A_DIST_DIR/modules" "../src/main/assets/python/${arch}/modules"
    cp -r "$P4A_DIST_DIR/site-packages" "../src/main/assets/python/${arch}/site-packages"
done
