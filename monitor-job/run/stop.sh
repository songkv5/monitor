crtDir=$(pwd)
mainClass=com.sys.monitor.Application
pids=$(ps -C java --no-heading -F --width 1500|grep $mainClass|awk '{print $2}')
for pid in $pids
  do
        echo "kill process $pid"
        kill $pid /dev/null 2>&1
  done

count=0
while [ $count -lt 1 ]
  do
    echo -e ".\t"
    sleep 1
    count=1
    for pid in $pids
      do
        pid_runnig=$(ps --no-heading -p $pid)
        if [ -n "$pid_runnig" ]; then
          count=0
        fi
      done
  done
echo $pids" stopped!"