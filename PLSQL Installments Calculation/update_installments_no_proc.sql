create or replace procedure update_installments_no as
    cursor c_contracts_cursor is
        select
            contract_id,
            contract_startdate,
            contract_enddate, 
            contract_payment_type
        from contracts;
    v_contract_type varchar(50);
    v_month_difference number;
    v_no_installments number;
    begin
        for r_contract_row in c_contracts_cursor
        loop
            v_contract_type := r_contract_row.contract_payment_type;
            v_month_difference := months_between(r_contract_row.contract_enddate, r_contract_row.contract_startdate);
            v_no_installments := case
                                            when v_contract_type = 'Annual' then v_month_difference / 12
                                            when v_contract_type = 'Quarter' then v_month_difference / 3
                                            when v_contract_type = 'Monthly' then v_month_difference
                                            when v_contract_type = 'Half-Annual' then v_month_difference / 6
                                        end;
           update contracts
           set payments_installments_no = v_no_installments
           where contract_id = r_contract_row.contract_id;
        end loop;
    end;