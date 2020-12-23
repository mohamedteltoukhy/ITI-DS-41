#! /usr/bin/bash
read -p "username: " var_username
read -sp "password: " var_password
read -p "group id: " var_groupid
var_userid=0

var_user_exists=0
var_grp_exists=0

if [[ -e data/passwd ]]; then

        if [ ! -z "$var_username" ] && [ ! -z "$var_password" ] && [ ! -z "$var_groupid" ]; then

                var_user_exists=$(cat data/passwd | grep -c $var_username)
                var_grp_exists=$(cat data/groups | grep -c $var_groupid)

                if [ $var_user_exists -eq 0 ] && [ $var_grp_exists -ne 0 ]; then

                        var_userid=$(cat data/passwd | tail -n 1 | cut -d':' -f3)
                        var_userid=$((var_userid+1))
                        echo "${var_username}:${var_password}:${var_userid}:${var_groupid}" >> data/passwd
                        echo "User Added Successfully!"

                elif [[ $var_user_exists -ne 0  ]]; then
                        echo "User Exists!"

                else
                        echo "Group Does not Exist!"
                fi
        else
                echo "Usage: ./adduser username password groupid"
        fi
else
        echo "data/passwd Does not Exist!"
fi
