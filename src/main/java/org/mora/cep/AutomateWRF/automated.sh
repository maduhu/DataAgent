#!/bin/bash

#Automated runnig of WRF model

#execute like below
#$ ./automated.sh [options] <<-END
#32
#0
#2
#END


#set paths to following directories
WRF_PATH="/home/ruveni/WRF/AutomatedScripts/WRFV3";
WPS_PATH="/home/ruveni/WRF/AutomatedScripts/WPS";
WRF_EMREAL_PATH="/home/ruveni/WRF/AutomatedScripts/WRFV3/test/em_real";

#set path to data
WRF_DATA="/home/ruveni/WRF/AutomatedScripts/WPS/ungrib/Colorado/";

START=$(date +%s);

#WRF Model initial
echo "Navigating to WRF root";
#cd /home/chamil/Playground/WRFV3;
cd $WRF_PATH;
ls;
echo "configuring WRF model...";

#WPS
echo "Moving to WPS directory";
#cd /home/chamil/Playground/WPS;
cd $WPS_PATH;
ls;
echo "configuring WPS...";
./configure;
echo "compiling WPS..";
./compile;

#WPS geogrid
echo "Running geogrid.exe";
./geogrid.exe;

#WPS ungrib
echo "linking Vtable";
ln -sf ungrib/Variable_Tables/Vtable.GFS Vtable;

echo "linking data path";
#./link_grib.csh /home/chamil/Playground/WPS/ungrib/Colorado/;
./link_grib.csh $WRF_DATA;

echo "Running ungrib.exe";
./ungrib.exe;

#WPS metgrid
echo "Running WPS metgrid.exe";
./metgrid.exe;

#WRF final
echo "moving to WRF em_real";
cd $WRF_EMREAL_PATH;
ls;

echo "linking intermediate files";
#ln -sf /home/chamil/Playground/WPS/met_em.d0* .;
ln -sf $WPS_PATH/met_em.d0* .;

echo "running real.exe";
./real.exe;

echo "running WRF.exe";
./wrf.exe;

END=$(date +%s);
DIFF=$(( $END - $START ));

echo "";
echo "WRF model completed successfully";
echo "runtime = $DIFF seconds";
echo "";