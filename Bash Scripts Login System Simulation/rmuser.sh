#! /usr/bin/bash
read -p "username: " var_username

if [[ -e data/passwd ]]; then

        if [ ! -z "$var_username" ]; then

                line_number=$(cat data/passwd | grep -n $var_username | cut -d':' -f1)

                if [ ! -z $line_number  ]; then
                        sed -i "${line_number}d" data/passwd
                        echo "User Removed Successfully!"
                else
                        echo "User Does not  Exist!"
                fi
        else
                echo "Usage: ./rmuser username"
        fi
else
        echo "data/passwd Does not Exist!"
fi
