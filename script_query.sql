
create table Products(

	id int not null auto_increment primary key,
    type varchar(45),
    description varchar(45),
    quantity int not null,
    threshold int not null,
    price numeric(15,2) not null,
    supplier varchar(45)  
);

create table Employees(

	id int not null auto_increment primary key,
    first_name varchar(45),
    last_name varchar(45),
    user_name varchar(45),
    password varchar(45),
    manager_id int null  
);

create table Sales(

	id int not null auto_increment primary key,
    the_date date,
    product_id int not null,
    quantity_sold int not null,
    total_sale numeric(15, 2) not null,
    sale_tax numeric(15, 2) not null,
    employee_id int not null,
    constraint employee_id_fk foreign key(employee_id) references Employees(id) on delete cascade on update cascade    
);

create table Products_sales(

	id int not null auto_increment primary key,
	product_id int not null,
    sales_id int not null,
    the_date date,
    constraint product_id_fk foreign key(product_id) references Products(id) on delete cascade on update cascade,
    constraint sales_id_fk foreign key(sales_id) references Sales(id) on delete cascade on update cascade
);

create table Sales_details(

	id int null auto_increment primary key,
    the_date date,
    total_sale_oftheday numeric(15, 2) not null
);

insert into Employees (first_name, last_name) value("Chirstian", "Mbog");
insert into Employees (first_name, last_name, manager_id) value("Charles", "Smith", 1);
insert into Employees (first_name, last_name) value("Christ", "Madisson");
insert into Employees (first_name, last_name) value("Ibrahim", "Mohamet");
insert into Employees (first_name, last_name) value("Josiane", "Anderson");
insert into Employees (first_name, last_name) value("Mayo", "Yung");

insert into Products(price, threshold, quantity, type, description, supplier) value(1500.99, 15, 150, "phone", "Iphone S 6", "Apple");
insert into Products(price, threshold, quantity, type, description, supplier) value(39.99, 15, 150, "kitchen", "2.20t Hot Air Fryer", "Gourmia");
insert into Products(price, threshold, quantity, type, description, supplier) value(60, 10, 800, "kitchen", "Hot Air Fryer Black", "Gourmia");
insert into Products(price, threshold, quantity, type, description, supplier) value(100, 10, 80, "kitchen", "Nutri Air Fryer", "Chef di..");
insert into Products(price, threshold, quantity, type, description, supplier) value(600, 10, 1000, "laptop", "inspiron", "Dell");
insert into Products(price, threshold, quantity, type, description, supplier) value(1500, 100, 500, "laptop", "Intel COREi7", "Asus");
insert into Products(price, threshold, quantity, type, description, supplier) value(999.99, 10, 1000, "laptop", "Intel COREi7", "HP");
insert into Products(price, threshold, quantity, type, description, supplier) value(1799.99, 15, 70, "refrigerator", "24.1 Cu. Ft.", "LG");
insert into Products(price, threshold, quantity, type, description, supplier) value(499.99, 5, 10, "oven", "5.1 Cu.", "Whirlpool");
insert into Products(price, threshold, quantity, type, description, supplier) value(999.99, 15, 60, "oven", "6.3  Cu", "LG");
insert into Products(price, threshold, quantity, type, description, supplier) value(699.99, 10, 20, "diswasher", "top control built-in", "LG");
insert into Products(price, threshold, quantity, type, description, supplier) value(999.99, 10, 1000, "laptop", "Intel COREi7", "HP");

# # Use trigger to update quantity of product (decrease) and  total sale into Sales_details.
# # Need a local variable: declare tax numeric(2,2); tax = 0.07; use local variable to calaculate the total_sale in sale table.

drop trigger if exists before_Sales_updated;
Delimiter $$
create trigger before_Sales_updated before insert on Sales
	for each row
    begin
		update Products set quantity = quantity - new.quantity_sold where id = new.product_id;
        insert into Sales_details(the_date, total_sale_oftheday) value (date(now()), total_sale_oftheday + new.total_sale);  
        
        ## Check that the quantity is greater than the threshold.
        select quantity, threshodl into q , t from Products where id = new.product_id;
        if q <= t then
			set @output = "The quantity gets to or under the thresold";
			select @output;
        end if;
        
    end $$
delimiter ;

# # Inserting of a sale in the Sales table which triggers 'before_Sales_updated' to update the quantity in the Products table then insert into the Sales_details
drop procedure set_sale;
delimiter $$
create procedure set_sale(in quantity int, in employeId int, in productId int   ) # # In Java pass three parameters 'in' : quantity, employee_id, and product_id. 
	begin
    declare tax numeric(2,2);
    set tax := 0.07; 
    select price into @price from Products where id = 5;
	insert into Sales(the_date, quantity_sold, total_sale, sale_tax, employee_id, product_id) value(date(now()), quantity, @price * quantity_sold, total_sale * tax, employeId, productId );
    end$$  
delimiter ;

# # populate the sale table
call set_sale(1, 3, 7);
insert into Sales(the_date, quantity_sold, total_sale, sale_tax, employee_id, product_id) value("2018-03-20", 1, 1799.99, 125.99, 4, 12 );
insert into Sales(the_date, quantity_sold, total_sale, sale_tax, employee_id, product_id) value("2018-03-19", 2, 39.99, 5.59, 2, 2 );
insert into Sales(the_date, quantity_sold, total_sale, sale_tax, employee_id, product_id) value("2018-03-19", 2, 39.99, 5.59, 2, 2 );
insert into Sales(the_date, quantity_sold, total_sale, sale_tax, employee_id, product_id) value("2018-03-18", 1, 349.99, 24.49, 1, 8 );
update Sales set total_sale = 79.98 where id = 5 and 6;
call set_sale(3, 3, 14);
call set_sale(2, 2, 12);
call set_sale(1, 1, 10);
call set_sale(4, 1, 8);
call set_sale(1, 3, 6);
call set_sale(1, 1, 4);
call set_sale(1, 3, 2);
call set_sale(1, 3, 1);

select  * from Products;

alter table Sales_details add column sale_id int null;
alter table Sales_details drop column sale_id;
alter table Sales_details add foreign key (sale_id) references Sales(id);

use gnchouwat;
select * from Products;
SELECT  id AS sale_id, the_date, product_id, quantity_sold FROM Sales;

# # Get the highest selling
select * from Sales where the_date = "2018-03-24" order by quantity_sold desc;

# # Get the lower selling
SELECT  * FROM Sales WHERE the_date = "2018-03-24" ORDER BY quantity_sold;

update  Products set supplier = "ekids" where id = 11;

select * from Sales;

insert into Sales(the_date, product_id, quantity_sold, total_sale, employee_id) value();

