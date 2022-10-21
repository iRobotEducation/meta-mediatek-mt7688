# Comment the following three lines and uncomment (and possibly edit) the
# UBOOT_MACHINE definition when building for the linkit7688.
# Uncomment the UBOOT_MACHINE and UBOOT_SYMLINK definitions when
# abandoning the ram version and building for flash only for Showboat & Sundial.
UBOOT_CONFIG="flash ram"
UBOOT_CONFIG[flash]="${MACHINE}_defconfig"
UBOOT_CONFIG[ram]="${MACHINE}_ram_defconfig"
#UBOOT_MACHINE = "linkit-smart-7688_defconfig"
#UBOOT_MACHINE = "${MACHINE}_defconfig"
#UBOOT_SYMLINK = "u-boot.${UBOOT_SUFFIX}-flash"

require u-boot-daredevil.inc
EXTRA_OEMAKE += 'DTC_FLAGS="-p 0x5000"'

SRC_URI_append_linkit7688 = " \
        file://custom.cfg \
	"
# Unfortunately, config file fragments are not compatible with the UBOOT_CONFIG multi build system.
# So we need to maintain the showboat defconfig files manually in this layer, using `bitbake -cmenuconfig`,
# `bitbake -cdevshell` and `make savedefconfig`, or, hopefully, something easier than that.
SRC_URI_append_showboat = " \
	file://showboat_defconfig;subdir=${S}/configs \
	file://showboat_ram_defconfig;subdir=${S}/configs \
	file://showboat.dts;subdir=${S}/arch/mips/dts \
	file://showboat_env.base;subdir=${S} \
	"

SRC_URI_append_sundial = " \
	file://sundial_defconfig;subdir=${S}/configs \
	file://sundial_ram_defconfig;subdir=${S}/configs \
	file://sundial.dts;subdir=${S}/arch/mips/dts \
	file://sundial_env.base;subdir=${S} \
	"

do_deploy_append() {
    if [ -n "${UBOOT_CONFIG}" ]
    then
        for type in ${UBOOT_CONFIG}; do
            ln -sf u-boot-${type}-${PV}-${PR}.${UBOOT_SUFFIX} ${DEPLOYDIR}/u-boot-${IROBOT_UBOOT_VERSION}.bin
	    break
        done
    else
        ln -sf ${UBOOT_IMAGE} ${DEPLOYDIR}/u-boot-${IROBOT_UBOOT_VERSION}.bin
    fi
}
