#!/bin/bash
# 应用名称
app=fly-security-oauth
# JVM的启动参数
app_opts="-Dfile.encoding=UTF-8 -Xms64m -Xmx64m -Xss256k"
# 备份的数量 最少是1
app_backup_num=3
# 当前时间
current_time="$(date +%Y-%m-%d.%H-%M-%S)"

# 参数 需要运行的命令
command="$1"

# 判断服务状态
function status() {
  app_pid=$(pgrep -f ${app}.jar)
  if [[ -z "${app_pid}" ]]; then
    echo "App ${app} is not running"
    return
  fi
  echo "App is running and pid = ${app_pid}"
}

# 关闭服务
function stop() {
  echo "App ${app} is ready to close"
  status
  if [[ -z "${app_pid}" ]]; then
    return
  fi
  kill "${app_pid}" >/dev/null
  echo "app ${app} has been successfully closed"
}

# 启动服务
function start() {
  if [[ -n "${app_pid}" ]]; then
    echo "Waiting for 10 seconds to let the last service close, Please don't press ctrl + c"
    sleep 10
  fi
  source /etc/profile
  nohup java ${app_opts} -jar ${app}.jar >${app}.log &
  echo "App ${app} has been successfully started"
}

# 创建deploy文件夹
function makeDirIfNotExist() {
  if [[ ! -d "deploy" ]]; then
    mkdir deploy
    echo "The deploy folder have been successfully created"
  fi
  if [[ ! -d "deploy/backup" ]]; then
    mkdir deploy/backup
    echo "The deploy/backup folder have been successfully created"
  fi
  echo "Please put your ${app}.jar into deploy folder now!"
}

# 将deploy目录的app.jar包覆盖到根目录
function deploy() {
  if [[ -f deploy/${app}.jar ]]; then
    echo "The deploy/${app}.jar will be overwritten to the root path"
    cp -f deploy/${app}.jar ${app}.jar
  else
    echo "App ${app} deploy failed"
    echo "There is no file(${app}.jar) in the deploy folder!"
    echo "Please put the ${app}.jar into the deploy folder!"
    exit 8
  fi
}

# 删除备份文件
function deleteBackupFiles() {
  # 获取前缀为app-backup的文件数量
  current_backup_num=$(ls -lR | grep "${app}-backup" | wc -l)
  echo "The folder has ${current_backup_num} backup files now, your settings is ${app_backup_num}"
  head_backup_num=$(expr ${app_backup_num} - ${current_backup_num})
  backup_array=$(ls -tr $(find deploy/backup -type f -name "${app}-backup*") | head ${head_backup_num})
  for i in "${backup_array[@]}"; do
    rm -rf $i
    echo "We have deleted the following backup files"
    echo "------------------------------------"
    echo "$i"
    echo "------------------------------------"
  done
}

# 备份当前正在运行的服务
function backup() {
  # 如果大于0 就可以备份
  if [[ "${app_backup_num}" -lt 0 ]]; then
    echo "Your current setting(${app_backup_num}) is less than 0, We will not back up your app"
  else
    echo "App ${app} ready to back up the old version"
    # 打包文件
    zip -q -r deploy/backup/${app}-backup-${current_time}.zip ${app}.jar resources lib
    echo "App ${app} have been successfully created"
    deleteBackupFiles
  fi
}

if [[ "$command" == "backup" ]]; then
  makeDirIfNotExist
  backup
elif [[ "$command" == "status" ]]; then
  status
elif [[ "$command" == "stop" ]]; then
  stop
elif [[ "$command" == "start" ]]; then
  stop
  start
elif [[ "$command" == "deploy" ]]; then
  echo "App ${app} ready to deploy"
  if [[ ! -d "deploy" ]]; then
    echo "Folder deploy does not exist!"
    echo "We will create folders that must be used"
    # 创建文件夹
    makeDirIfNotExist
    exit 8
  fi
  backup
  # 开始部署
  deploy
  stop
  start
else
  # 提示命令
  echo "only support these command -> backup / status / stop / start / deploy"
fi
