FITIMAGE_KERNEL ?= "${B}/vmlinux.bin.lzma"
FITIMAGE_DTB    ?= "arch/mips/boot/dts/ralink/${KERNEL_DTB_NAME}.dtb"
FITIMAGE_ITS    ?= "../${KBRANCH}/${FIT_IMAGE_DEF_SRC} ${DEPLOYDIR}/fit-image-${KERNEL_VERSION}.its"
ROOTFS_IMAGE    ?= "${DEPLOY_DIR_IMAGE}/base-image-${MACHINE}.squashfs-xz"

DEPENDS += "u-boot-mkimage-native dtc-native opensource-keys-native"

# The following assets are produced by the kernel build (or maintained in the kernel
# build recipe) and are required by build_fitimage().  We deploy them to ${DEPLOYDIR}
deploy_fitimage_assets() {
    install -m 0644 ${B}/vmlinux.bin.lzma ${DEPLOYDIR}/vmlinux-${KERNEL_VERSION}.bin.lzma
    ln -sf vmlinux-${KERNEL_VERSION}.bin.lzma ${DEPLOYDIR}/vmlinux.bin.lzma
    install -m 0644 arch/mips/boot/dts/ralink/${KERNEL_DTB_NAME}.dtb ${DEPLOYDIR}/device-tree-${KERNEL_VERSION}.dtb
    ln -sf device-tree-${KERNEL_VERSION}.dtb ${DEPLOYDIR}/device-tree.dtb
    install -m 0644 ../${KBRANCH}/${FIT_IMAGE_DEF_SRC} ${DEPLOYDIR}/fit-image-${KERNEL_VERSION}.its
    ln -sf fit-image-${KERNEL_VERSION}.its ${DEPLOYDIR}/fit-image.its
}

build_fitimage() {
    # Strap on your seatbelt
    # - convert the devicetree binary (back) to source
    # - compute the sha256sum of the rootfs, keeping just the 64 characters of the sum
    # - replace the string 'rootfshash = "UNKNOWN"' in the device tree source with 'rootfshash = "<hash value>"'
    # - and then convert the device tree source back to binary.
    dtc -I dtb -O dts -o device-tree.dts ${DEPLOY_DIR_IMAGE}/device-tree.dtb
    sha256sum -b ${ROOTFS_IMAGE} | cut -c-64 | \
      xargs --replace=INSERTED -- sed 's/rootfshash = "UNKNOWN"/rootfshash = "INSERTED"/' device-tree.dts | \
      dtc -I dts -O dtb -o device-tree.dtb

    cp ${DEPLOY_DIR_IMAGE}/fit-image.its .
    cp ${DEPLOY_DIR_IMAGE}/vmlinux.bin.lzma .
    cp ${DEPLOY_DIR_IMAGE}/initramfs-sundial.cpio.lzma .
    mkimage -f fit-image.its -k ${STAGING_DIR_NATIVE}${datadir}/keys fitImage
}
