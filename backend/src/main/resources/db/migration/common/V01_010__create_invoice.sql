create table invoice (id bigint not null auto_increment,
  invoice_number varchar(255) not null,
  shipment_id bigint not null,
  invoice_creation_date TIMESTAMP not null,
  pre_carriage double not null,
  export_insurance double not null,
  export_customs_clearance double not null,
  flight_price double not null,
  import_insurance double not null,
  import_customs_clearance double not null,
  on_carriage double not null,
  management_fee double not null,
  service_fee double not null,
  discount double not null,
  primary key (id));

alter table invoice add constraint UK_zzj4c1bm89aoqdnscu6v85rkt unique (invoice_number);
alter table invoice add constraint DE_4568c1bm89aoqdnscu6v88754 foreign key (shipment_id) references shipment (id) on delete cascade;

