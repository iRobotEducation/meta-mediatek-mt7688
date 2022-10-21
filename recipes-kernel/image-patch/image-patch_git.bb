DESCRIPTION = "Openwrt tool for pathcing an image with DTB file."
SECTION = "Openwrt tools."
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "git://github.com/openwrt/openwrt.git;protocol=git;rev=7c23f741e97f6645bb5cd662a4943796a344b26a"

S = "${WORKDIR}/git/tools/patch-image/src"

BBCLASSEXTEND="native nativesdk"

FILES_${PN}_class-native="${D}/${bindir}/*"

do_compile_class-native () {
    ${CC} patch-dtb.c ${LDFLAGS} -o patch-dtb
}

do_install(){
    install -d ${D}/${bindir}/
    install -m 0755 ${S}/patch-dtb ${D}/${bindir}/ 
}
