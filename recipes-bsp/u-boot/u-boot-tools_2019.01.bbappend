FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
	file://0001-mkimage-Add-option-to-allow-signing-of-images-withou.patch \
	file://0002-mkimage-Fail-if-key-directory-cannot-be-found.patch \
	"

do_install_append () {
	# fit_check_sign
	install -m 0755 tools/fit_check_sign ${D}${bindir}/uboot-fit_check_sign
	ln -sf uboot-fit_check_sign ${D}${bindir}/fit_check_sign
}

PACKAGES += "${PN}-fit-check-sign"
FILES_${PN}-fit-check-sign = "${bindir}/uboot-fit_check_sign ${bindir}/fit_check_sign"
