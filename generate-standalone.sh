#!/bin/bash

ANDROID_NDK_DIR=/opt/android-ndk-beta
ANDROID_API=26
ANDROID_STL=gnustl
INSTALL_PREFIX=/opt/ndk-standalone-beta
declare -a COMPILE_ARCHITECTURES=("arm")

pushd ${ANDROID_NDK_DIR}
for ARCH in "${COMPILE_ARCHITECTURES[@]}"
do
    build/tools/make_standalone_toolchain.py \
        --arch ${ARCH} \
        --api ${ANDROID_API} \
        --stl ${ANDROID_STL} \
        --install-dir ${INSTALL_PREFIX}-${ARCH}
done
popd
