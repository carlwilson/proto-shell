#! /bin/bash
# script to locate and convert BEAM iso/cue file pairs, which are in reality
# bin/cue file pairs

# get the param passed and test that it's a directory
if [ ! -d $1 ]; then
  # not a directory so get out
  { echo "Not a valid directory." >&2; exit 1; }
else
  # use find to locate all files *.iso recursively beneath the root
  for isofile in $( find $1 -iname '*.iso' -type f );
    do
      # replace the iso with a cue ext.
      cuefile=`echo "$isofile" | sed 's/\(.*\.\)iso/\1cue/'`;
      
      # look for a matching .cue file (same/path/samename.cue)
      # if cue is also an existing file
      if [ -f $cuefile ]; then
        # a bit hacky but for testing, renaming is easier, get the correctly named iso into the home dir
        isoname=$(basename "$isofile");
        extension="${isofile#*.}";
        homedir="~";
        eval homedir=$homedir;
        isoname="${homedir}/${isoname%.*}.${extension}";
        echo "Creating isoname " $isoname;
        # bchunk the iso (really a bin?) and the cue file to a new iso under home for now
        bchunk $isofile $cuefile $isoname;
      fi
    done
fi
