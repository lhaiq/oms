component=$1
version=$2
subversion=$3 

ng_repo="http://10.13.0.8:8081/nexus/content/sites/maxent-raw-releases"

tar czf id_service_v2_xconf.tgz xconf
md5sum id_service_v2_xconf.tgz > id_service_v2_xconf.tgz+md5
curl -v --user 'admin:admin123' --upload-file ./id_service_v2_xconf.tgz "$ng_repo/$component/$version/$version.$subversion/id_service_v2_xconf.tgz"
curl -v --user 'admin:admin123' --upload-file ./id_service_v2_xconf.tgz+md5 "$ng_repo/$component/$version/$version.$subversion/id_service_v2_xconf.tgz"+md5

cd ./target; tar czf id_service_v2.tgz *.tar.gz
md5sum id_service_v2.tgz > id_service_v2.tgz+md5
curl -v --user 'admin:admin123' --upload-file ./id_service_v2.tgz "$ng_repo/$component/$version/$version.$subversion/id_service_v2.tgz"
curl -v --user 'admin:admin123' --upload-file ./id_service_v2.tgz+md5 "$ng_repo/$component/$version/$version.$subversion/id_service_v2.tgz+md5"
