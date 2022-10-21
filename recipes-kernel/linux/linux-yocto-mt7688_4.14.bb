# linux-yocto-mt7688 kernel
#

require recipes-kernel/linux/linux-yocto-mt7688_4.14.inc

# always assign KERNEL_DTB_NAME appropriately in the kernel recipes
# either softer or lazy assignment only in kernel inc file leading to MACHINE
# value to be something else than the intended MACHINE name.
# This requires further inverstigation
KERNEL_DTB_NAME := "${MACHINE}"

# separating dts file name, dts patches, some of the custom configuration
# files from kernel include file as these may change/differ between kernel
# package variants. This is for flexibility only at this time.
SRC_URI += "file://${KBRANCH}/${FIT_IMAGE_DEF_SRC} \
            file://0001-mt7688-dts-makefile.patch \
            file://0002-use-kthread_worker-instead-of-workqueues.patch \
            file://irobot-create3.cfg \
           "
