create table clients (
    client_id number not null primary key,
    client_name varchar(50),
    client_address varchar(50),
    client_notes varchar(50)
);

create table contracts (
    contract_id number not null primary key,
    contract_startdate date,
    contract_enddate date,
    payments_installments_no number,
    contract_total_fees number,
    contract_deposit_fees number,
    client_id number references clients(client_id),
    contract_payment_type varchar(50),
    notes varchar(1500)
);

create table installments_paid (
    installment_id number not null primary key,
    contract_id number references contracts(contract_id),
    installment_date date,
    installment_amount number,
    paid number
);