mvn -Dmaven.test.skip=true clean package install

cd modules/app-version-manager

mvn clean package -Dmaven.test.skip=true -P fatjar

cd ../app-version-rest

mvn clean package -Dmaven.test.skip=true -P fatjar