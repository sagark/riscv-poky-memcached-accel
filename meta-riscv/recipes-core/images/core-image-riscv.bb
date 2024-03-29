SUMMARY = "A small image just capable of allowing a device to boot."

IMAGE_FEATURES += "package-management"

IMAGE_INSTALL = "packagegroup-core-boot ${ROOTFS_PKGMANAGE_BOOTSTRAP} ${CORE_IMAGE_EXTRA_INSTALL}"

# Basic packages
IMAGE_INSTALL += "dropbear apt libffi libffi-dev"

# Python
IMAGE_INSTALL += "python-numpy python-subprocess python-ctypes"

# Basic toolchain on target
IMAGE_INSTALL += "gcc binutils glibc glibc-dev libgcc libgcc-dev libstdc++ libstdc++-dev"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_ROOTFS_SIZE ?= "8192"

