alter table shipment add column flight_number VARCHAR(255)  null;
alter table shipment add column airline VARCHAR(255)  null;
alter table shipment add column departure_airport VARCHAR(255)  null;
alter table shipment add column destination_airport VARCHAR(255)  null;
alter table shipment add column departure_time VARCHAR(255)  null;
alter table shipment add column destination_time VARCHAR(255)  null;
alter table shipment add column price double  null;