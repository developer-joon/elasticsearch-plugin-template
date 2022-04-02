## 소개

엘라스틱서치 플러그인 기초 프로젝트입니다.


## 사용방법

1. https://www.elastic.co/kr/downloads/elasticsearch 환경에 맞는 엘라스틱서치를 다운로드 합니다.
2. 해당 프로젝트를 IDE에서 오픈합니다.
3. 다운로드받은 엘라스틱서치에 있는 jdk를 프로젝트에 설정합니다.
4. build.gradle 파일에서 엘라스틱서치 관련 버전을 수정합니다.
5. Application 실행 설정을 구성합니다.
   1. VM 설정 아래 3가지를 추가합니다.
      -Des.path.home=<경로>/elasticsearch   
      -Des.path.conf=<경로>/elasticsearch/config   
      -Des.logs.base_path=<경로>/elasticsearch/logs   
   2. 실행 클래스를 입력합니다.
      1. org.elasticsearch.bootstrap.Elasticsearch
6. 다운로드받은 elasticsearch/jdk/conf/security/java.policy 파일에 아래 내용을 추가합니다.   
   permission javax.management.MBeanTrustPermission "register";   
   permission javax.management.MBeanServerPermission "createMBeanServer";   
   permission java.lang.RuntimePermission "getClassLoader";   
7. 프로젝트를 실행합니다. 
8. 정상적으로 엘라스틱서치가 실행되면 https://www.elastic.co/guide/en/elasticsearch/plugins/current/plugin-authors.html 플러그인 개발 방법을 참고하고 진행합니다.

