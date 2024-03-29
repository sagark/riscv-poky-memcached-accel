SUMMARY = "A high-performance memory object caching system"
DESCRIPTION = "\
 memcached optimizes specific high-load serving applications that are designed \
 to take advantage of its versatile no-locking memory access system. Clients \
 are available in several different programming languages, to suit the needs \
 of the specific application. Traditionally this has been used in mod_perl \
 apps to avoid storing large chunks of data in Apache memory, and to share \
 this burden across several machines."

HOMEPAGE = "http://memcached.org/"
LICENSE = "BSD-3-Clause"

LIC_FILES_CHKSUM = "file://COPYING;md5=7e5ded7363d335e1bb18013ca08046ff"

inherit autotools

DEPENDS += "libevent"
RDEPENDS_${PN} += "perl perl-module-posix perl-module-autoloader \
    perl-module-tie-hash bash \
    "

SRC_URI = "git://github.com/sagark/memcached-accel.git;branch=master;rev=facb719f40ecfcb98271a79561d4b94757b7a843  \
           file://configure.patch \
           file://memcached-add-hugetlbfs-check.patch"

S = "${WORKDIR}/git"

# set the same COMPATIBLE_HOST as libhugetlbfs
COMPATIBLE_HOST = '(i.86|x86_64|powerpc|powerpc64|arm|riscv-poky).*-linux'

SRC_URI[md5sum] = "46402dfbd7faadf6182283dbbd18b1a6"
SRC_URI[sha256sum] = "d9173ef6d99ba798c982ea4566cb4f0e64eb23859fdbf9926a89999d8cdc0458"

SRCREV_default_pn-memcached-accel ?= "facb719f40ecfcb98271a79561d4b94757b7a843"

python __anonymous () {
    endianness = d.getVar('SITEINFO_ENDIANNESS', True)
    if endianness == 'le':
        d.appendVar('EXTRA_OECONF', " ac_cv_c_endian=little")
    else:
        d.appendVar('EXTRA_OECONF', " ac_cv_c_endian=big")
}

PACKAGECONFIG ??= ""
PACKAGECONFIG[hugetlbfs] = "--enable-hugetlbfs, --disable-hugetlbfs, libhugetlbfs"

inherit update-rc.d

INITSCRIPT_NAME = "memcached"
INITSCRIPT_PARAMS = "defaults"

do_configure_prepend() {
    ( cd "${S}" && ./autogen.sh )
}

do_install_append() {
    install -D -m 755 ${S}/scripts/memcached-init ${D}${sysconfdir}/init.d/memcached
    mkdir -p ${D}/usr/share/memcached/scripts
    install -m 755 ${S}/scripts/memcached-tool ${D}/usr/share/memcached/scripts
    install -m 755 ${S}/scripts/start-memcached ${D}/usr/share/memcached/scripts
}
