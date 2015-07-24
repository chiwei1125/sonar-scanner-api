#!/bin/bash

set -euo pipefail

function installTravisTools {
  mkdir ~/.local
  curl -sSL https://github.com/SonarSource/travis-utils/tarball/v15 | tar zx --strip-components 1 -C ~/.local
  source ~/.local/bin/install
}

case "$TESTS" in

CI)
  installTravisTools

  build_snapshot "SonarSource/sonarqube"

  mvn verify -B -e -V
  ;;

IT-DEV)
  installTravisTools

  build_snapshot "SonarSource/sonarqube"

  mvn install -Dsource.skip=true -Denforcer.skip=true -Danimal.sniffer.skip=true -Dmaven.test.skip=true

  cd it
  mvn -DsonarRunner.version="2.5-SNAPSHOT" -Dsonar.runtimeVersion="DEV" -Dmaven.test.redirectTestOutputToFile=false install
  ;;

esac
