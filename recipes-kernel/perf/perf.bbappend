FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# The size of generated .swu pacakge with pofile build was more than 25MB. Due to storage size limitations .swu image failed to boot the robot but installed in
# some cases. The scripting installs the python package which adds around 3MB of .swu size. The basic functionality  of perf does not required python and tui. 
# So, to reduce the size of .swu image updated PACKAGECONFIG to remove scripting and tui packages from perf.
PACKAGECONFIG_remove = "scripting tui"
