# This recipe provides the ability to build the mediatek version of u-boot from a 
# tar ball.  This is purposely kept separate from the u-boot_git which has been 
# throughly tested for seiflashing.  When time allows, it may be cleaner to have
# this recipe be a "version" of u-boot by calling it u-boot_mediatek, and then having
# a PREFERRED_VERSION_u-boot="mediatek", to select when we want to use this u-boot recipe.

require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native xxd-native"

UBOOT_MACHINE="sundial_defconfig"

IROBOT_UBOOT_VERSION ??="unknown"
IROBOT_UBOOT_SEI_VERSION ??="${IROBOT_UBOOT_VERSION}"

EXTRA_OEMAKE += 'DTC_FLAGS="-p 0x5000"'
EXTRA_OEMAKE += "KCPPFLAGS=-DIROBOT_UBOOT_VERSION=${IROBOT_UBOOT_VERSION}"

do_compile_prepend() {
    [ -e ${S}/${MACHINE}_env.base ] && sed 's/IROBOT_UBOOT_VERSION/${IROBOT_UBOOT_VERSION}/g' ${S}/${MACHINE}_env.base > ${S}/${MACHINE}_env
}

# Add dts files for sundial
SRC_URI_append_sundial = " \
    file://sundial.dts;subdir=${S}/arch/mips/dts \
    file://sundial-u-boot.dtsi;subdir=${S}/arch/mips/dts \
    file://sundial_env.base;subdir=${S} \
    file://sundial_defconfig;subdir=${S}/configs \
    file://u-boot-4-source_date_epoch \
"

# include patches against mediatek
SRC_URI_append = " \
    file://0001-Add-support-for-ISSI-IS25LP080D-NOR-Flash.patch \
    file://0002-Add-support-for-Toshiba-TC58CVG0S3-SPI-NAND-device.patch \
    file://0003-Report-skipped-blocks-when-reading-writing-mtd-parti.patch \
    file://0004-Add-support-for-Macronix-MX25V8035F-NOR-Flash.patch \
    file://0005-rsa-verify-Use-debug-instead-of-printf.patch \
    file://0006-rsa_import_keys-Add-rsa_import_keys-command.patch \
    file://0007-verified-boot-Scan-the-public-keys-in-u-boot-fdt-ins.patch \
    file://0008-Always-include-root-node-when-verifying-image-signat.patch \
    file://0009-Tweak-memory-settings-for-ETRON-and-Nanya-512Mbit-me.patch \
    file://0010-Tweak-irobot-mt7688.h-to-meet-our-needs.patch \
    file://0011-Drive-WDT_RST_N-signal-until-processor-is-reset.patch \
    file://0012-Display-u-boot-version-and-DQS-calibration-status-at.patch \
    file://0013-Pulse-GPIO22-high-at-the-start-and-end-of-DDR-calibr.patch \
    file://0014-Increase-DDR-drive-strength-from-4-to-12.patch \
    file://0015-Build-sundial.dtb-instead-of-irobot.dtb.patch \
    file://0016-Don-t-include-MediaTek-upgrade-code-unless-the-menuc.patch \
    file://0017-Fix-corner-case-in-bad-block-table-handling.patch \
    file://0018-Add-support-for-TC58CVG0S3HRAIJ-Toshiba-NAND-flash-P.patch \
    file://0019-u-boot-ignore-reserved-bits-in-Macronix-ECC-status-r.patch \
"

SRC_URI = "https://artifactory.wardrobe.irobot.com/artifactory/Projects-dependencies/daredevil/sources/u-boot/u-boot-iRobot.tar.bz2;name=source"
SRC_URI[source.sha256sum] = "61911418fdf72e15b81fd793fd4937c673571d920f661d45850b00fb9b1f2699"

# move the source folder since we are not longer using a git repo as the source
S = "${WORKDIR}/u-boot-iRobot"

# For reproducible builds, we want to generate a u-boot with a fixed embedded build date.
# Normally, this would be determined by the date/time on which the image was generated.
# For reproductible builds, it is determined by the build system.  Yocto would normally
# base this date on the timestamp of the last commit in the git repository, but since we
# moved from using a git repository to using a tarball supplied by MediaTek, we no longer
# have have a git repo.  In that case. Yocto would use the most recent file in the source
# tree, but since we populate files in the the source tree using SRC_URI (see the
# ";subdir=${S}/..." parameters in the SRC_URI's above), that most recent file will have
# a timestamp corresponding to the date/time in which bitbake was run, thus defeating the
# whole reproducible build notion.  The way around this is to specify a "Source Date Epoch"
# file (SDE_FILE) containing a timestamp to be used for timestamps in the u-boot build, which
# we do below.  We choose as our timestamp, the date/time of the first successful u-boot-4.x
# build with Jenkins: 1593125736.  That way we can play with subsequent Jenkins builds and
# still produce identical 4.0.1 binaries. Future revisions (if any) would also share this
# timestamp, but will differ by whatever differences that future revision provides.
SDE_FILE = "${WORKDIR}/u-boot-4-source_date_epoch"

# NOTE: Because this recipe installs a file to the deploy directory's u-boot.bin-flash, we can not 
# run this recipe AND u-boot back to back in either order.  You must do a cleanall of the first u-boot
# before you can run the next u-boot command.  This is not ideal, but this approach introduced the 
# least amount of change to implement this version of u-boot into the infrastrure for reflashing/rekeying
# that already exists for the Daredevil project.
do_deploy_append() {
    # copy the mtmips version to deploy directory for sei flash
    ln -sf u-boot.bin-flash ${DEPLOYDIR}/u-boot-${IROBOT_UBOOT_VERSION}.bin
    install -m 644 ${B}/u-boot-mtmips.bin ${DEPLOYDIR}/u-boot.bin-flash
}
