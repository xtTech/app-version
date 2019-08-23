#!/bin/bash
#processname: spring-boot  project

#app=`echo $PWD | awk -F'/' '{print $NF}'`
# 对脚本增加执行权限 chmod +x spring.sh
# 使用方法 将jar的名字写到APP变量  .jar不用写
# 启动 ./spring.sh start  停止 ./spring.sh stop 重启 ./spring.sh restart
# 这里写上你需要启动应用的 jar 名称
app=app-version-manager
cd $PWD


function start(){
    PID=`ps aux | grep ${app} | grep -v grep | grep java | awk '{print $2}'`
    if [ ${PID} ];then
        echo "${app} 正在运行，请不要复制启动"
    else
        echo "开始启动 spring-boot 项目: ${app}"
        JAR=`ls -d ${app}.jar | head -1`
        nohup java -jar -Xms1024m -Xmx1024m -Xss512k -Dspring.profiles.active=prd ${JAR} > service.log 2>&1 &
    fi
}

function stop(){
    PID=`ps aux | grep ${app} | grep -v grep | grep java | awk '{print $2}'`
    if [ ${PID} ];then
        echo "开始停止spring-boot 项目: ${app}!!!"
        kill ${PID}
        sleep 5
        PID2=`ps aux | grep ${app} | grep -v grep | grep java | awk '{print $2}'`
        if [ ${PID2} ];then
            echo "${app}没有停止成功，现在强制停止 ${app}"
            kill -9 ${PID}
        fi
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
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    status)
        status
        ;;
    *)
        echo $"Usage: $0 {start|stop|restart|status}"
        exit 1
esac

exit 0
