#! /usr/bin/bash
read -p "username: " var_username
read -sp "password: " var_password

var_user_exists=0

if [[ -e data/passwd ]]; then

        if [ ! -z "$var_username" ] && [ ! -z "$var_password" ]; then

                var_user_exists=$(cat data/passwd | grep -c -w "${var_username}:${var_password}")

                if [ $var_user_exists -ne 0 ]; then
                        echo "Login Success!"
                else
                        echo "Login Failed! Username or Password is incorrect"
                fi
        else
                echo "Usage: ./testlogin username password"
        fi
else
        echo "data/passwd Does not Exist!"
fi
