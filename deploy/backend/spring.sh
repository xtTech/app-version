#!/bin/bash
#processname: spring-boot  project

app=avm
cd $PWD

PID=`ps aux | grep ${app} | grep -v grep | grep java | awk '{print $2}'`

function start(){
    if [ ${PID} ];then
        echo "${app} 正在运行，请不要重复启动"
    else
        echo "开始启动 app-version 项目: ${app}模块"
        JAR=`ls -d ${app}.jar | head -1`
        nohup java -jar -Xms1024m -Xmx1024m -Dspring.profiles.active=dev ${JAR} > /dev/null 2>&1 &
        echo ${app}"项目已启动"
   fi
}

function stop(){
    if [ ${PID} ];then
        echo "开始停止 app-version 项目: ${app}"
        kill ${PID}
        sleep 5
        PID2=`ps aux | grep ${app} | grep -v grep | grep java | awk '{print $2}'`
        if [ ${PID2} ];then
            echo "${app}没有停止成功，现在强制停止 ${app}"
            kill -9 ${PID}
        fi
        echo "${app}停止成功"
    else
        echo "${app} 没有启动"
    fi
}

function status(){
    if [ ${PID} ];then
        echo "Tomcat is running ..."
    else
        echo "Tomcat is not exist!!!"
    fi
}

function restart(){
        stop
        start
}

case "$1" in
    avm-start)
        start
        ;;
    avm-stop)
        stop
        ;;
    avm-restart)
        restart
        ;;
    avm-status)
        status
        ;;
   avr-start)
        app=avr
        PID=`ps aux | grep ${app} | grep -v grep | grep java | awk '{print $2}'`
        start
        ;;
   avr-stop)
        app=avr
        PID=`ps aux | grep ${app} | grep -v grep | grep java | awk '{print $2}'`
        stop
        ;;
   avr-restart)
        app=avr
        PID=`ps aux | grep ${app} | grep -v grep | grep java | awk '{print $2}'`
        restart
        ;;
   avr-status)
        app=avr
        PID=`ps aux | grep ${app} | grep -v grep | grep java | awk '{print $2}'`
        status
        ;;
    *)
        echo $"Usage: $0 {avm-start|avm-stop|avm-restart|avm-status|avr-start|avr-stop|avr-restart|avr-status}"
        exit 1
esac

exit 0
