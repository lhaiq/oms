#!/usr/bin/env bash
component=$1
branch=$2

if [ $branch == develop ]; then
    salt_master=10.18.1.10
elif [[ $branch == release/* ]] || [[ $branch == hotfix/* ]]; then
    salt_master=10.17.0.7
fi

ng_repo="http://10.13.0.8:8081/nexus/content/sites/maxent-raw-ci"


head=`git ls-remote --heads git@gitlab.maxent-inc.com:id/id-service-v2.git |grep $branch |cut -f1`

tar czf id_service_v2_xconf.tgz xconf
md5sum  id_service_v2_xconf.tgz > id_service_v2_xconf.tgz+md5
curl -v --user 'admin:admin123' --upload-file ./id_service_v2_xconf.tgz "$ng_repo/$component/$head/id_service_v2_xconf.tgz"
curl -v --user 'admin:admin123' --upload-file ./id_service_v2_xconf.tgz+md5 "$ng_repo/$component/$head/id_service_v2_xconf.tgz+md5"

cd ./target; tar czf id_service_v2.tgz *.tar.gz
md5sum id_service_v2.tgz > id_service_v2.tgz+md5
curl -v --user 'admin:admin123' --upload-file ./id_service_v2.tgz "$ng_repo/$component/$head/id_service_v2.tgz"
curl -v --user 'admin:admin123' --upload-file ./id_service_v2.tgz+md5 "$ng_repo/$component/$head/id_service_v2.tgz+md5"

#echo ssh track@10.18.1.10 "sudo salt -I 'roles:id_service_v2' state.sls pillar=\"{'CI':'yes','head':'$head'}\" id_service_v2"
ssh track@$salt_master "sudo salt '*' saltutil.refresh_pillar;sudo salt -I 'roles:id_service_v2' state.sls pillar=\"{'CI':'yes','head':'$head'}\" id_service_v2"

#deliberately modify to trigger ci test
#deliberately modify to trigger ci test again
#deliberately modify to trigger ci test once again
