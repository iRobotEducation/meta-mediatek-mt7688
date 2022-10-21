SUMMARY = "Report a fingerprint of the TSK used when booting."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

RDEPENDS_${PN} = "initramfs-framework-base"
DEPENDS += "coreutils-native"

SRC_URI = " \
           file://fingerprint \
"

do_install() {
	install -d -m 0755 ${D}/init.d
	install -m 0755 ${WORKDIR}/fingerprint ${D}/init.d/20-fingerprint
}

FILES_${PN} += "init.d/*"
