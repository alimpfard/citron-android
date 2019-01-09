if [ ! -e citron ]; then
    git clone --single-branch -b android -recursive https://github.com/alimpfard/citron &&
    cd citron &&
    git apply ../citron.patch
fi

if [ ! -e bdwgc ]; then
    git clone https://github.com/ivmai/bdwgc &&
    cd bdwgc &&
    git clone git://github.com/ivmai/libatomic_ops.git &&
    git apply ../bdwgc.patch
    cd ..
fi

if [ ! -e libffi ]; then
    git clone https://github.com/libffi/libffi.git
fi

if [ ! -e pcre ]; then
    wget https://ftp.pcre.org/pub/pcre/pcre-8.42.zip &&
    unzip pcre-8.42.zip &&
    mv pcre-8.42 pcre
fi

ANDROID_NDK_DIR=/opt/ndk-standalone-beta
PARENT_DIR=$(realpath .)/build/compile
INSTALL_DIR=$(realpath .)/build/install

declare -a COMPILE_ARCHS=("arm64" "arm")

configure() {
    ./configure $@ --host="${COMPILER_PREFIX}" --prefix="${INSTALL_DIR}/${ABI_NAME}"
}

compile_gc() {
    cd bdwgc
    ./autogen.sh
    CFLAGS="${CFLAGS} -DGC_DONT_DEFINE_LINK_MAP" configure --enable-static
}

compile_pcre() {
    cd pcre
    configure --enable-pcre16 --enable-pcre32 --enable-jit --enable-unicode-properties
}

compile_ffi() {
    cd libffi
    ./autogen.sh
    configure --enable-static
}

make_ffi() {
    make clean
    make -j
    make install
    cd ..
}

make_pcre() {
    make clean
    make -j
    make install
    cd ..
}

compile_citron() {
    # ....
    cd citron
}

make_gc() {
    make clean
    make -j
    make install
    cd ..
}

make_citron() {
    make clean
    make jni
    make INSTALL_DIR=${INSTALL_DIR}/${ABI_NAME} install
    cd ..
}

copy_into_test_build() {
    if [ ! -e test/citron-test/app/src/main/assets ]; then
        mkdir -p test/citron-test/app/src/main/assets
    fi

    if [ ! -e test/citron-test/app/src/main/jniLibs/arm64-v8a ]; then
        mkdir -p test/citron-test/app/src/main/jniLibs/arm64-v8a;
    fi

    cp -r ${INSTALL_DIR}/${ABI_NAME}/share/Citron test/citron-test/app/src/main/assets;
    cp -r ${INSTALL_DIR}/${ABI_NAME}/lib/* test/citron-test/app/src/main/jniLibs/arm64-v8a/;
}

for ARCH in "${COMPILE_ARCHS[@]}"; do
    COMPILER_GROUP=""
    COMPILER_PREFIX=""
    case $ARCH in
        arm64 )
            COMPILER_GROUP=arm64
            ;;
        arm )
            COMPILER_GROUP=arm
            ;;
    esac

    STANDALONE_TOOLCHAIN="$ANDROID_NDK_DIR-$COMPILER_GROUP"
    ANDROID_NDK_BIN="$STANDALONE_TOOLCHAIN/bin"
    SYSROOT_DIR="$STANDALONE_TOOLCHAIN/sysroot"


    case $ARCH in
        arm64 )
            ABI_NAME=aarch64
            COMPILER_PREFIX=aarch64-linux-android
            ;;
        arm )
            ABI_NAME=armeabi
            COMPILER_PREFIX=arm-linux-androideabi
            ;;
    esac

    export CFLAGS="-g3 --sysroot=${SYSROOT_DIR} -I${INSTALL_DIR}/${ABI_NAME}/include -L${INSTALL_DIR}/${ABI_NAME}/lib"
    export CXXFLAGS="-g3 --sysroot=${SYSROOT_DIR} -I${INSTALL_DIR}/${ABI_NAME}/include -L${INSTALL_DIR}/${ABI_NAME}/lib"
    export CC=${ANDROID_NDK_BIN}/${COMPILER_PREFIX}-gcc
    export CPP="${ANDROID_NDK_BIN}/${COMPILER_PREFIX}-gcc -E"
    export CXX=${ANDROID_NDK_BIN}/${COMPILER_PREFIX}-g++
    export LD=${ANDROID_NDK_BIN}/${COMPILER_PREFIX}-ld
    export AR=${ANDROID_NDK_BIN}/${COMPILER_PREFIX}-ar
    export AS=${ANDROID_NDK_BIN}/${COMPILER_PREFIX}-as
    export RANLIB=${ANDROID_NDK_BIN}/${COMPILER_PREFIX}-ranlib
    export STRIP=${ANDROID_NDK_BIN}/${COMPILER_PREFIX}-strip

    echo "--------Compiling for $ARCH------------"
    compile_gc
    make_gc
    compile_ffi
    make_ffi
    compile_pcre
    make_pcre
    compile_citron
    make_citron

    copy_into_test_build
    unset CC
    unset CPP
    unset CXX
    unset LD
    unset AR
    unset AS
    unset RANLIB
    unset STRIP
    unset CFLAGS
    unset CXXFLAGS
    unset ABI_NAME
    unset COMPILER_PREFIX
    unset CFLAGS
    unset LDFLAGS

done
