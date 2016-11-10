#!/bin/bash

rawurlencode() {
  local string="${1}"
  local strlen=${#string}
  local encoded=""
 
  for (( pos=0 ; pos<strlen ; pos++ )); do
     c=${string:$pos:1}
     case "$c" in
        [-_.~a-zA-Z0-9] ) o="${c}" ;;
        * )               printf -v o '%%%02x' "'$c"
     esac
     encoded+="${o}"
  done
  echo "${encoded}"    # You can either set a return variable (FASTER) 
  REPLY="${encoded}"   #+or echo the result (EASIER)... or both... :p
}

USERNAME="admin";

if [ "$#" -ne 1 ]; then
    echo "Enter please ROUTER IP";
    exit;
fi




clear;
echo -e "Script for managment router TP-link by Sergey Zhuravel\n"
echo "Connect to $1" 

IP=$1;
IP_ADDRESS=`ping -c 2 $IP | grep -c "icmp"`;

#  ping ip address
if [ $IP_ADDRESS -eq 0 ]; then
	echo "ping error";
	exit;
fi

# input password
printf 'Enter passsword: '
read -s PASSWORD

# lenfth pasword
PASSWORD_LENGTH=`echo -n $PASSWORD | wc -c`

# check on length password
if [ $PASSWORD_LENGTH -gt 0 ]; then
		echo -e "\n---"
	else
		echo "You don't enter password"
		exit 1;

fi



PASSWORD_MD5=`echo -n $PASSWORD | md5sum | cut -d " " -f 1`;
COOKIE_B64_PART=`echo -n $USERNAME":"$(echo -n $PASSWORD_MD5)|base64`;
COOKIEVAL_UNENCODED=`echo -n "Basic $COOKIE_B64_PART"`;
COOKIEVAL=`rawurlencode "$COOKIEVAL_UNENCODED"`

GET_KEY_URL=`echo "http://$IP/userRpm/LoginRpm.htm?Save=Save"`
RESPONSE=`curl -s --cookie "Authorization=$COOKIEVAL" $GET_KEY_URL`;
KEY=`echo $RESPONSE |  head -n 1 | cut -d '/' -f 4` # extract key from post-login-page

KEY_LENGTH=`echo -n $KEY | wc -c`

# check version tp-link
if [ $KEY_LENGTH -gt 0 ]; then

# validate password
VALIDATE_PASS=`curl -s --user admin:$PASSWORD --referer http://$IP/$KEY/userRpm/StatusRpm.htm http://$IP/$KEY/userRpm/StatusRpm.htm | grep -c Save`
	if [ $VALIDATE_PASS -gt 0 ]; then
		echo "PASS OK"
		echo "---"
		else
			echo "PASSWORD ERROR"
			exit;

	fi


	echo "Version type: new"
	./conf_tplink.cfg $IP $PASSWORD

else

	# validate password
VALIDATE_PASS=`curl -s --user admin:$PASSWORD --referer http://$IP/userRpm/MenuRpm.htm http://$IP/userRpm/MenuRpm.htm | grep -c visible`

	if [ $VALIDATE_PASS -gt 0 ]; then
		echo "PASS OK"
		echo "---"
		else
			echo "PASSWORD ERROR"
			exit;

	fi

	echo "Version type: old"
	./conf_tplink_old.cfg $IP $PASSWORD

fi


