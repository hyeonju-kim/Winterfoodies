#CodeDeploy에서 바로 deploy.sh를 실행시킬수 없어 우회하는 방법으로 deploy.sh를 실행하는 execute-deploy.sh파일을 실행하도록 설정

#!/bin/bash
/home/ec2-user/app/travis/deploy.sh > /dev/null 2> /dev/null < /dev/null &