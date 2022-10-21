SUMMARY = "Install initscript that extracts the RTCS key from the BTCS"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

RDEPENDS_${PN} = "initramfs-framework-base secureboot-common sundial-sectools-bin-extract-rtcs-key"
DEPENDS += "coreutils-native"

SRC_URI = " \
           file://getRTCSkey \
"

do_install() {
	install -d -m 0755 ${D}/init.d
	install -m 0755 ${WORKDIR}/getRTCSkey ${D}/init.d/80-getRTCSkey
}
FILES_${PN} += "init.d/*"
