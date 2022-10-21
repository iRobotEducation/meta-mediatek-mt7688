SUMMARY = "Install initscript that extracts locks the NOR flash"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

RDEPENDS_${PN} = "initramfs-framework-base libgpio"
DEPENDS += "coreutils-native"

SRC_URI = " \
           file://lockNOR \
"

do_install() {
	install -d -m 0755 ${D}/init.d
	install -m 0755 ${WORKDIR}/lockNOR ${D}/init.d/90-lockNOR
}
FILES_${PN} += "init.d/*"
