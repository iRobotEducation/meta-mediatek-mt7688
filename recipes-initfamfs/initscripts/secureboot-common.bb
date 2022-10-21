SUMMARY = "Install common functions that can be used by other secureboot initscripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

RDEPENDS_${PN} = "initramfs-framework-base"
DEPENDS += "coreutils-native"

SRC_URI = " \
           file://common \
"

do_install() {
	install -d -m 0755 ${D}/init.d
	install -m 0755 ${WORKDIR}/common ${D}/init.d/00-common
}
FILES_${PN} += "init.d/*"
