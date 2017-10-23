#!/usr/bin/env bash
ENTRANCE="cn.edu.hust.memcached.server.Application"
# 获取模块工作目录的绝对路径
CWD="`pwd`/$( dirname "${BASH_SOURCE[0]}" )/.."

read -p "please enter port:" PORT
echo "start the Memcached server on port $PORT ......"

cd "${CWD}" && java -cp ./lib/*: $ENTRANCE $PORT

# sleep 一下，检查当前jar是否在运行
sleep 2
ps aux | grep $ENTRANCE | grep -v grep