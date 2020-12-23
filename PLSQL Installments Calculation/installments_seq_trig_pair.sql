create sequence installments_paid_seq
    start with 1
    maxvalue 9999999
    minvalue 1
    cache 20;

create or replace trigger installments_seq_trig
before insert on installments_paid
for each row
begin
    :new.installment_id := installments_paid_seq.nextval;
end;