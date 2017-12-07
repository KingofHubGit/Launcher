LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)


LOCAL_STATIC_JAVA_LIBRARIES := \
        android-support-v4 \
		lib_gson_2_6_1

LOCAL_PACKAGE_NAME := xgdLauncher 
LOCAL_CERTIFICATE := platform

LOCAL_OVERRIDES_PACKAGES := Launcher2 \
                            Trebuchet

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := lib_gson_2_6_1:libs/gson-2.6.1.jar


include $(BUILD_MULTI_PREBUILT)


# Use the folloing include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
