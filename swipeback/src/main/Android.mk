LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := swipeback
LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res
# LOCAL_JAVA_LIBRARIES := android-support-v4
include $(BUILD_STATIC_JAVA_LIBRARY)