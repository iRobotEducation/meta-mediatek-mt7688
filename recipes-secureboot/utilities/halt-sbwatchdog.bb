SUMMARY = "Halt the SecureBoot watchdog timer"
DESCRIPTION = "Install the halt_sbwatchdog utility (currently, a one line \
shell script), which may be invoked by an init script or other utility to \
halt the SecureBoot watchdog.  u-boot enables the watchdog reset immediately \
prior to launching the Linux kernel.  This watchdog must be halted within 60 \
seconds, or else the processor will reset and u-boot will launch the \
fallback kernel (which will/should then initiate the process of overwriting \
presumed bad "A" kernel and rootfs with the presumed good "B" (fallback) kernel \
and rootfs.  If you don't want that to happen, then you should execute \
halt_sbwatchdog (which is what we install).  Realistically, the only entity \
that could care about halting this, is the base-image init.d script processing, \
and it shouldn't halt the sbwatchdog until it is reasonably certain that the new \
kernel is functioning well enough to accept a software update.  Ideally, the \
entity most in position to make that determination is buried somewhere in \
connectivity."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = " \
           file://halt_sbwatchdog \
"

S = "${WORKDIR}"

do_install() {
    install -d ${D}${bindir}
    install -m 755 ${S}/halt_sbwatchdog -D ${D}${bindir}
}

FILES_${PN} += "${bindir}/*"
