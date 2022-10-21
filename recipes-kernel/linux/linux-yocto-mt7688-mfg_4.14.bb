# linux-yocto-mt7688-mfg kernel
#   manufacturing kernel is not a virtual kernel provider for sundial machine
#   but it is a variant kernel

require recipes-kernel/linux/linux-yocto-mt7688_4.14.inc

# KERNEL_PACKAGE_NAME is used to avoid conflicts or multiple providers of
# virtual/kernel in the kernel recipe for same machine and distro of real
# product or regular image
# Refer:
#   sections "24.13.6. Miscellaneous Changes" and
#            "7.3.17. Using Virtual Providers"
#       https://www.yoctoproject.org/docs/latest/mega-manual/mega-manual.html
#   useful discussions:
#       https://lists.yoctoproject.org/pipermail/yocto/2017-March/035329.html
#       https://bugzilla.yoctoproject.org/show_bug.cgi?id=6945
#       https://patchwork.openembedded.org/patch/141561/

KERNEL_PACKAGE_NAME := "kernel-mfg"

# Set kernel type to manufacturing as this kernel is none of the types defined
# by Yocto by default. Yocto defined types: standard, tiny, preempt-rt
# Refer:
#   https://www.yoctoproject.org/docs/latest/mega-manual/mega-manual.html#kernel-types

LINUX_KERNEL_TYPE := "mfg"

LINUX_VERSION_EXTENSION_append := "-mfg"

# always assign KERNEL_DTB_NAME appropriately in the kernel recipes
# either softer or lazy assignment only in kernel inc file leading to MACHINE
# value to be something else than the intended MACHINE name.
# This requires further inverstigation
KERNEL_DTB_NAME := "${MACHINE}"

FIT_IMAGE_DEF_SRC := "fit-image-factory.its"
# if changing fitImage target name for manufacturing, then consider changing
# the names/references appropriately in sundial-usb-boot-image.bb as well to
# include the same in wic image
FIT_IMAGE := "fitImage-${LINUX_KERNEL_TYPE}"
KERNEL_UIMAGE := "uImage-${LINUX_KERNEL_TYPE}"

# separating dts file name, dts patches, some of the custom configuration
# files from kernel include file as these may change/differ between kernel
# package variants. This is for flexibility only at this time.
SRC_URI += "file://${KBRANCH}/${FIT_IMAGE_DEF_SRC} \
            file://0001-mt7688-dts-makefile.patch \
            file://${KBRANCH}/irobot-mfg-config.cfg \
	    file://${KBRANCH}/daredevil/manufacturing-patches.scc \
           "

# overwrite defult COMPATIBLE_MACHINE as manufacturing image is supported only
# on sundial
COMPATIBLE_MACHINE := "(sundial)"

kernel_do_deploy_append() {
   cp ../${KBRANCH}/${FIT_IMAGE_DEF_SRC} .
   cp arch/mips/boot/dts/ralink/${KERNEL_DTB_NAME}.dtb device-tree.dtb
   mkimage -f ${FIT_IMAGE_DEF_SRC} -k ${STAGING_DIR_NATIVE}${datadir}/keys ${FIT_IMAGE}
   install -m 0644 ${B}/${FIT_IMAGE} ${DEPLOYDIR}/${FIT_IMAGE}
}