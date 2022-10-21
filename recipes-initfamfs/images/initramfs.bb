# Simple initramfs image used as part of Sundial SecureBoot framework
#
# Copied and adapted from poky/meta/recipes-core/images/core-image-minimal-initramfs.bb

DESCRIPTION = "Small image that supplies only the components necessary to \
support the Sundial SecureBoot framework."

# Since space is at a premium, we use NO_RECOMMENDATIONS to tell bitbake not to install anything that
# is "recommended" by packages we install in our image.  This seems to work just fine for us (although,
# realistically, it doesn't make that much of a difference).
NO_RECOMMENDATIONS_pn-initramfs = "1"

# We use our custom fallback.bb recipe to install 10-fallback, which checks
# to see if we have booted from the "B" (fallback) image and to implement
# whatever corrective measures are necessary (such as copying the "B" image
# over the broken "A" image).
# We use our custom rootfs.bb recipe to install 90-rootfs, which validates
# the checksum and then mounts our rootfs (or reboots to the fallback image
# if the checksum doesn't match).
PACKAGE_INSTALL = "${VIRTUAL-RUNTIME_base-utils} fallback fingerprint-tsk rootfs get-rtcs-key lock-nor"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""

export IMAGE_BASENAME = "${MLPREFIX}initramfs"
IMAGE_LINGUAS = ""

LICENSE = "MIT"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
inherit core-image

IMAGE_ROOTFS_SIZE = "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "0"

# Use the same restriction as initramfs-live-install
COMPATIBLE_HOST = "mipsel-poky-linux"
