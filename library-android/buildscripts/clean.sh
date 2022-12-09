#!/bin/bash -e

# Clean python-for-android caches
p4a clean_all

#Remove built binaries and assets
rm -rf ../py-includes
rm -rf ../src/main/jniLibs
rm -rf ../src/main/assets/*/modules
rm -rf ../src/main/assets/*/stdlib.zip

# Remove build tools
rm -rf sdk
