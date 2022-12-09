# Python-for-Android paths
LOCAL_PATH := $(call my-dir)
PYTHON_FOR_ANDROID_PATH := $(LOCAL_PATH)/../../../python-for-android
GUEST_PYTHON_PATH := $(PYTHON_FOR_ANDROID_PATH)/build/other_builds/python3
PYTHON_PATH := $(GUEST_PYTHON_PATH)/x86_64__ndk_target_21/python3

# Build libaniparse.so
include $(CLEAR_VARS)
LOCAL_MODULE    := aniparse
LOCAL_SRC_FILES := pybridge.c
LOCAL_C_INCLUDES := $(PYTHON_PATH)/Include
LOCAL_LDLIBS := -llog -lpython3.9
LOCAL_LDFLAGS := -L$(PYTHON_PATH)/android-build
include $(BUILD_SHARED_LIBRARY)
