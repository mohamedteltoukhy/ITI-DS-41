select x in manage_user manage_group exit
do

        if [[ $x == "manage_user" ]]
        then
                select y in add_user remove_user test_login change_password back
                do
                        if [[ $y == "add_user" ]]
                        then
                                ./adduser.sh
                                break
                        fi

                        if [[ $y == "remove_user" ]]
                        then
                                ./rmuser.sh
                                break
                        fi

                        if [[ $y == "test_login" ]]
                        then
                                ./testlogin.sh
                                break
                        fi

                        if [[ $y == "change_password" ]]
                        then
                                ./chpassword.sh
                                break
                        fi

                        if [[ $y == "back" ]]
                        then
                                break
                        fi
                done
        fi

        if [[ $x == "manage_group" ]]
        then

                select y in add_group remove_group back
                do

                        if [[ $y == "add_group" ]]
                       then
                                ./addgroup.sh
                                break
                        fi

                        if [[ $y == "remove_group" ]]
                        then

                                ./rmgroup.sh
                                break
                        fi

                        if [[ $y == "back" ]]
                                then
                                break
                        fi

                done

        fi

        if [[ $x == "exit" ]]
        then
                exit
        fi
done