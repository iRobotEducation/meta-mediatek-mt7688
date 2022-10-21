SUMMARY = "Recover kernel/rootfs on a boot failure "
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

RDEPENDS_${PN} = "initramfs-framework-base secureboot-common mtd-utils-ubifs halt-sbwatchdog"
DEPENDS += "coreutils-native"

SRC_URI = " \
           file://fallback \
"

do_install() {
	install -d -m 0755 ${D}/init.d
	install -m 0755 ${WORKDIR}/fallback ${D}/init.d/10-fallback
}

FILES_${PN} += "init.d/*"
