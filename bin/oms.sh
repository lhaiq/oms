#!/bin/sh

WORK_DIR=$(cd `dirname $0`; pwd)/../
LOG_DIR=${WORK_DIR}/logs

if [ ! -d "$LOG_DIR" ] ; then
   mkdir "$LOG_DIR"
fi

PID_FILE=${LOG_DIR}/oms.pid

JAVA=/usr/bin/java
JAVA_OPTS="-server -Xms256m -Xmx256m
 -XX:+HeapDumpOnOutOfMemoryError
 -XX:HeapDumpPath=/services/logs/oom.hprof
 -XX:+UseParNewGC
 -XX:+UseConcMarkSweepGC
 -XX:CMSInitiatingOccupancyFraction=75
 -XX:+UseCMSInitiatingOccupancyOnly
 -XX:+ExplicitGCInvokesConcurrent
 -XX:-UseBiasedLocking
 -XX:+AlwaysPreTouch
 -XX:+CMSParallelRemarkEnabled
 -Dwork.dir=${WORK_DIR}
 -Dlogback.configurationFile=file:conf/logback.xml
 -Dfile.encoding=UTF-8
 -Duser.timezone=UTC
 -Dcom.sun.management.jmxremote.port=8109
 -Dcom.sun.management.jmxremote.ssl=false
 -Dcom.sun.management.jmxremote.authenticate=false"
CLASS_PATH=" -classpath ":$(echo ${WORK_DIR}/lib/*.jar|sed 's/ /:/g')
CLASS=com.maxent.oms.Boot

cd $WORK_DIR

case "$1" in

  start)
  	if [ -f "${PID_FILE}" ]; then
    	echo "oms is running,pid=`cat ${PID_FILE}`."
    else
    	exec "$JAVA" $JAVA_OPTS $CLASS_PATH $CLASS >> ${LOG_DIR}/oms.out 2>&1 &
		echo "oms is running,pid=$!."
    	echo $! > ${PID_FILE}
    fi
    ;;

  stop)
  	if [ -f "${PID_FILE}" ]; then
    	kill -9 `cat ${PID_FILE}`
    	rm -rf ${PID_FILE}
    	echo "oms  is stopped."
    else
    	echo "oms  is not running."
    fi
    ;;

  restart)
    bin/$0 stop
    sleep 1
    bin/$0 start
    ;;

  status)
  	if [ -f "${PID_FILE}" ]; then
    	echo "oms  is running,pid=`cat ${PID_FILE}`."
    else
    	echo "oms  is not running."
    fi
    ;;

  *)
    echo "Usage: oms.sh {start|stop|restart|status}"
    ;;

esac

exit 0