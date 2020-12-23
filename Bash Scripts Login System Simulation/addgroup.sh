#! /usr/bin/bash
current_id=$(tail -1 data/groups | cut -d : -f 2)
if [[ -z $current_id ]]
then
        $current_id=1
fi

        read -p "Group Name: " grpname
        if [[ -n $(grep $grpname data/groups) ]]
        then
                echo "this is an existing group"
        else
                my_id=$(($current_id+1))

                echo "$grpname:$my_id" >> data/groups
                echo "Group Added Successfully!"
        fi
