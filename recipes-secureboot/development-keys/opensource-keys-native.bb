SUMMARY = "Development key used by open source Create 3 image"
DESCRIPTION = "Install development key in the sysroot so that it \
can be referenced by other recipes."


LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

inherit native
# We don't need to depend on the C compiler, etc...
INHIBIT_DEFAULT_DEPS="1"

SRC_URI = "file://keys/C3OpenSource-TrustSigningKey.pri.key \
	   file://keys/C3OpenSource-TrustSigningKey.pub.key  \
	   file://keys/C3OpenSource-CodeSigningKey.pri.key \
	   file://keys/C3OpenSource-CodeSigningKey.pub.key \
	  "

do_install() {
    install -d ${D}${datadir}/keys
    install -m 644 ${WORKDIR}/keys/* ${D}${datadir}/keys
}

FILES_${PN} += "${datadir}/keys/"

