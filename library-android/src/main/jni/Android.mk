# Python-for-Android paths
LOCAL_PATH := $(call my-dir)
INCLUDE_PATH := $(LOCAL_PATH)/../../../py-includes
JNILIBS_PATH := $(LOCAL_PATH)/../jniLibs

# Build libaniparse.so
include $(CLEAR_VARS)
LOCAL_MODULE    := aniparse
LOCAL_SRC_FILES := aniparse.c
LOCAL_C_INCLUDES := $(INCLUDE_PATH)/$(TARGET_ARCH_ABI)/Include
LOCAL_LDLIBS := -llog -lpython3.9
LOCAL_LDFLAGS := -L$(JNILIBS_PATH)/$(TARGET_ARCH_ABI)
include $(BUILD_SHARED_LIBRARY)
