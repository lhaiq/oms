#!/bin/sh

WORK_DIR=$(cd `dirname $0`; pwd)/../
LOG_DIR=${WORK_DIR}/logs

if [ ! -d "$LOG_DIR" ] ; then
   mkdir "$LOG_DIR"
fi


LOG_FILE=${LOG_DIR}/id-system.log


PID_FILE=${LOG_DIR}/id-system.pid

JAVA=/usr/bin/java
JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:-CMSConcurrentMTEnabled
-XX:CMSInitiatingOccupancyFraction=70 -XX:+CMSParallelRemarkEnabled -Dwork.dir=${WORK_DIR}
  -Dlogging.config=file:conf/log4j.properties    "
CLASS_PATH=" -classpath ":$(echo ${WORK_DIR}/lib/*.jar|sed 's/ /:/g')
CLASS_PATH=${CLASS_PATH}:${WORK_DIR}/conf/
CLASS=com.maxent.id.system.Boot

cd $WORK_DIR

case "$1" in

  start)
  	if [ -f "${PID_FILE}" ]; then
    	echo "id-system is running,pid=`cat ${PID_FILE}`."
    else
    	exec "$JAVA" $JAVA_OPTS $CLASS_PATH $CLASS>> ${LOG_DIR}/id-system.log 2>&1 &
		echo "id-system is running,pid=$!."
    	echo $! > ${PID_FILE}
    fi
    ;;

  stop)
  	if [ -f "${PID_FILE}" ]; then
    	kill -9 `cat ${PID_FILE}`
    	rm -rf ${PID_FILE}
    	echo "id-system  is stopped."
    else
    	echo "id-system  is not running."
    fi
    ;;

  restart)
    bin/$0 stop
    sleep 1
    bin/$0 start
    ;;

  status)
  	if [ -f "${PID_FILE}" ]; then
    	echo "id-system  is running,pid=`cat ${PID_FILE}`."
    else
    	echo "id-system  is not running."
    fi
    ;;

  *)
    echo "Usage: id-system.sh {start|stop|restart|status}"
    ;;

esac

exit 0