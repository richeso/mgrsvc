#!/bin/sh
useradd -p $(openssl passwd -1 "chesopass") "cheso"
useradd -p $(openssl passwd -1 "davidpass") "david"
useradd -p $(openssl passwd -1 "vincepass") "vince"
echo "192.168.8.54 demo.mapr.com demo" >> /etc/hosts
/opt/mapr/server/configure.sh -N demo.mapr.com -c -C 192.168.8.54:7222
###cp /app/resources/config/mapr-clusters.conf /opt/mapr/conf/mapr-clusters.conf
###sleep 1d
##exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "com.mapr.msweb.MswebApp"  "$@"
exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp $( cat /app/jib-classpath-file ) $( cat /app/jib-main-class-file ) "$@"

