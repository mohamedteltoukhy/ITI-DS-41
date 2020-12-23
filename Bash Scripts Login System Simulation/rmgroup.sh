read -p "Group Name: " grp_to_rm
if [[ -z $(grep $grp_to_rm data/groups) ]]
then
        echo "This Group Does not Exist!"
        break
fi


id_to_rm=$(grep $grp_to_rm data/groups | cut -d : -f 2)

if [[ -n $(cut -d : -f 4 data/passwd | grep $id_to_rm) ]]
then

        echo "There are users within the group, Please Remove them first"
else
        sed -i "/${grp_to_rm}/d" data/groups

        echo "Group Removed Successfully!"
        break
fi