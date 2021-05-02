#!/bin/bash

# Check 2 arguments are given #
if [ ! $# -eq 2 ]
then
  echo "Usage: $0 token=<token> user=<user>"
  exit
fi

for arg in "$@"
do
  if ! expr match "$arg" "^token\|user=.*$" > /dev/null 
  then
    echo "Usage: $0 token=<token> user=<user>"
    exit
  fi
done 

token_value=""
user_name=""
for arg in "$@"
do
  key=${arg%%=*}
  case $key in
  token) token_value=${arg##*=}
  ;;
  user) user_name=${arg##*=}
  ;;
  esac
done

sed -i "s/%token%/$token_value/g" spring-boot-secrets-demo-bc.yaml
sed -i "s/%user%/$user_name/g" spring-boot-secrets-demo-bc.yaml

oc create -f spring-boot-secrets-demo-bc.yaml
