#!/bin/bash
#*******************************************************************************
# Copyright (C) 2013 - now()
# SOCO Engineers GmbH
# All rights reserved.
# http://www.soco-engineers.de/
#*******************************************************************************
#
# /etc/init.d/simuspace
# chkconfig: 45 95 05

RETVAL=0
SelfDIR=/opt/sus
export JAVA_HOME=$SelfDIR/runtime/linux64/zulu17.38.21-ca-jdk17.0.5-linux_x64
export JRE_HOME=$SelfDIR/runtime/linux64/zulu17.38.21-ca-jdk17.0.5-linux_x64

prog="simuspace"
start() {
    export LANG=en_US.UTF-8
    export LC_ALL=en_US.UTF-8
    umask 077
    echo -n $"Starting $prog:"


    if [ -f $SelfDIR/apache-karaf-current/apache-tomcat/bin/catalina.sh ]; then
        result0=$(netstat -tulnp | grep ':80\>')
        if [ -n "$result0" ]; then
            echo "Port 80 not available, so SIMuSPACE WebUI(Tomcat) will not run.";
        else
            $SelfDIR/nginx-1.16.1/sbin/nginx -p $SelfDIR/nginx-1.16.1
            $SelfDIR/apache-karaf-current/apache-tomcat/bin/catalina.sh start -Dsun.awt.headless=true -Djava.awt.headless=true    
       fi
    fi
    
    if [ -f $SelfDIR/apache-karaf-current/bin/start ]; then
        result1=$(netstat -tulnp | grep ':8080\>')
        if [ -n "$result1" ]; then
            echo "Port 8080 not available for apache-karaf";
        else
            $SelfDIR/apache-karaf-current/bin/start clean
        fi
    fi

    ##################  Run Ceetron Server  #########################
    echo

    /proj/fem-sup/AUSTAUSCH/simuspace/nodejs/node $SelfDIR/apache-karaf-current/wfengine/CeeCloudCugServer/Main.js &
    sleep 0.5
    echo

    echo Ceetron Server Started


    ###################################################

    
    RETVAL=$?
    [ "$RETVAL" = 0 ] && touch /var/lock/subsys/$prog
    echo
}

stop() {    
    echo -n $"Stopping $prog:"


    if [ -f $SelfDIR/apache-karaf-current/bin/stop ]; then
        $SelfDIR/apache-karaf-current/bin/stop
    fi
    if [ -f $SelfDIR/apache-karaf-current/apache-tomcat/bin/catalina.sh ]; then
        $SelfDIR/apache-karaf-current/apache-tomcat/bin/catalina.sh stop
        $SelfDIR/nginx-1.16.1/sbin/nginx -p $SelfDIR/nginx-1.16.1 -s stop
    fi

    #################  Kill Ceetron Server  #########################
    echo

    (kill -9 $(ps aux | grep /proj/fem-sup/AUSTAUSCH/simuspace/nodejs/node\ $SelfDIR/apache-karaf-current/wfengine/CeeCloudCugServer/Main.js | awk '{print $2}') 2>&1) >/dev/null
    echo Ceetron Server Stopped

    RETVAL=$?
    [ "$RETVAL" = 0 ] && rm -f /var/lock/subsys/$prog
    echo
}


set_start_time() {
    # Prompt the user for the start date and time in MM-DD HH:MM format
    read -p "Enter start date and time (MM-DD HH:MM): " startDateTime

    # Validate the date and time format
    if [[ ! $startDateTime =~ ^[0-9]{2}-[0-9]{2}\ [0-9]{2}:[0-9]{2}$ ]]; then
        echo "Invalid date and time format. Please enter in MM-DD HH:MM format."
        exit 1
    fi

    # Extract month, day, hour, and minute
    startMonth=${startDateTime:0:2}
    startDay=${startDateTime:3:2}
    startHour=${startDateTime:6:2}
    startMinute=${startDateTime:9:2}

    # Remove any existing start cron jobs
    crontab -l | grep -v "$SelfDIR/bin/simuspace.sh start" | crontab -

    # Add the new start cron job
    (crontab -l ; echo "$startMinute $startHour $startDay $startMonth * $SelfDIR/bin/simuspace.sh start") | crontab -

    echo "Start time set to $startDateTime."
}

# Function to set stop time
set_stop_time() {
    # Prompt the user for the stop date and time in MM-DD HH:MM format
    read -p "Enter stop date and time (MM-DD HH:MM): " stopDateTime

    # Validate the date and time format
    if [[ ! $stopDateTime =~ ^[0-9]{2}-[0-9]{2}\ [0-9]{2}:[0-9]{2}$ ]]; then
        echo "Invalid date and time format. Please enter in MM-DD HH:MM format."
        exit 1
    fi

    # Extract month, day, hour, and minute
    stopMonth=${stopDateTime:0:2}
    stopDay=${stopDateTime:3:2}
    stopHour=${stopDateTime:6:2}
    stopMinute=${stopDateTime:9:2}

    # Remove any existing stop cron jobs
    crontab -l | grep -v "$SelfDIR/bin/simuspace.sh stop" | crontab -

    # Add the new stop cron job
    (crontab -l ; echo "$stopMinute $stopHour $stopDay $stopMonth * $SelfDIR/bin/simuspace.sh stop") | crontab -

    echo "Stop time set to $stopDateTime."
}

clear_cron_jobs() {
    # Remove any existing cron jobs for this script
    crontab -l | grep -v "$SelfDIR/bin/simuspace.sh" | crontab -

    echo "All cron jobs for simuspace.sh have been cleared."
}

restart() {
    stop
    sleep 5  # delay to ensure complete stop. must be adjusted according to server performance
    start
}

case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    set_start_time)
        set_start_time
        ;;
    set_stop_time)
        set_stop_time
        ;;
    clear_cron_jobs)
        clear_cron_jobs
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|set_start_time|set_stop_time|clear_cron_jobs}"
        RETVAL=1
esac
exit $RETVAL
