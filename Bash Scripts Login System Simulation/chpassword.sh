#! /usr/bin/bash
read -p "username: " var_username
read -sp "new password: " var_password

var_user_exists=0

if [[ -e data/passwd ]]; then

        if [ ! -z "$var_username" ] && [ ! -z "$var_password" ]; then

                var_user_exists=$(cat data/passwd | grep -n $var_username | cut -d":" -f1)

                if [ ! -z $var_user_exists ]; then
                        cat data/passwd | awk -F ":" -v rn=$var_user_exists -v new_pass=$var_password '{if(NR == rn) {gsub($2,new_pass);} print $0 }' > temp && mv -f temp data/passwd
                        echo "Password Changed Successfully!"
                else
                        echo "User Does not Exist!"
                fi
        else
                echo "Usage: ./chpassword username password"
        fi
else
        echo "data/passwd Does not Exist!"
fi
