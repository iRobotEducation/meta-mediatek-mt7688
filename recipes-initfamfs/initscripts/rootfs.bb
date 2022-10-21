SUMMARY = "Validate and mount rootfs "
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

RDEPENDS_${PN} = "initramfs-framework-base secureboot-common"
DEPENDS += "coreutils-native"

# initramfs-framework_1.0.bb advises putting:
#
# BAD_RECOMMENDATIONS += "initramfs-module-rootfs"
#
# in our initramfs recipe, and using some other recipe (i.e. this one) to mount our rootfs.
# But, since space is at a premium, we use the "NO_RECOMMENDATIONS" mechansism in our initramfs.bb recipe
# to tell bitbake not to install anything that is "recommended" by packages installed in our
# initramfs image.

SRC_URI = " \
           file://rootfs \
"

do_install() {
	install -d -m 0755 ${D}/init.d
	install -m 0755 ${WORKDIR}/rootfs ${D}/init.d/50-rootfs
}
FILES_${PN} += "init.d/*"
