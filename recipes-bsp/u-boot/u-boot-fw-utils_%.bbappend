FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
        file://fw_env.config;subdir=git/tools/env/ \
        "

# allow fw-utils to compile
UBOOT_CONFIG=""
UBOOT_MACHINE = "linkit-smart-7688_defconfig"

