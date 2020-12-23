create or replace procedure insert_installments as
        cursor c_contracts_cursor is
            select
                contract_id,
                contract_startdate,
                payments_installments_no,
                contract_total_fees,
                contract_deposit_fees,
                contract_payment_type
            from contracts;
        v_installments_no number;
        v_payment_type varchar(50);
        v_remaining_fees number;
        v_installment_amt number;
        v_start_date date;
        v_installment_date date;
        v_months number;
    begin
        for r_contract_row in c_contracts_cursor
        loop
            v_installments_no := r_contract_row.payments_installments_no;
            v_payment_type := r_contract_row.contract_payment_type;
            v_remaining_fees := r_contract_row.contract_total_fees - nvl(r_contract_row.contract_deposit_fees, 0);
            v_installment_amt := v_remaining_fees / v_installments_no;
            v_start_date := r_contract_row.contract_startdate;
            v_installment_date := v_start_date;
            v_months := case
                                    when v_payment_type = 'Annual' then 12
                                    when v_payment_type = 'Quarter' then 3
                                    when v_payment_type = 'Monthly' then 1
                                    when v_payment_type = 'Half-Annual' then 6
                                end;
            for i in 1..v_installments_no
            loop
                insert into installments_paid(contract_id, installment_date, installment_amount, paid)
                values(r_contract_row.contract_id, v_installment_date, v_installment_amt, 0); 
                v_installment_date := add_months(v_installment_date, v_months);
            end loop;
        end loop;
    end;