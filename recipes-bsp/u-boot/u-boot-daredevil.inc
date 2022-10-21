require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native xxd-native"

SRC_URI_append = " \
        file://0001-lks7688-mods.patch \
        file://0001-phy-Add-USB-PHY-driver-for-the-MT76x8-7628-7688-SoC-2.patch \
        file://0002-lks7688-enable-usb-fs-support.patch \
	file://0001-Add-support-for-ISSI-IS25LP080D-NOR-Flash.patch \
	file://0002-Add-support-for-Toshiba-TC58CVG0S3-SPI-NAND-device.patch \
	file://0003-Report-skipped-blocks-when-reading-writing-mtd-parti.patch \
	file://0004-Add-support-for-Macronix-MX25V8035F-NOR-Flash.patch \
	file://0005-Tweak-memory-settings-for-ETRON-and-Nanya-512Mbit-me.patch \
	file://0006-rsa-reject-images-with-unknown-padding.patch \
	file://0007-Add-support-for-booting-images-up-to-32MB-uncompress.patch \
	file://0008-Drive-WDT_RST_N-signal-until-processor-is-reset.patch \
	file://0009-rsa-verify-Use-debug-instead-of-printf.patch \
	file://0010-rsa_import_keys-Add-rsa_import_keys-command.patch \
	file://0012-verified-boot-Scan-the-public-keys-in-u-boot-fdt-ins.patch \
	file://0013-Turn-on-LED-D3-at-boot.patch \
	file://0014-Always-include-root-node-when-verifying-image-signat.patch \
	file://0018-Pulse-GPIO22-high-at-the-start-and-end-of-DDR-calibr.patch \
	file://0019-Force-DDR_DQS_DLY-to-a-nominal-value-instead-of-usin.patch \
	file://0020-Display-u-boot-version-and-DQS-calibration-status-at.patch \
	file://0021-Use-new-version-lowlevel_init.S-supplied-by-MediaTek.patch \
	file://0022-Increase-the-drive-strength-to-0x0c0c.patch \
	file://0023-Fix-corner-case-in-bad-block-table-handling.patch \
	file://0024-Add-scrub-parameter-to-nandndev_erase.patch \
	file://0025-Add-support-for-TC58CVG0S3HRAIJ-Toshiba-NAND-flash-P.patch \
        "

# These two patches:
# file://0015-Always-enable-cache-at-startup.patch
# file://0016-Add-in-support-for-locking-unlocking-the-flash-devic.patch
# are only used by the RAM based utilities such as rekey
#
# file://0017-Don-t-change-DDR-drive-strength.patch was used to generate a
# u-boot-3.5.2 version in an attempt to solve a factory-line-stoppage problem.
# Since it didn't work, we have removed it from the build.

EXTRA_OEMAKE += "KCPPFLAGS=-DIROBOT_UBOOT_VERSION=${IROBOT_UBOOT_VERSION}"

# Build u-boot with something substantially similar to:
# BB_ENV_EXTRAWHITE="$BB_ENV_EXTRAWHITE IROBOT_UBOOT_VERSION" IROBOT_UBOOT_VERSION=0.1 bitbake u-boot
# to create a versioned u-boot
IROBOT_UBOOT_VERSION ??="unknown"
IROBOT_UBOOT_SEI_VERSION ??="${IROBOT_UBOOT_VERSION}"
do_compile_prepend() {
	[ -e ${S}/${MACHINE}_env.base ] && sed 's/IROBOT_UBOOT_VERSION/${IROBOT_UBOOT_VERSION}/g' ${S}/${MACHINE}_env.base > ${S}/${MACHINE}_env
}

SRCREV = "v2019.04-rc4"
