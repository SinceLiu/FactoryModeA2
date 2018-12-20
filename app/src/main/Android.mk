
LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_JAVA_LIBRARIES += telephony-common 

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res
LOCAL_RESOURCE_DIR += frameworks/support/swipeback/res
LOCAL_STATIC_JAVA_LIBRARIES := swipeback
LOCAL_AAPT_FLAGS := \
	--auto-add-overlay \
	--extra-packages com.readboy.swipeback

LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_SRC_FILES += $(call all-subdir-java-files)
LOCAL_PACKAGE_NAME := FactoryMode
LOCAL_CERTIFICATE := platform
LOCAL_DEX_PREOPT := false

LOCAL_OVERRIDES_PACKAGES := ValidationTools

include $(BUILD_PACKAGE)

