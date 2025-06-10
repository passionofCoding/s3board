#!/bin/bash
set -e

APP_NAME=s3board
BASE_DIR=/home/ubuntu/s3board
CURRENT_DIR=$BASE_DIR/current
PREVIOUS_DIR=$BASE_DIR/previous
TOBE_DIR=$BASE_DIR/targetdir
PORT1=8080
PORT2=8081

# 1. 현재 실행 중인 포트를 확인
CURRENT_PORT=$(lsof -i -P -n | grep java | grep -E ":$PORT1|:$PORT2" | awk '{print $9}' | sed 's/.*://')
if [ "$CURRENT_PORT" == "$PORT1" ]; then
    NEW_PORT=$PORT2
    OLD_PORT=$PORT1
else
    NEW_PORT=$PORT1
    OLD_PORT=$PORT2
fi

echo "현재 실행 중인 포트: $CURRENT_PORT → 새로운 포트: $NEW_PORT"

# 2. 이전 버전 백업
rm -rf $PREVIOUS_DIR
cp -r $CURRENT_DIR $PREVIOUS_DIR

# 3. 새로운 버전으로 current 갱신
rm -rf $CURRENT_DIR
mkdir -p $CURRENT_DIR
cp $TOBE_DIR/$APP_NAME.jar $CURRENT_DIR/

# 4. 기존 실행 종료
sudo fuser -k -n tcp $NEW_PORT || true

# 5. 새 버전 실행 (새 포트로)
cd $CURRENT_DIR
nohup java -jar $APP_NAME.jar --server.port=$NEW_PORT > ./output.log 2>&1 &

# 6. 헬스체크
echo "헬스체크 시작..."
for i in {1..10}
do
  sleep 3
  STATUS=$(curl -s http://localhost:$NEW_PORT/actuator/health | grep UP || true)
  if [ ! -z "$STATUS" ]; then
    echo "헬스체크 성공! 포트 $NEW_PORT 정상 기동"

    # 기존 포트 종료
    sudo fuser -k -n tcp $OLD_PORT || true
    exit 0
  fi
done

# 7. 실패 시 롤백
echo "헬스체크 실패. 롤백 시작"
sudo fuser -k -n tcp $NEW_PORT || true
rm -rf $CURRENT_DIR
cp -r $PREVIOUS_DIR $CURRENT_DIR
cd $CURRENT_DIR
nohup java -jar $APP_NAME.jar --server.port=$OLD_PORT > ./output.log 2>&1 &
exit 1
